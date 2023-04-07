package hwicode.schedule.dailyschedule.checklist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskCheckerService;
import hwicode.schedule.dailyschedule.common.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.dailyckecklist_find_service.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.SubTaskCheckerController;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.delete.SubTaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.save.SubTaskCheckerSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask_checker.dto.status_modify.SubTaskStatusModifyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubTaskCheckerController.class)
class SubTaskCheckerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SubTaskCheckerService subTaskCheckerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 서브_과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskCheckerSaveRequest subTaskCheckerSaveRequest = createSubTaskSaveRequest(DAILY_CHECKLIST_ID, TASK_NAME, NEW_SUB_TASK_NAME);
        SubTaskCheckerSaveResponse subTaskCheckerSaveResponse = createSubTaskSaveResponse(SUB_TASK_ID, NEW_SUB_TASK_NAME);

        given(subTaskCheckerService.saveSubTask(any()))
                .willReturn(SUB_TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/subtaskCheckers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTaskCheckerSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                                objectMapper.writeValueAsString(subTaskCheckerSaveResponse)
                        ));

        verify(subTaskCheckerService).saveSubTask(any());
    }

    @Test
    void 서브_과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskCheckerDeleteRequest subTaskCheckerDeleteRequest = createSubTaskDeleteRequest(DAILY_CHECKLIST_ID, TASK_NAME);

        // when then
        mockMvc.perform(delete("/dailyschedule/checklist/subtaskCheckers/subTaskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskCheckerDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(subTaskCheckerService).deleteSubTask(any(), any());
    }

    @Test
    void 서브_과제의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskStatusModifyRequest subTaskStatusModifyRequest = createSubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_NAME, SubTaskStatus.DONE);
        SubTaskStatusModifyResponse subTaskStatusModifyResponse = createSubTaskStatusModifyResponse(SUB_TASK_NAME, TaskStatus.PROGRESS, SubTaskStatus.DONE);

        given(subTaskCheckerService.changeSubTaskStatus(any(), any()))
                .willReturn(TaskStatus.PROGRESS);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/subtaskCheckers/subTaskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTaskStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskStatusModifyResponse)
                ));

        verify(subTaskCheckerService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 서브_과제_생성을_요청할_때_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNameDuplicationException subTaskCheckerNameDuplicationException = new SubTaskCheckerNameDuplicationException();
        given(subTaskCheckerService.saveSubTask(any()))
                .willThrow(subTaskCheckerNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/subtaskCheckers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createSubTaskSaveRequest(DAILY_CHECKLIST_ID, TASK_NAME, NEW_SUB_TASK_NAME)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNameDuplicationException.getMessage()));

        verify(subTaskCheckerService).saveSubTask(any());
    }

    @Test
    void 서브_과제를_찾을_때_서브_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotFoundException subTaskCheckerNotFoundException = new SubTaskCheckerNotFoundException();
        given(subTaskCheckerService.changeSubTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/subtaskCheckers/subTaskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createSubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_NAME, SubTaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotFoundException.getMessage()));

        verify(subTaskCheckerService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 체크리스트를_조회할_때_체크리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();
        given(subTaskCheckerService.changeSubTaskStatus(any(), any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/subtaskCheckers/subTaskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createSubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_NAME, SubTaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskCheckerService).changeSubTaskStatus(any(), any());
    }
}
