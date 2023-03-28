package hwicode.schedule.dailyschedule.checklist.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.TaskService;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify.TaskStatusModifyRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_CHECKLIST_ID, TASK_NAME);
        given(taskService.saveTask(any()))
                .willReturn(TASK_ID);

        // when then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskSaveRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(TASK_ID))
                .andExpect(jsonPath("$.taskName").value(TASK_NAME));

        verify(taskService).saveTask(any());
    }

    @Test
    void 과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        TaskDeleteRequest taskDeleteRequest = new TaskDeleteRequest(DAILY_CHECKLIST_ID);

        // when then
        mockMvc.perform(delete("/tasks/taskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(any(), any());
    }

    @Test
    void 과제의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskStatusModifyRequest taskStatusModifyRequest = new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, Status.DONE);
        given(taskService.changeTaskStatus(any(), any()))
                .willReturn(Status.DONE);

        // when then
        mockMvc.perform(patch("/tasks/taskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value(TASK_NAME))
                .andExpect(jsonPath("$.taskStatus").value("DONE"));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_어려움_점수의_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskDifficultyModifyRequest taskDifficultyModifyRequest = new TaskDifficultyModifyRequest(DAILY_CHECKLIST_ID, Difficulty.HARD);
        given(taskService.changeTaskDifficulty(any(), any()))
                .willReturn(Difficulty.HARD);

        // when then
        mockMvc.perform(patch("/tasks/taskName/difficulty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDifficultyModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value(TASK_NAME))
                .andExpect(jsonPath("$.modifiedDifficulty").value("HARD"));

        verify(taskService).changeTaskDifficulty(any(), any());
    }

    @Test
    void 과제_생성을_요청할_때_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        TaskNameDuplicationException taskNameDuplicationException = new TaskNameDuplicationException();
        given(taskService.saveTask(any()))
                .willThrow(taskNameDuplicationException);

        // when then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskSaveRequest(DAILY_CHECKLIST_ID, TASK_NAME))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(taskNameDuplicationException.getMessage()));

        verify(taskService).saveTask(any());
    }

    @Test
    void 과제의_진행_상태_변경을_요청할_때_진행_상태가_존재하지_않는다면_에러가_발생한다() throws Exception {
        // given
        StatusNotFoundException statusNotFoundException = new StatusNotFoundException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(statusNotFoundException);

        // when then
        mockMvc.perform(patch("/tasks/taskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, Status.DONE))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(statusNotFoundException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제를_찾을_때_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskNotFoundException taskNotFoundException = new TaskNotFoundException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(taskNotFoundException);

        // when then
        mockMvc.perform(patch("/tasks/taskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, Status.DONE))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskNotFoundException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_진행_상태를_DONE으로_변경을_요청할_때_서브_과제의_진행_상태가_모두_DONE이_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskNotAllDoneException subTaskNotAllDoneException = new SubTaskNotAllDoneException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(subTaskNotAllDoneException);

        // when then
        mockMvc.perform(patch("/tasks/taskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, Status.DONE))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskNotAllDoneException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_진행_상태를_TODO로_변경을_요청할_때_서브_과제의_진행_상태가_모두_TODO가_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskNotAllTodoException subTaskNotAllTodoException = new SubTaskNotAllTodoException();
        given(taskService.changeTaskStatus(any(), any()))
                .willThrow(subTaskNotAllTodoException);

        // when then
        mockMvc.perform(patch("/tasks/taskName/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskStatusModifyRequest(DAILY_CHECKLIST_ID, Status.DONE))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskNotAllTodoException.getMessage()));

        verify(taskService).changeTaskStatus(any(), any());
    }

}
