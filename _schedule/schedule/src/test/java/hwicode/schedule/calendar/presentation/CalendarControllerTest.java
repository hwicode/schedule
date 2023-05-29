package hwicode.schedule.calendar.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import hwicode.schedule.calendar.application.CalendarAggregateService;
import hwicode.schedule.calendar.presentation.calendar.CalendarController;
import hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal.GoalAddToCalendarsRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal.GoalAddToCalendarsResponse;
import hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify.GoalNameModifyRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.goal_name_modify.GoalNameModifyResponse;
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
import java.util.Set;

import static hwicode.schedule.calendar.CalendarDataHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarController.class)
class CalendarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalendarAggregateService calendarAggregateService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 목표_생성을_요청하면_201_상태코드가_리턴된다() throws Exception {
        // given
        Set<YearMonth> yearMonths = Set.of(YEAR_MONTH, YEAR_MONTH.plusMonths(1));
        GoalSaveRequest goalSaveRequest = new GoalSaveRequest(GOAL_NAME, yearMonths);
        GoalSaveResponse goalSaveResponse = new GoalSaveResponse(GOAL_ID, GOAL_NAME);

        given(calendarAggregateService.saveGoal(any(), any()))
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

        verify(calendarAggregateService).saveGoal(any(), any());
    }

    @Test
    void 여러_개의_캘린더에_목표_추가를_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        Set<YearMonth> yearMonths = Set.of(YEAR_MONTH, YEAR_MONTH.plusMonths(1));
        GoalAddToCalendarsRequest goalAddToCalendarsRequest = new GoalAddToCalendarsRequest(yearMonths);
        GoalAddToCalendarsResponse goalAddToCalendarsResponse = new GoalAddToCalendarsResponse(GOAL_ID, goalAddToCalendarsRequest.getYearMonths());

        given(calendarAggregateService.addGoalToCalendars(any(), any()))
                .willReturn(GOAL_ID);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/goals/{goalId}", GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalAddToCalendarsRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(goalAddToCalendarsResponse)
                ));

        verify(calendarAggregateService).addGoalToCalendars(any(), any());
    }

    @Test
    void 목표의_이름_변경을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        GoalNameModifyRequest goalNameModifyRequest = new GoalNameModifyRequest(YEAR_MONTH, GOAL_NAME, NEW_GOAL_NAME);
        GoalNameModifyResponse goalNameModifyResponse = new GoalNameModifyResponse(CALENDAR_ID, NEW_GOAL_NAME);

        given(calendarAggregateService.changeGoalName(any(), any(), any()))
                .willReturn(NEW_GOAL_NAME);

        // when
        ResultActions perform = mockMvc.perform(patch("/calendars/{calendarId}/goals/{goalId}", CALENDAR_ID, GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalNameModifyRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        objectMapper.writeValueAsString(goalNameModifyResponse)
                ));

        verify(calendarAggregateService).changeGoalName(any(), any(), any());
    }

    @Test
    void 캘린더에_일주일간_공부일_수정을_요청하면_200_상태코드가_리턴된다() throws Exception {
        // given
        int weeklyStudyDate = 6;
        WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest = new WeeklyStudyDateModifyRequest(YEAR_MONTH, weeklyStudyDate);
        WeeklyStudyDateModifyResponse weeklyStudyDateModifyResponse = new WeeklyStudyDateModifyResponse(CALENDAR_ID, weeklyStudyDate);

        given(calendarAggregateService.changeWeeklyStudyDate(any(), anyInt()))
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

        verify(calendarAggregateService).changeWeeklyStudyDate(any(), anyInt());
    }

}
