package hwicode.schedule.calendar.presentation.calendar;

import hwicode.schedule.calendar.application.CalendarAggregateService;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveRequest;
import hwicode.schedule.calendar.presentation.calendar.dto.save.GoalSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
