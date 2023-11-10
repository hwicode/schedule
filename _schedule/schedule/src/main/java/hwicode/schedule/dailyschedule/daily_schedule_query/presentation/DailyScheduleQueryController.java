package hwicode.schedule.dailyschedule.daily_schedule_query.presentation;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.DailyScheduleQueryService;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class DailyScheduleQueryController {

    private final DailyScheduleQueryService dailyScheduleQueryService;

    @GetMapping("/dailyschedule/calendar/daily-todo-lists")
    @ResponseStatus(value = HttpStatus.OK)
    public List<DailyScheduleSummaryQueryResponse> getMonthlyDailyScheduleQueryResponses(@RequestParam YearMonth yearMonth) {
        return dailyScheduleQueryService.getMonthlyDailyScheduleQueryResponses(yearMonth);
    }

    @GetMapping("/dailyschedule/daily-todo-lists/{dailyToDoListId}")
    @ResponseStatus(value = HttpStatus.OK)
    public DailyScheduleQueryResponse getDailyScheduleQueryResponse(@PathVariable("dailyToDoListId") @Positive Long dailyScheduleId) {
        return dailyScheduleQueryService.getDailyScheduleQueryResponse(dailyScheduleId);
    }

}
