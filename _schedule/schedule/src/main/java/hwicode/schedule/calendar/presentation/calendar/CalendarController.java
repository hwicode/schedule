package hwicode.schedule.calendar.presentation.calendar;

import hwicode.schedule.calendar.application.CalendarAggregateService;
import hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal.GoalAddToCalendarsRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.calendar_goal.GoalAddToCalendarsResponse;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@RestController
public class CalendarController {

    private final CalendarAggregateService calendarAggregateService;

    @PostMapping("/calendars/goals")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GoalSaveResponse saveGoal(@RequestBody @Valid GoalSaveRequest goalSaveRequest) {
        Long goalId = calendarAggregateService.saveGoal(
                goalSaveRequest.getGoalName(), goalSaveRequest.getYearMonths()
        );
        return new GoalSaveResponse(goalId, goalSaveRequest.getGoalName());
    }

    @PatchMapping("/calendars/goals/{goalId}")
    @ResponseStatus(value = HttpStatus.OK)
    public GoalAddToCalendarsResponse addGoalToCalendars(@PathVariable @Positive Long goalId,
                                                         @RequestBody @Valid GoalAddToCalendarsRequest goalAddToCalendarsRequest) {
        Long addedGoalId = calendarAggregateService.addGoalToCalendars(
                goalId, goalAddToCalendarsRequest.getYearMonths()
        );
        return new GoalAddToCalendarsResponse(addedGoalId, goalAddToCalendarsRequest.getYearMonths());
    }

}
