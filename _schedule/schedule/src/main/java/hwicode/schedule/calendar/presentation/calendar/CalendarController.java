package hwicode.schedule.calendar.presentation.calendar;

import hwicode.schedule.calendar.application.calendar.CalendarService;
import hwicode.schedule.calendar.application.calendar.dto.*;
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
import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RestController
public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/calendars")
    @ResponseStatus(value =HttpStatus.CREATED)
    public CalendarSaveResponse saveCalendar(@LoginUser LoginInfo loginInfo,
                                             @RequestBody @Valid CalendarSaveRequest request) {
        CalendarSaveCommand command = new CalendarSaveCommand(loginInfo.getUserId(), request.getYearMonth());
        Long calendarId = calendarService.saveCalendar(command);
        return new CalendarSaveResponse(calendarId, command.getYearMonth());
    }

    @PostMapping("/calendars/goals")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GoalSaveResponse saveGoal(@LoginUser LoginInfo loginInfo,
                                     @RequestBody @Valid GoalSaveRequest request) {
        GoalSaveCommand command = new GoalSaveCommand(
                loginInfo.getUserId(), request.getGoalName(), request.getYearMonths()
        );
        Long goalId = calendarService.saveGoal(command);
        return new GoalSaveResponse(goalId, command.getName());
    }

    @PostMapping("/calendars/goals/{goalId}")
    @ResponseStatus(value = HttpStatus.OK)
    public GoalAddToCalendarsResponse addGoalToCalendars(@LoginUser LoginInfo loginInfo,
                                                         @PathVariable @Positive Long goalId,
                                                         @RequestBody @Valid GoalAddToCalendarsRequest request) {
        GoalAddToCalendersCommand command = new GoalAddToCalendersCommand(
                loginInfo.getUserId(), goalId, request.getYearMonths()
        );
        Long addedGoalId = calendarService.addGoalToCalendars(command);
        return new GoalAddToCalendarsResponse(addedGoalId, command.getYearMonths());
    }

    @PatchMapping("/calendars/{calendarId}/goals/{goalId}/name")
    @ResponseStatus(value = HttpStatus.OK)
    public GoalNameModifyResponse changeGoalName(@LoginUser LoginInfo loginInfo,
                                                 @PathVariable @Positive Long calendarId,
                                                 @PathVariable @Positive Long goalId,
                                                 @RequestBody @Valid GoalNameModifyRequest request) {
        GoalModifyNameCommand command = new GoalModifyNameCommand(
                loginInfo.getUserId(), request.getYearMonth(), request.getGoalName(), request.getNewGoalName()
        );
        String changedGoalName = calendarService.changeGoalName(command);
        return new GoalNameModifyResponse(calendarId, changedGoalName);
    }

    @PatchMapping("/calendars/{calendarId}/weeklyStudyDate")
    @ResponseStatus(value = HttpStatus.OK)
    public WeeklyStudyDateModifyResponse changeWeeklyStudyDate(@LoginUser LoginInfo loginInfo,
                                                               @PathVariable @Positive Long calendarId,
                                                               @RequestBody @Valid WeeklyStudyDateModifyRequest request) {
        CalendarModifyStudyDateCommand command = new CalendarModifyStudyDateCommand(
                loginInfo.getUserId(), request.getYearMonth(), request.getWeeklyStudyDate()
        );
        int weeklyStudyDate = calendarService.changeWeeklyStudyDate(command);
        return new WeeklyStudyDateModifyResponse(calendarId, weeklyStudyDate);
    }

}
