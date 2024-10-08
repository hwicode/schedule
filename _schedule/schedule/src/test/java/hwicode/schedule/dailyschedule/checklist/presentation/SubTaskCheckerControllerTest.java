package hwicode.schedule.dailyschedule.checklist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerAggregateService;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.SubTaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.save.SubTaskSaveResponse;
import hwicode.schedule.dailyschedule.shared_domain.SubTaskStatus;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.SubTaskCheckerController;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.name_modify.SubTaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.subtaskchecker.dto.status_modify.SubTaskStatusModifyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
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

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SubTaskCheckerSubService subTaskCheckerSubService;

    @MockBean
    TaskCheckerAggregateService taskCheckerAggregateService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 서브_과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME);
        SubTaskSaveResponse subTaskSaveResponse = new SubTaskSaveResponse(SUB_TASK_CHECKER_ID, SUB_TASK_CHECKER_NAME);

        given(subTaskCheckerSubService.saveSubTaskChecker(any()))
                .willReturn(SUB_TASK_CHECKER_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskSaveRequest)));
        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskSaveResponse)
                ));

        verify(subTaskCheckerSubService).saveSubTaskChecker(any());
    }

    @Test
    void 서브_과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .param("taskName", TASK_CHECKER_NAME)
                        .param("subTaskName", SUB_TASK_CHECKER_NAME)
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(subTaskCheckerSubService).deleteSubTaskChecker(any());
    }

    @Test
    void 서브_과제를_저장할_때_실패하면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();

        given(subTaskCheckerSubService.saveSubTaskChecker(any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskSaveRequest(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskCheckerSubService).saveSubTaskChecker(any());
    }

    @Test
    void 서브_과제를_삭제할_때_실패하면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();

        given(subTaskCheckerSubService.deleteSubTaskChecker(any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .param("taskName", TASK_CHECKER_NAME)
                        .param("subTaskName", SUB_TASK_CHECKER_NAME)
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskCheckerSubService).deleteSubTaskChecker(any());
    }

    @Test
    void 서브_과제체커의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskStatusModifyRequest subTaskStatusModifyRequest = new SubTaskStatusModifyRequest(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE);
        SubTaskStatusModifyResponse subTaskStatusModifyResponse = new SubTaskStatusModifyResponse(SUB_TASK_CHECKER_NAME, TaskStatus.PROGRESS, SubTaskStatus.DONE);

        given(subTaskCheckerSubService.changeSubTaskStatus(any()))
                .willReturn(TaskStatus.PROGRESS);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskStatusModifyResponse)
                ));

        verify(subTaskCheckerSubService).changeSubTaskStatus(any());
    }

    @Test
    void 서브_과제체커의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskCheckerNameModifyRequest subTaskCheckerNameModifyRequest = new SubTaskCheckerNameModifyRequest(SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME);
        SubTaskCheckerNameModifyResponse subTaskCheckerNameModifyResponse = new SubTaskCheckerNameModifyResponse(TASK_CHECKER_ID, NEW_SUB_TASK_CHECKER_NAME);

        given(taskCheckerAggregateService.changeSubTaskCheckerName(any()))
                .willReturn(NEW_SUB_TASK_CHECKER_NAME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name",
                        TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskCheckerNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskCheckerNameModifyResponse)
                ));

        verify(taskCheckerAggregateService).changeSubTaskCheckerName(any());
    }

    @Test
    void 서브_과제체커를_찾을_때_서브_과제체커가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotFoundException subTaskCheckerNotFoundException = new SubTaskCheckerNotFoundException();
        given(subTaskCheckerSubService.changeSubTaskStatus(any()))
                .willThrow(subTaskCheckerNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskStatusModifyRequest(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotFoundException.getMessage()));

        verify(subTaskCheckerSubService).changeSubTaskStatus(any());
    }

    @Test
    void 체크리스트를_조회할_때_체크리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();
        given(subTaskCheckerSubService.changeSubTaskStatus(any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskStatusModifyRequest(TASK_CHECKER_NAME, SUB_TASK_CHECKER_NAME, SubTaskStatus.DONE)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskCheckerSubService).changeSubTaskStatus(any());
    }

    @Test
    void 과제체커를_조회할_때_과제체커가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskCheckerNotFoundException taskCheckerNotFoundException = new TaskCheckerNotFoundException();
        given(taskCheckerAggregateService.changeSubTaskCheckerName(any()))
                .willThrow(taskCheckerNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name",
                        TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskCheckerNameModifyRequest(SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskCheckerNotFoundException.getMessage()));

        verify(taskCheckerAggregateService).changeSubTaskCheckerName(any());
    }

    @Test
    void 서브_과제체커의_이름_변경을_요청할_때_서브_과제체커의_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNameDuplicationException subTaskCheckerNameDuplicationException = new SubTaskCheckerNameDuplicationException();
        given(taskCheckerAggregateService.changeSubTaskCheckerName(any()))
                .willThrow(subTaskCheckerNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/subtasks/{subTaskId}/name",
                        TASK_CHECKER_ID, SUB_TASK_CHECKER_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskCheckerNameModifyRequest(SUB_TASK_CHECKER_NAME, NEW_SUB_TASK_CHECKER_NAME)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNameDuplicationException.getMessage()));

        verify(taskCheckerAggregateService).changeSubTaskCheckerName(any());
    }

}
