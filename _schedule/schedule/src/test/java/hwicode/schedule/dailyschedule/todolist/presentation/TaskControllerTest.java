package hwicode.schedule.dailyschedule.todolist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.todolist.application.DailyToDoListService;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.application.TaskService;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.exception.application.DailyToDoListNotExistException;
import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.exception.domain.dailytodolist.TaskNameDuplicationException;
import hwicode.schedule.dailyschedule.todolist.exception.domain.dailytodolist.TaskNotFoundException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.SubTaskNameChangeRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.SubTaskNameChangeResponse;
import hwicode.schedule.dailyschedule.todolist.presentation.task.TaskController;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyResponse;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.name_modify.TaskNameModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.name_modify.TaskNameModifyResponse;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @MockBean
    TaskService taskService;

    @MockBean
    DailyToDoListService dailyToDoListService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = createTaskSaveRequest(DAILY_TO_DO_LIST_ID, TASK_NAME);
        TaskSaveResponse taskSaveResponse = createTaskSaveResponse(TASK_ID, TASK_NAME);

        given(taskSaveAndDeleteService.save(any()))
                .willReturn(TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/todolist/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskSaveResponse)
                ));

        verify(taskSaveAndDeleteService).save(any());
    }

    @Test
    void 과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        TaskDeleteRequest taskDeleteRequest = createTaskDeleteRequest(DAILY_TO_DO_LIST_ID);

        // when then
        mockMvc.perform(delete("/dailyschedule/todolist/tasks/taskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDeleteRequest)))
                .andExpect(status().isNoContent());

        verify(taskSaveAndDeleteService).delete(any(), any());
    }

    @Test
    void 과제의_정보_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskInformationModifyRequest taskInformationModifyRequest = createTaskInformationModifyRequest(Priority.THIRD, Importance.THIRD);
        TaskInformationModifyResponse taskInformationModifyResponse = createTaskInformationModifyResponse(TASK_ID, Priority.THIRD, Importance.THIRD);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/todolist/tasks/%s/information", TASK_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskInformationModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskInformationModifyResponse)
                ));

        verify(taskService).changeTaskInformation(any(), any());
    }

    @Test
    void 과제의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskNameModifyRequest taskNameModifyRequest = createTaskNameModifyRequest(DAILY_TO_DO_LIST_ID, NEW_TASK_NAME);
        TaskNameModifyResponse taskNameModifyResponse = createTaskNameModifyResponse(DAILY_TO_DO_LIST_ID, NEW_TASK_NAME);

        given(dailyToDoListService.changeTaskName(any(), any()))
                .willReturn(NEW_TASK_NAME);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/todolist/tasks/taskName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskNameModifyResponse)
                ));

        verify(dailyToDoListService).changeTaskName(any(), any());
    }

    @Test
    void 과제의_이름변경을_요청할_때_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        TaskNameDuplicationException taskNameDuplicationException = new TaskNameDuplicationException();
        given(dailyToDoListService.changeTaskName(any(), any()))
                .willThrow(taskNameDuplicationException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/todolist/tasks/taskName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskNameModifyRequest(DAILY_TO_DO_LIST_ID, NEW_TASK_NAME)
                )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(taskNameDuplicationException.getMessage()));

        verify(dailyToDoListService).changeTaskName(any(), any());
    }

    @Test
    void 과제를_찾을_때_과제가_없으면_에러가_발생한다() throws Exception {
        // given
        TaskNotFoundException taskNotFoundException = new TaskNotFoundException();
        given(dailyToDoListService.changeTaskName(any(), any()))
                .willThrow(taskNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/todolist/tasks/taskName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskNameModifyRequest(DAILY_TO_DO_LIST_ID, NEW_TASK_NAME)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskNotFoundException.getMessage()));

        verify(dailyToDoListService).changeTaskName(any(), any());
    }

    @Test
    void 투두리스트를_찾을_때_투두리스트가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        DailyToDoListNotExistException dailyToDoListNotExistException = new DailyToDoListNotExistException();
        given(dailyToDoListService.changeTaskName(any(), any()))
                .willThrow(dailyToDoListNotExistException);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/todolist/tasks/taskName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskNameModifyRequest(DAILY_TO_DO_LIST_ID, NEW_TASK_NAME)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyToDoListNotExistException.getMessage()));

        verify(dailyToDoListService).changeTaskName(any(), any());
    }

    @Test
    void 과제를_찾을_때_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskNotExistException taskNotExistException = new TaskNotExistException();
        given(taskService.changeTaskInformation(any(), any()))
                .willThrow(taskNotExistException);

        // when
        ResultActions perform = mockMvc.perform(
                patch(String.format("/dailyschedule/todolist/tasks/%s/information", TASK_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        createTaskInformationModifyRequest(Priority.SECOND, Importance.SECOND)
                )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskNotExistException.getMessage()));

        verify(taskService).changeTaskInformation(any(), any());
    }

    @Test
    void 서브과제의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskNameChangeRequest subTaskNameChangeRequest = createSubTaskNameChangeRequest(TASK_ID, NEW_SUB_TASK_NAME);
        SubTaskNameChangeResponse subTaskNameChangeResponse = createSubTaskNameChangeResponse(TASK_ID, NEW_SUB_TASK_NAME);

        given(taskService.changeSubTaskName(any(), any()))
                .willReturn(NEW_SUB_TASK_NAME);

        // when
        ResultActions perform = mockMvc.perform(patch("/dailyschedule/todolist/subtasks/subTaskName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskNameChangeRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskNameChangeResponse)
                ));

        verify(taskService).changeSubTaskName(any(), any());
    }
}
