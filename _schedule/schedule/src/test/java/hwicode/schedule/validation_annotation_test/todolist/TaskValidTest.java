package hwicode.schedule.validation_annotation_test.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.common.exception.GlobalErrorCode;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.todolist.application.TaskAggregateService;
import hwicode.schedule.dailyschedule.todolist.application.TaskSaveAndDeleteService;
import hwicode.schedule.dailyschedule.todolist.domain.Importance;
import hwicode.schedule.dailyschedule.todolist.domain.Priority;
import hwicode.schedule.dailyschedule.todolist.presentation.task.TaskController;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.DAILY_TO_DO_LIST_ID;
import static hwicode.schedule.dailyschedule.todolist.ToDoListDataHelper.TASK_NAME;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskValidTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskSaveAndDeleteService taskSaveAndDeleteService;

    @MockBean
    TaskAggregateService taskAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 모든_필드가_있으면_통과된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_TO_DO_LIST_ID, TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_TO_DO_LIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        perform.andExpect(status().isCreated());
    }

    private static Stream<Arguments> provideWrongDailyToDoListId() {
        return Stream.of(
                arguments(null, "must not be null"),
                arguments(-1L, "must be greater than 0"),
                arguments(0L, "must be greater than 0")
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongDailyToDoListId")
    void 투두리스트의_id에_잘못된_값이_들어오면_400에러가_발생한다(Long dailyToDoListId, String errorMessage) throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(dailyToDoListId, TASK_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_TO_DO_LIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        String field = "dailyToDoListId";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(errorMessage));
    }

    private static Stream<Arguments> provideWrongTaskName() {
        String blankMessage = "must not be blank";
        return Stream.of(
                arguments(null, blankMessage),
                arguments("", blankMessage),
                arguments(" ", blankMessage)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongTaskName")
    void 과제의_이름에_잘못된_값이_들어오면_400에러가_발생한다(String taskName, String errorMessage) throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_TO_DO_LIST_ID, taskName, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_TO_DO_LIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        String field = "taskName";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(errorMessage));
    }

    @Test
    void 과제의_어려움에_null이_들어오면_400에러가_발생한다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_TO_DO_LIST_ID, TASK_NAME, null, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_TO_DO_LIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        String field = "difficulty";
        String errorMessage = "must not be null";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(errorMessage));
    }

    private static Stream<Arguments> provideWrongDifficulty() {
        return Stream.of(
                arguments("normal"),
                arguments("NORMA"),
                arguments("Normal")
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongDifficulty")
    void 과제의_어려움에_형식에_맞지_않는_값이_들어오면_400에러가_발생한다(String wrongDifficulty) throws Exception {
        // given
        String wrongDifficultyBody = String.format(
                "{\"dailyToDoListId\":1," +
                "\"taskName\":\"taskName\"," +
                "\"difficulty\":\"%s\"," +
                "\"priority\":\"SECOND\"," +
                "\"importance\":\"SECOND\"}", wrongDifficulty);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_TO_DO_LIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongDifficultyBody));

        // then
        String message = "HTTP message body 중에 타입을 잘못 설정한게 있습니다.";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }

}
