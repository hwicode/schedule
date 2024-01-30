package hwicode.schedule.calendar.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.calendar.application.calendar.CalendarService;
import hwicode.schedule.calendar.exception.application.YearMonthNullException;
import hwicode.schedule.calendar.exception.application.YearMonthsSizeNotValidException;
import hwicode.schedule.calendar.exception.domain.calendar.CalendarGoalDuplicateException;
import hwicode.schedule.calendar.exception.domain.calendar.WeeklyDateNotValidException;
import hwicode.schedule.calendar.exception.infra.GoalNotFoundException;
import hwicode.schedule.calendar.presentation.calendar.CalendarController;
import hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal.GoalAddToCalendarsRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal.GoalAddToCalendarsResponse;
import hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify.GoalNameModifyRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify.GoalNameModifyResponse;
import hwicode.schedule.calendar.presentation.calendar.dto.save.CalendarSaveRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.save.CalendarSaveResponse;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveResponse;
import hwicode.schedule.calendar.presentation.calendar.dto.weekly_study_date_modify.WeeklyStudyDateModifyRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.weekly_study_date_modify.WeeklyStudyDateModifyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarController.class)
class CalendarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalendarService calendarService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 캘린더_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        CalendarSaveRequest calendarSaveRequest = new CalendarSaveRequest(YEAR_MONTH);
        CalendarSaveResponse calendarSaveResponse = new CalendarSaveResponse(CALENDAR_ID, YEAR_MONTH);

        given(calendarService.saveCalendar(any()))
                .willReturn(CALENDAR_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/calendars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(calendarSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(calendarSaveResponse)
                ));

        verify(calendarService).saveCalendar(any());
    }

    @Test
    void 목표_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        Set<YearMonth> yearMonths = Set.of(YEAR_MONTH, YEAR_MONTH.plusMonths(1));
        GoalSaveRequest goalSaveRequest = new GoalSaveRequest(GOAL_NAME, yearMonths);
        GoalSaveResponse goalSaveResponse = new GoalSaveResponse(GOAL_ID, GOAL_NAME);

        given(calendarService.saveGoal(any()))
                .willReturn(GOAL_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/calendars/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalSaveRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(goalSaveResponse)
                ));

        verify(calendarService).saveGoal(any());
    }

    @Test
    void 여러_개의_캘린더에_목표_추가를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        Set<YearMonth> yearMonths = Set.of(YEAR_MONTH, YEAR_MONTH.plusMonths(1));
        GoalAddToCalendarsRequest goalAddToCalendarsRequest = new GoalAddToCalendarsRequest(yearMonths);
        GoalAddToCalendarsResponse goalAddToCalendarsResponse = new GoalAddToCalendarsResponse(GOAL_ID, goalAddToCalendarsRequest.getYearMonths());

        given(calendarService.addGoalToCalendars(any()))
                .willReturn(GOAL_ID);

        // when
        ResultActions perform = mockMvc.perform(post("/calendars/goals/{goalId}", GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalAddToCalendarsRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(goalAddToCalendarsResponse)
                ));

        verify(calendarService).addGoalToCalendars(any());
    }

    @Test
    void 목표의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        GoalNameModifyRequest goalNameModifyRequest = new GoalNameModifyRequest(YEAR_MONTH, GOAL_NAME, NEW_GOAL_NAME);
        GoalNameModifyResponse goalNameModifyResponse = new GoalNameModifyResponse(CALENDAR_ID, NEW_GOAL_NAME);

        given(calendarService.changeGoalName(any()))
                .willReturn(NEW_GOAL_NAME);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/goals/{goalId}/name", CALENDAR_ID, GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(goalNameModifyResponse)
                ));

        verify(calendarService).changeGoalName(any());
    }

    @Test
    void 캘린더에_일주일간_공부일_수정을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int weeklyStudyDate = 6;
        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, weeklyStudyDate);
        WeeklyStudyDateModifyResponse weeklyStudyDateModifyResponse = new WeeklyStudyDateModifyResponse(CALENDAR_ID, weeklyStudyDate);

        given(calendarService.changeWeeklyStudyDate(any()))
                .willReturn(weeklyStudyDate);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/weeklyStudyDate", CALENDAR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(weeklyStudyDateModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(weeklyStudyDateModifyResponse)
                ));

        verify(calendarService).changeWeeklyStudyDate(any());
    }

    @Test
    void 목표의_이름_변경을_요청할_때_캘린더에_목표가_존재하지_않는다면_에러가_발생한다() throws Exception {
        // given
        GoalNotFoundException goalNotFoundException = new GoalNotFoundException();
        given(calendarService.changeGoalName(any()))
                .willThrow(goalNotFoundException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/calendars/{calendarId}/goals/{goalId}/name",
                        CALENDAR_ID, GOAL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new GoalNameModifyRequest(YEAR_MONTH, GOAL_NAME, NEW_GOAL_NAME)
                        )));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(goalNotFoundException.getMessage()));

        verify(calendarService).changeGoalName(any());
    }

    @Test
    void 캘린더에_목표_추가를_요청할_때_목표의_이름이_중복되면_에러가_발생한다() throws Exception {
        // given
        CalendarGoalDuplicateException calendarGoalDuplicateException = new CalendarGoalDuplicateException();
        given(calendarService.saveGoal(any()))
                .willThrow(calendarGoalDuplicateException);

        // when
        ResultActions perform = mockMvc.perform(
                post("/calendars/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new GoalSaveRequest(GOAL_NAME, Set.of(YEAR_MONTH))
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(calendarGoalDuplicateException.getMessage()));
    }

    @Test
    void 캘린더의_일주일간_공부일_수정을_요청할_때_0에서_7을_벗어나면_에러가_발생한다() throws Exception {
        // given
        WeeklyDateNotValidException weeklyDateNotValidException = new WeeklyDateNotValidException();
        given(calendarService.changeWeeklyStudyDate(any()))
                .willThrow(weeklyDateNotValidException);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/calendars/{calendarId}/weeklyStudyDate",
                        CALENDAR_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new WeeklyStudyDateModifyRequest(YEAR_MONTH, 5)
                        )));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(weeklyDateNotValidException.getMessage()));
    }

    @Test
    void 목표_생성을_할_때_목표_기간이_24개월보다_크다면_에러가_발생한다() throws Exception {
        // given
        YearMonthsSizeNotValidException yearMonthsSizeNotValidException = new YearMonthsSizeNotValidException();

        Set<YearMonth> overSizeYearMonths = Set.of(YEAR_MONTH);

        GoalSaveRequest goalSaveRequest = new GoalSaveRequest(GOAL_NAME, overSizeYearMonths);
        given(calendarService.saveGoal(any()))
                .willThrow(yearMonthsSizeNotValidException);

        // when
        ResultActions perform = mockMvc.perform(post("/calendars/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalSaveRequest)));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(yearMonthsSizeNotValidException.getMessage()));
    }

    @Test
    void 목표_생성을_할_때_목표_기간에_null이_존재하면_에러가_발생한다() throws Exception {
        // given
        YearMonthNullException yearMonthNullException = new YearMonthNullException();

        Set<YearMonth> yearMonths = new HashSet<>();
        yearMonths.add(null);

        GoalSaveRequest goalSaveRequest = new GoalSaveRequest(GOAL_NAME, yearMonths);
        given(calendarService.saveGoal(any()))
                .willThrow(yearMonthNullException);

        // when
        ResultActions perform = mockMvc.perform(post("/calendars/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalSaveRequest)));

        // then
        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(yearMonthNullException.getMessage()));
    }

}
