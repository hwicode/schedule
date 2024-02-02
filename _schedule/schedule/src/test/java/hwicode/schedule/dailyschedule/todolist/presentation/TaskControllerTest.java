package hwicode.schedule.dailyschedule.todolist.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import hwicode.schedule.dailyschedule.todolist.application.TaskAggregateService;
import hwicode.schedule.dailyschedule.todolist.exception.application.TaskNotExistException;
import hwicode.schedule.dailyschedule.todolist.presentation.task.TaskController;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.information_modify.TaskInformationModifyResponse;
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
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TaskAggregateService taskAggregateService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 과제의_정보_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        TaskInformationModifyRequest taskInformationModifyRequest = new TaskInformationModifyRequest(Priority.THIRD, Importance.THIRD);
        TaskInformationModifyResponse taskInformationModifyResponse = new TaskInformationModifyResponse(TASK_ID, Priority.THIRD, Importance.THIRD);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/information", TASK_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskInformationModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(taskInformationModifyResponse)
                ));

        verify(taskAggregateService).changeTaskInformation(any());
    }

    @Test
    void 과제를_찾을_때_과제가_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        TaskNotExistException taskNotExistException = new TaskNotExistException();
        given(taskAggregateService.changeTaskInformation(any()))
                .willThrow(taskNotExistException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/tasks/{taskId}/information", TASK_ID)
                        .header("Authorization", BEARER + "accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TaskInformationModifyRequest(Priority.SECOND, Importance.SECOND)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(taskNotExistException.getMessage()));

        verify(taskAggregateService).changeTaskInformation(any());
    }

}
