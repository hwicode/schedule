package hwicode.schedule.dailyschedule.checklist.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.application.TaskCheckerService;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.StatusNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskCheckerNameDuplicationException;
import hwicode.schedule.dailyschedule.checklist.exception.dailychecklist.TaskCheckerNotFoundException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotAllDoneException;
import hwicode.schedule.dailyschedule.checklist.exception.task.SubTaskCheckerNotAllTodoException;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.TaskCheckerController;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.delete.TaskCheckerDeleteRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveResponse;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyResponse;
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

@WebMvcTest(TaskCheckerController.class)
class TaskCheckerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskCheckerService taskCheckerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskCheckerSaveRequest taskCheckerSaveRequest = createTaskSaveRequest(DAILY_CHECKLIST_ID, NEW_TASK_NAME);
        TaskCheckerSaveResponse taskCheckerSaveResponse = createTaskSaveResponse(TASK_ID, NEW_TASK_NAME);

        given(taskCheckerService.saveTask(any()))
                .willReturn(TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/taskCheckers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskCheckerSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskCheckerSaveResponse)
                ));

        verify(taskCheckerService).saveTask(any());
    }

    @Test
    void 과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        TaskCheckerDeleteRequest taskCheckerDeleteRequest = createTaskDeleteRequest(DAILY_CHECKLIST_ID);

        // when then
        mockMvc.perform(delete("/dailyschedule/checklist/taskCheckers/taskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCheckerDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(taskCheckerService).deleteTask(any(), any());
    }

    @Test
    void 과제의_진행_상태_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskStatusModifyRequest taskStatusModifyRequest = createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE);
        TaskStatusModifyResponse taskStatusModifyResponse = createTaskStatusModifyResponse(TASK_NAME, TaskStatus.DONE);

        given(taskCheckerService.changeTaskStatus(any(), any()))
                .willReturn(TaskStatus.DONE);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/taskCheckers/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskStatusModifyResponse)
                ));

        verify(taskCheckerService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_어려움_점수의_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskDifficultyModifyRequest taskDifficultyModifyRequest = createTaskDifficultyModifyRequest(DAILY_CHECKLIST_ID, Difficulty.HARD);
        TaskDifficultyModifyResponse taskDifficultyModifyResponse = createTaskDifficultyModifyResponse(TASK_NAME, Difficulty.HARD);

        given(taskCheckerService.changeTaskDifficulty(any(), any()))
                .willReturn(Difficulty.HARD);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/taskCheckers/taskName/difficulty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDifficultyModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskDifficultyModifyResponse)
                ));

        verify(taskCheckerService).changeTaskDifficulty(any(), any());
    }

    @Test
    void 과제_생성을_요청할_때_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        TaskCheckerNameDuplicationException taskCheckerNameDuplicationException = new TaskCheckerNameDuplicationException();
        given(taskCheckerService.saveTask(any()))
                .willThrow(taskCheckerNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/checklist/taskCheckers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskSaveRequest(DAILY_CHECKLIST_ID, NEW_TASK_NAME)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(taskCheckerNameDuplicationException.getMessage()));

        verify(taskCheckerService).saveTask(any());
    }

    @Test
    void 과제의_진행_상태_변경을_요청할_때_진행_상태가_존재하지_않는다면_에러가_발생한다() throws Exception {
        // given
        StatusNotFoundException statusNotFoundException = new StatusNotFoundException();
        given(taskCheckerService.changeTaskStatus(any(), any()))
                .willThrow(statusNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/taskCheckers/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(statusNotFoundException.getMessage()));

        verify(taskCheckerService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제를_찾을_때_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskCheckerNotFoundException taskCheckerNotFoundException = new TaskCheckerNotFoundException();
        given(taskCheckerService.changeTaskStatus(any(), any()))
                .willThrow(taskCheckerNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/taskCheckers/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskCheckerNotFoundException.getMessage()));

        verify(taskCheckerService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_진행_상태를_DONE으로_변경을_요청할_때_서브_과제의_진행_상태가_모두_DONE이_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotAllDoneException subTaskCheckerNotAllDoneException = new SubTaskCheckerNotAllDoneException();
        given(taskCheckerService.changeTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotAllDoneException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/taskCheckers/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotAllDoneException.getMessage()));

        verify(taskCheckerService).changeTaskStatus(any(), any());
    }

    @Test
    void 과제의_진행_상태를_TODO로_변경을_요청할_때_서브_과제의_진행_상태가_모두_TODO가_아니면_에러가_발생한다() throws Exception {
        // given
        SubTaskCheckerNotAllTodoException subTaskCheckerNotAllTodoException = new SubTaskCheckerNotAllTodoException();
        given(taskCheckerService.changeTaskStatus(any(), any()))
                .willThrow(subTaskCheckerNotAllTodoException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/checklist/taskCheckers/taskName/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskStatusModifyRequest(DAILY_CHECKLIST_ID, TaskStatus.DONE)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(subTaskCheckerNotAllTodoException.getMessage()));

        verify(taskCheckerService).changeTaskStatus(any(), any());
    }

}
