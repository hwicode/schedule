package hwicode.schedule.validation_annotation_test.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.common.exception.GlobalErrorCode;
import hwicode.schedule.dailyschedule.todolist.application.DailyToDoListAggregateService;
import hwicode.schedule.dailyschedule.todolist.domain.Emoji;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.DailyToDoListController;
import hwicode.schedule.dailyschedule.todolist.presentation.dailytodolist.dto.DailyToDoListInformationChangeRequest;
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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyToDoListController.class)
class DailyToDoListValidTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DailyToDoListAggregateService dailyToDoListAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    private static Stream<Arguments> provideReview() {
        return Stream.of(
                arguments("좋은데!"), arguments(""), arguments("  ")
        );
    }

    @ParameterizedTest
    @MethodSource("provideReview")
    void 리뷰와_이모지가_존재하면_통과된다(String review) throws Exception {
        // given
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = new DailyToDoListInformationChangeRequest(review, Emoji.GOOD);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/information", DAILY_TO_DO_LIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dailyToDoListInformationChangeRequest)));

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    void 리뷰와_없으면_에러가_발생한다() throws Exception {
        // given
        DailyToDoListInformationChangeRequest dailyToDoListInformationChangeRequest = new DailyToDoListInformationChangeRequest(null, Emoji.GOOD);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/dailyschedule/daily-todo-lists/{dailyToDoListId}/information", DAILY_TO_DO_LIST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dailyToDoListInformationChangeRequest)));

        // then
        String field = "review";
        String message = "must not be null";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(message));
    }

}
