package hwicode.schedule.validation_annotation_test.checklist;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.auth.infra.token.DecodedToken;
import hwicode.schedule.auth.infra.token.TokenProvider;
import hwicode.schedule.common.exception.GlobalErrorCode;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.TaskCheckerSubService;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.TaskCheckerController;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.Importance;
import hwicode.schedule.dailyschedule.shared_domain.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static hwicode.schedule.auth.AuthDataHelper.BEARER;
import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.DAILY_CHECKLIST_ID;
import static hwicode.schedule.dailyschedule.checklist.ChecklistDataHelper.TASK_CHECKER_NAME;
import static hwicode.schedule.validation_annotation_test.ValidationDataHelper.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskCheckerController.class)
class TaskCheckerValidTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TaskCheckerSubService taskCheckerSubService;

    @MockBean
    TokenProvider tokenProvider;

    @BeforeEach
    void decodeToken() {
        given(tokenProvider.decodeToken(any()))
                .willReturn(new DecodedToken(1L));
    }

    @Test
    void 모든_필드가_있으면_통과된다() throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .header("Authorization", BEARER + "accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        perform.andExpect(status().isCreated());
    }

    @Test
    void 투두리스트의_id에_형식에_맞지_않는_값이_들어오면_400에러가_발생한다() throws Exception {
        // given
        String wrongTableId = "ww";
        String wrongTimeTableIdBody = String.format(
                "{\"dailyToDoListId\":%s," +
                "\"taskName\":\"taskName\"," +
                "\"difficulty\":\"NORMAL\"," +
                "\"priority\":\"SECOND\"," +
                "\"importance\":\"SECOND\"}", wrongTableId);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .header("Authorization", BEARER + "accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongTimeTableIdBody));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BODY_TYPE_ERROR_MESSAGE));
    }

    private static Stream<Arguments> provideWrongDailyChecklistId() {
        return Stream.of(
                arguments(null, NOT_NULL_ERROR_MESSAGE),
                arguments(-1L, POSITIVE_ERROR_MESSAGE),
                arguments(0L, POSITIVE_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongDailyChecklistId")
    void 투두리스트의_id에_잘못된_값이_들어오면_400에러가_발생한다(Long dailyChecklistId, String errorMessage) throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(dailyChecklistId, TASK_CHECKER_NAME, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .header("Authorization", BEARER + "accessToken")
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
        return Stream.of(
                arguments(null, NOT_BLANK_ERROR_MESSAGE),
                arguments("", NOT_BLANK_ERROR_MESSAGE),
                arguments(" ", NOT_BLANK_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongTaskName")
    void 과제의_이름에_잘못된_값이_들어오면_400에러가_발생한다(String taskName, String errorMessage) throws Exception {
        // given
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_CHECKLIST_ID, taskName, Difficulty.NORMAL, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .header("Authorization", BEARER + "accessToken")
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
        TaskSaveRequest taskSaveRequest = new TaskSaveRequest(DAILY_CHECKLIST_ID, TASK_CHECKER_NAME, null, Priority.SECOND, Importance.SECOND);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .header("Authorization", BEARER + "accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequest)));

        // then
        String field = "difficulty";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(NOT_NULL_ERROR_MESSAGE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"normal", "NORMA", "Normal"})
    void 과제의_어려움에_형식에_맞지_않는_값이_들어오면_400에러가_발생한다(String wrongDifficulty) throws Exception {
        // given
        String wrongDifficultyBody = String.format(
                "{\"dailyToDoListId\":1," +
                "\"taskName\":\"taskName\"," +
                "\"difficulty\":\"%s\"," +
                "\"priority\":\"SECOND\"," +
                "\"importance\":\"SECOND\"}", wrongDifficulty);

        // when
        ResultActions perform = mockMvc.perform(post("/dailyschedule/daily-todo-lists/{dailyToDoListId}/tasks", DAILY_CHECKLIST_ID)
                .header("Authorization", BEARER + "accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongDifficultyBody));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BODY_TYPE_ERROR_MESSAGE));
    }

}
