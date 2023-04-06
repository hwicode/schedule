package hwicode.schedule.dailyschedule.checklist.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.presentation.task.TaskController;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.save.TaskSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task.task_dto.status_modify.TaskStatusModifyResponse;
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

@WebMvcTest(TaskController.class)
class TaskCheckerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = createTaskSaveRequest(DAILY_CHECKLIST_ID, NEW_TASK_NAME);
        TaskSaveResponse taskSaveResponse = createTaskSaveResponse(TASK_ID, NEW_TASK_NAME);

        given(taskService.saveTask(any()))
                .willReturn(TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskSaveResponse)
                ));

        verify(taskService).saveTask(any());
    }

    @Test
    void 과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        TaskDeleteRequest taskDeleteRequest = createTaskDeleteRequest(DAILY_CHECKLIST_ID);

        // when then
        mockMvc.perform(delete("/dailyschedule/checklist/tasks/taskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(any(), any());
    }

    @Test
    void 과제의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskStatusModifyRequest taskStatusModifyRequest = createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE);
        TaskStatusModifyResponse taskStatusModifyResponse = createTaskStatusModifyResponse(TASK_NAME, TaskStatus.DONE);

        given(taskService.changeTaskStatus(any(), any()))
                .willReturn(TaskStatus.DONE);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/tasks/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskStatusModifyResponse)
                ));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_어려움_점수의_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskDifficultyModifyRequest taskDifficultyModifyRequest = createTaskDifficultyModifyRequest(DAILY_CHECKLIST_ID, Difficulty.HARD);
        TaskDifficultyModifyResponse taskDifficultyModifyResponse = createTaskDifficultyModifyResponse(TASK_NAME, Difficulty.HARD);

        given(taskService.changeTaskDifficulty(any(), any()))
                .willReturn(Difficulty.HARD);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/tasks/taskName/difficulty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDifficultyModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskDifficultyModifyResponse)
                ));

        verify(taskService).changeTaskDifficulty(any(), any());
    }

    @Test
    void 과제_생성을_요청할_때_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        TaskNameDuplicationException taskNameDuplicationException = new TaskNameDuplicationException();
        given(taskService.saveTask(any()))
                .willThrow(taskNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskSaveRequest(DAILY_CHECKLIST_ID, NEW_TASK_NAME)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(taskNameDuplicationException.getMessage()));

        verify(taskService).saveTask(any());
    }

    @Test
    void 과제의_진행_상태_변경을_요청할_때_진행_상태가_존재하지_않는다면_에러가_발생한다() throws Exception {
        // given
        StatusNotFoundException statusNotFoundException = new StatusNotFoundException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(statusNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/tasks/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(statusNotFoundException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제를_찾을_때_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskNotFoundException taskNotFoundException = new TaskNotFoundException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(taskNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/tasks/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskNotFoundException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_진행_상태를_DONE으로_변경을_요청할_때_서브_과제의_진행_상태가_모두_DONE이_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotAllDoneException subTaskCheckerNotAllDoneException = new SubTaskCheckerNotAllDoneException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotAllDoneException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/tasks/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotAllDoneException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_진행_상태를_TODO로_변경을_요청할_때_서브_과제의_진행_상태가_모두_TODO가_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotAllTodoException subTaskCheckerNotAllTodoException = new SubTaskCheckerNotAllTodoException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotAllTodoException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/tasks/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotAllTodoException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

}
