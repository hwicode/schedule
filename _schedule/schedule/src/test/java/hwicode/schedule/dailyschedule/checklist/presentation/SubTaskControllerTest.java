package hwicode.schedule.dailyschedule.checklist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.SubTaskService;
import hwicode.schedule.dailyschedule.checklist.domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.dailyckecklist_find_service.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotFoundException;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.SubTaskController;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.save.SubTaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtask.subtask_dto.status_modify.SubTaskStatusModifyResponse;
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

@WebMvcTest(SubTaskController.class)
public class SubTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubTaskService subTaskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 서브_과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskSaveRequest subTaskSaveRequest = createSubTaskSaveRequest(DAILY_CHECKLIST_ID, TASK_NAME, NEW_SUB_TASK_NAME);
        SubTaskSaveResponse subTaskSaveResponse = createSubTaskSaveResponse(SUB_TASK_ID, NEW_SUB_TASK_NAME);

        given(subTaskService.saveSubTask(any()))
                .willReturn(SUB_TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/subtasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTaskSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                                objectMapper.writeValueAsString(subTaskSaveResponse)
                        ));

        verify(subTaskService).saveSubTask(any());
    }

    @Test
    void 서브_과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskDeleteRequest subTaskDeleteRequest = createSubTaskDeleteRequest(DAILY_CHECKLIST_ID, TASK_NAME);

        // when then
        mockMvc.perform(delete("/dailyschedule/checklist/subtasks/subTaskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(subTaskService).deleteSubTask(any(), any());
    }

    @Test
    void 서브_과제의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskStatusModifyRequest subTaskStatusModifyRequest = createSubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_NAME, SubTaskStatus.DONE);
        SubTaskStatusModifyResponse subTaskStatusModifyResponse = createSubTaskStatusModifyResponse(SUB_TASK_NAME, TaskStatus.PROGRESS, SubTaskStatus.DONE);

        given(subTaskService.changeSubTaskStatus(any(), any()))
                .willReturn(TaskStatus.PROGRESS);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/subtasks/subTaskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTaskStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskStatusModifyResponse)
                ));

        verify(subTaskService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 서브_과제_생성을_요청할_때_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        SubTaskNameDuplicationException subTaskNameDuplicationException = new SubTaskNameDuplicationException();
        given(subTaskService.saveSubTask(any()))
                .willThrow(subTaskNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/subtasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createSubTaskSaveRequest(DAILY_CHECKLIST_ID, TASK_NAME, NEW_SUB_TASK_NAME)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskNameDuplicationException.getMessage()));

        verify(subTaskService).saveSubTask(any());
    }

    @Test
    void 서브_과제를_찾을_때_서브_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubTaskNotFoundException subTaskNotFoundException = new SubTaskNotFoundException();
        given(subTaskService.changeSubTaskStatus(any(), any()))
                .willThrow(subTaskNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/subtasks/subTaskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createSubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_NAME, SubTaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subTaskNotFoundException.getMessage()));

        verify(subTaskService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 체크리스트를_조회할_때_체크리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();
        given(subTaskService.changeSubTaskStatus(any(), any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/subtasks/subTaskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createSubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_NAME, SubTaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskService).changeSubTaskStatus(any(), any());
    }
}
