package hwicode.schedule.validation_annotation_test.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.calendar.application.CalendarAggregateService;
import hwicode.schedule.calendar.presentation.calendar.CalendarController;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.weekly_study_date_modify.WeeklyStudyDateModifyRequest;
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

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    void 일주일간_공부일이_0이면_통과된다() throws Exception {
        // given
        int weeklyStudyDate = 0;
        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, weeklyStudyDate);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/weeklyStudyDate", CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weeklyStudyDateModifyRequest)));

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    void 일주일간_공부일이_0과_7사이면_통과된다() throws Exception {
        // given
        int weeklyStudyDate = 4;
        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, weeklyStudyDate);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/weeklyStudyDate", CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weeklyStudyDateModifyRequest)));

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    void 일주일간_공부일이_오버되면_에러가_발생한다() throws Exception {
        // given
        int overDate = 8;
        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, overDate);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/weeklyStudyDate", CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weeklyStudyDateModifyRequest)));

        // then
        String field = "weeklyStudyDate";
        String message = "must be less than or equal to 7";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(message));
    }

    @Test
    void 일주일간_공부일이_0보다_작으면_에러가_발생한다() throws Exception {
        // given
        int wrongDate = -1;
        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, wrongDate);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/weeklyStudyDate", CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weeklyStudyDateModifyRequest)));

        // then
        String field = "weeklyStudyDate";
        String message = "must be greater than or equal to 0";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(message));
    }

    @Test
    void 일주일간_공부일이_존재하지_않으면_에러가_발생한다() throws Exception {
        // given
        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, null);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/weeklyStudyDate", CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weeklyStudyDateModifyRequest)));

        // then
        String field = "weeklyStudyDate";
        String message = "must not be null";
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(GlobalErrorCode.INVALID_PARAMETER.getMessage()))
                .andExpect(jsonPath("$.errors[0].field").value(field))
                .andExpect(jsonPath("$.errors[0].message").value(message));
    }

}
