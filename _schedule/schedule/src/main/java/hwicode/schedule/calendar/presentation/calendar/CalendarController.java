package hwicode.schedule.calendar.presentation.calendar;

import hwicode.schedule.calendar.application.CalendarService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.YearMonth;

@RequiredArgsConstructor
@Validated
@RestController
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/calendars")
    @ResponseStatus(value =HttpStatus.CREATED)
    public CalendarSaveResponse saveCalendar(@RequestBody @Valid CalendarSaveRequest calendarSaveRequest) {
        YearMonth yearMonth = calendarSaveRequest.getYearMonth();
        Long calendarId = calendarService.saveCalendar(yearMonth);
        return new CalendarSaveResponse(calendarId, yearMonth);
    }

    @PostMapping("/calendars/goals")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GoalSaveResponse saveGoal(@RequestBody @Valid GoalSaveRequest goalSaveRequest) {
        Long goalId = calendarService.saveGoal(
                goalSaveRequest.getGoalName(), goalSaveRequest.getYearMonths()
        );
        return new GoalSaveResponse(goalId, goalSaveRequest.getGoalName());
    }

    @PostMapping("/calendars/goals/{goalId}")
    @ResponseStatus(value = HttpStatus.OK)
    public GoalAddToCalendarsResponse addGoalToCalendars(@PathVariable @Positive Long goalId,
                                                         @RequestBody @Valid GoalAddToCalendarsRequest goalAddToCalendarsRequest) {
        Long addedGoalId = calendarService.addGoalToCalendars(
                goalId, goalAddToCalendarsRequest.getYearMonths()
        );
        return new GoalAddToCalendarsResponse(addedGoalId, goalAddToCalendarsRequest.getYearMonths());
    }

    @PatchMapping("/calendars/{calendarId}/goals/{goalId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public GoalNameModifyResponse changeGoalName(@PathVariable @Positive Long calendarId,
                                                 @PathVariable @Positive Long goalId,
                                                 @RequestBody @Valid GoalNameModifyRequest goalNameModifyRequest) {
        String changedGoalName = calendarService.changeGoalName(
                goalNameModifyRequest.getYearMonth(),
                goalNameModifyRequest.getGoalName(),
                goalNameModifyRequest.getNewGoalName()
        );

        return new GoalNameModifyResponse(calendarId, changedGoalName);
    }

    @PatchMapping("/calendars/{calendarId}/weeklyStudyDate")
    @ResponseStatus(value = HttpStatus.OK)
    public WeeklyStudyDateModifyResponse changeWeeklyStudyDate(@PathVariable @Positive Long calendarId,
                                                               @RequestBody @Valid WeeklyStudyDateModifyRequest weeklyStudyDateModifyRequest) {
        int weeklyStudyDate = calendarService.changeWeeklyStudyDate(
                weeklyStudyDateModifyRequest.getYearMonth(), weeklyStudyDateModifyRequest.getWeeklyStudyDate()
        );
        return new WeeklyStudyDateModifyResponse(calendarId, weeklyStudyDate);
    }

}
