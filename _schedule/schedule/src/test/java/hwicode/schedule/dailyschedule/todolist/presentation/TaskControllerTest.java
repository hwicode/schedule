package hwicode.schedule.dailyschedule.todolist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.application.TaskService;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.TaskController;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyResponse;
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
}
