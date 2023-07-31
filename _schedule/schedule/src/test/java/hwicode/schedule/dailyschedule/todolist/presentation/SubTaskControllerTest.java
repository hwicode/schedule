package hwicode.schedule.dailyschedule.todolist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.dailyschedule.checklist.exception.application.DailyChecklistNotFoundException;
import hwicode.schedule.dailyschedule.todolist.application.SubTaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.SubTaskController;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubTaskController.class)
class SubTaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SubTaskSaveAndDeleteService subTaskSaveAndDeleteService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 서브_과제_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskSaveRequest subTaskSaveRequest = new SubTaskSaveRequest(DAILY_TO_DO_LIST_ID, TASK_NAME, SUB_TASK_NAME);
        SubTaskSaveResponse subTaskSaveResponse = new SubTaskSaveResponse(SUB_TASK_ID, SUB_TASK_NAME);

        given(subTaskSaveAndDeleteService.save(any()))
                .willReturn(SUB_TASK_ID);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks",
                        DAILY_TO_DO_LIST_ID, TASK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subTaskSaveRequest)));
        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(subTaskSaveResponse)
                ));

        verify(subTaskSaveAndDeleteService).save(any());
    }

    @Test
    void 서브_과제_삭제을_요청하면_204_상태코드가_리턴된다() throws Exception {
        // given
        SubTaskDeleteRequest subTaskDeleteRequest = new SubTaskDeleteRequest(DAILY_TO_DO_LIST_ID, TASK_NAME, SUB_TASK_ID, SUB_TASK_NAME);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}",
                        DAILY_TO_DO_LIST_ID, TASK_ID, SUB_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subTaskDeleteRequest)));

        // then
        perform.andExpect(status().isNoContent());

        verify(subTaskSaveAndDeleteService).delete(any(), any());
    }

    @Test
    void 서브_과제를_저장할_때_실패하면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();

        given(subTaskSaveAndDeleteService.save(any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks",
                        DAILY_TO_DO_LIST_ID, TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskSaveRequest(DAILY_TO_DO_LIST_ID, TASK_NAME, SUB_TASK_NAME)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskSaveAndDeleteService).save(any());
    }

    @Test
    void 서브_과제를_삭제할_때_실패하면_에러가_발생한다() throws Exception {
        // given
        DailyChecklistNotFoundException dailyChecklistNotFoundException = new DailyChecklistNotFoundException();

        given(subTaskSaveAndDeleteService.delete(any(), any()))
                .willThrow(dailyChecklistNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks/{taskId}/subtasks/{subTaskId}",
                        DAILY_TO_DO_LIST_ID, TASK_ID, SUB_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SubTaskDeleteRequest(DAILY_TO_DO_LIST_ID, TASK_NAME, SUB_TASK_ID, SUB_TASK_NAME)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(dailyChecklistNotFoundException.getMessage()));

        verify(subTaskSaveAndDeleteService).delete(any(), any());
    }

}
