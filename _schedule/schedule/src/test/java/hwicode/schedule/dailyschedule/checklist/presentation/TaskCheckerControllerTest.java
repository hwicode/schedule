package hwicode.schedule.dailyschedule.checklist.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.exception.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.TaskCheckerController;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyResponse;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskCheckerController.class)
class TaskCheckerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskCheckerSubService taskCheckerSubService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);
        TaskSaveResponse taskSaveResponse = new TaskSaveResponse(TASK_CHECKER_ID, TASK_CHECKER_NAME);

        given(taskCheckerSubService.saveTaskChecker(any()))
                .willReturn(TASK_CHECKER_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskSaveResponse)
                ));

        verify(taskCheckerSubService).saveTaskChecker(any());
    }

    @Test
    void 과제를_저장할_때_실패하면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();

        given(taskCheckerSubService.saveTaskChecker(any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TaskSaveRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(taskCheckerSubService).saveTaskChecker(any());
    }

    @Test
    void 과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}", DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .param("taskName", TASK_CHECKER_NAME)
        );

        // then
        perform.andExpect(status().isNoContent());

        verify(taskCheckerSubService).deleteTaskChecker(any(), any(), any());
    }

    @Test
    void 과제를_삭제할_때_실패하면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();

        given(taskCheckerSubService.deleteTaskChecker(any(), any(), any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}", DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .param("taskName", TASK_CHECKER_NAME)
        );

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(taskCheckerSubService).deleteTaskChecker(any(), any(), any());
    }

    @Test
    void 과제체커의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskStatusModifyRequest taskStatusModifyRequest = new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, TaskStatus.DONE);
        TaskStatusModifyResponse taskStatusModifyResponse = new TaskStatusModifyResponse(TASK_CHECKER_NAME, TaskStatus.DONE);

        given(taskCheckerSubService.changeTaskStatus(any(), any()))
                .willReturn(TaskStatus.DONE);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskStatusModifyResponse)
                ));

        verify(taskCheckerSubService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제체커의_어려움_점수의_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskDifficultyModifyRequest taskDifficultyModifyRequest = new TaskDifficultyModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, Difficulty.HARD);
        TaskDifficultyModifyResponse taskDifficultyModifyResponse = new TaskDifficultyModifyResponse(TASK_CHECKER_NAME, Difficulty.HARD);

        given(taskCheckerSubService.changeTaskDifficulty(any(), any()))
                .willReturn(Difficulty.HARD);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/difficulty",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDifficultyModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskDifficultyModifyResponse)
                ));

        verify(taskCheckerSubService).changeTaskDifficulty(any(), any());
    }

    @Test
    void 과제체커의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskCheckerNameModifyRequest taskCheckerNameModifyRequest = new TaskCheckerNameModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, NEW_TASK_CHECKER_NAME);
        TaskCheckerNameModifyResponse taskCheckerNameModifyResponse = new TaskCheckerNameModifyResponse(DAILY_CHECKLIST_ID, NEW_TASK_CHECKER_NAME);

        given(taskCheckerSubService.changeTaskCheckerName(any(), any()))
                .willReturn(NEW_TASK_CHECKER_NAME);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/name",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskCheckerNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskCheckerNameModifyResponse)
                ));

        verify(taskCheckerSubService).changeTaskCheckerName(any(), any());
    }

    @Test
    void 과제체커의_진행_상태_변경을_요청할_때_진행_상태가_존재하지_않는다면_에러가_발생한다() throws Exception {
        // given
        StatusNotFoundException statusNotFoundException = new StatusNotFoundException();
        given(taskCheckerSubService.changeTaskStatus(any(), any()))
                .willThrow(statusNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(statusNotFoundException.getMessage()));

        verify(taskCheckerSubService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제체커를_찾을_때_과제체커가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskCheckerNotFoundException taskCheckerNotFoundException = new TaskCheckerNotFoundException();
        given(taskCheckerSubService.changeTaskStatus(any(), any()))
                .willThrow(taskCheckerNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskCheckerNotFoundException.getMessage()));

        verify(taskCheckerSubService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제체커의_진행_상태를_DONE으로_변경을_요청할_때_서브_과제체커의_진행_상태가_모두_DONE이_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotAllDoneException subTaskCheckerNotAllDoneException = new SubTaskCheckerNotAllDoneException();
        given(taskCheckerSubService.changeTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotAllDoneException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotAllDoneException.getMessage()));

        verify(taskCheckerSubService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제체커의_진행_상태를_TODO로_변경을_요청할_때_서브_과제체커의_진행_상태가_모두_TODO가_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotAllTodoException subTaskCheckerNotAllTodoException = new SubTaskCheckerNotAllTodoException();
        given(taskCheckerSubService.changeTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotAllTodoException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/status",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotAllTodoException.getMessage()));

        verify(taskCheckerSubService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제체커의_이름_변경을_요청할_때_과제체커의_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        TaskCheckerNameDuplicationException taskCheckerNameDuplicationException = new TaskCheckerNameDuplicationException();
        given(taskCheckerSubService.changeTaskCheckerName(any(), any()))
                .willThrow(taskCheckerNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/name",
                        DAILY_CHECKLIST_ID, TASK_CHECKER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TaskCheckerNameModifyRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, NEW_TASK_CHECKER_NAME)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(taskCheckerNameDuplicationException.getMessage()));

        verify(taskCheckerSubService).changeTaskCheckerName(any(), any());
    }

}
