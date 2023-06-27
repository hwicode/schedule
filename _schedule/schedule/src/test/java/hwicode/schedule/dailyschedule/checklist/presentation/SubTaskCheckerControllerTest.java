package hwicode.schedule.dailyschedule.checklist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerAggregateService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.application.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.SubTaskCheckerController;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubTaskCheckerController.class)
class SubTaskCheckerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SubTaskCheckerSubService subTaskCheckerSubService;

    @MockBean
    TaskCheckerAggregateService taskCheckerAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 서브_과제체커의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        SubTaskStatusModifyResponse subTaskStatusModifyResponse = new SubTaskStatusModifyResponse(SUB_TASK_CHECKER_NAME, TaskStatus.PROGRESS, SubTaskStatus.DONE);

        given(subTaskCheckerSubService.changeSubTaskStatus(any(), any()))
                .willReturn(TaskStatus.PROGRESS);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTaskStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskStatusModifyResponse)
                ));

        verify(subTaskCheckerSubService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 서브_과제체커의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = new SubTaskCheckerNameModifyRequest(TASK_CHECKER_ID, SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);
        SubTaskCheckerNameModifyResponse subTaskCheckerNameModifyResponse = new SubTaskCheckerNameModifyResponse(TASK_CHECKER_ID, NEW_SUB_TASK_CHECKER_NAME);

        given(taskCheckerAggregateService.changeSubTaskCheckerName(any(), any()))
                .willReturn(NEW_SUB_TASK_CHECKER_NAME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name",
                        TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTaskCheckerNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskCheckerNameModifyResponse)
                ));

        verify(taskCheckerAggregateService).changeSubTaskCheckerName(any(), any());
    }

    @Test
    void 서브_과제체커를_찾을_때_서브_과제체커가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotFoundException subTaskCheckerNotFoundException = new SubTaskCheckerNotFoundException();
        given(subTaskCheckerSubService.changeSubTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new SubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotFoundException.getMessage()));

        verify(subTaskCheckerSubService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 체크리스트를_조회할_때_체크리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();
        given(subTaskCheckerSubService.changeSubTaskStatus(any(), any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new SubTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskCheckerSubService).changeSubTaskStatus(any(), any());
    }

    @Test
    void 과제체커를_조회할_때_과제체커가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskCheckerNotFoundException taskCheckerNotFoundException = new TaskCheckerNotFoundException();
        given(taskCheckerAggregateService.changeSubTaskCheckerName(any(), any()))
                .willThrow(taskCheckerNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name",
                        TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new SubTaskCheckerNameModifyRequest(TASK_CHECKER_ID, SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskCheckerNotFoundException.getMessage()));

        verify(taskCheckerAggregateService).changeSubTaskCheckerName(any(), any());
    }

    @Test
    void 서브_과제체커의_이름_변경을_요청할_때_서브_과제체커의_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNameDuplicationException subTaskCheckerNameDuplicationException = new SubTaskCheckerNameDuplicationException();
        given(taskCheckerAggregateService.changeSubTaskCheckerName(any(), any()))
                .willThrow(subTaskCheckerNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name",
                        TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskCheckerNameModifyRequest(TASK_CHECKER_ID, SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNameDuplicationException.getMessage()));

        verify(taskCheckerAggregateService).changeSubTaskCheckerName(any(), any());
    }

}
