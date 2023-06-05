package hwicode.schedule.dto_valid_test.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.calendar.application.CalendarAggregateService;
import hwicode.schedule.calendar.presentation.calendar.CalendarController;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveRequest;
import hwicode.schedule.common.exception.GlobalErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.validation.constraints.Size;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

import static hwicode.schedule.calendar.CalendarDataHelper.GOAL_NAME;
import static hwicode.schedule.calendar.CalendarDataHelper.YEAR_MONTH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarController.class)
class CalendarValidTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalendarAggregateService calendarAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 목표_기간이_비어있으면_에러가_발생한다() throws Exception {
        // given
        Set<YearMonth> emptyYearMonths = new HashSet<>();
        GoalSaveRequest goalSaveRequest = new GoalSaveRequest(GOAL_NAME, emptyYearMonths);

        // when
        ResultActions perform = mockMvc.perform(post("/calendars/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalSaveRequest)));

        // then
        String field = "yearMonths";
        String message = "must not be empty";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(message));
    }


    @Test
    void 목표_기간이_24개월_이상이면_에러가_발생한다() throws Exception {
        // given
        Set<YearMonth> overSizeYearMonths = Set.of(
                YEAR_MONTH, YEAR_MONTH.plusMonths(1), YEAR_MONTH.plusMonths(2),
                YEAR_MONTH.plusMonths(3), YEAR_MONTH.plusMonths(4), YEAR_MONTH.plusMonths(5),
                YEAR_MONTH.plusMonths(6), YEAR_MONTH.plusMonths(7), YEAR_MONTH.plusMonths(8),
                YEAR_MONTH.plusMonths(9),YEAR_MONTH.plusMonths(10),YEAR_MONTH.plusMonths(11),
                YEAR_MONTH.plusMonths(12),YEAR_MONTH.plusMonths(13),YEAR_MONTH.plusMonths(14),
                YEAR_MONTH.plusMonths(15),YEAR_MONTH.plusMonths(16),YEAR_MONTH.plusMonths(17),
                YEAR_MONTH.plusMonths(18),YEAR_MONTH.plusMonths(19),YEAR_MONTH.plusMonths(20),
                YEAR_MONTH.plusMonths(21),YEAR_MONTH.plusMonths(22),YEAR_MONTH.plusMonths(23),
                YEAR_MONTH.plusMonths(24)
        );

        GoalSaveRequest goalSaveRequest = new GoalSaveRequest(GOAL_NAME, overSizeYearMonths);

        // when
        ResultActions perform = mockMvc.perform(post("/calendars/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalSaveRequest)));

        // then
        String field = "yearMonths";
        String message = GoalSaveRequest.class.getDeclaredField(field).getAnnotation(Size.class).message();
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(message));
    }

}
