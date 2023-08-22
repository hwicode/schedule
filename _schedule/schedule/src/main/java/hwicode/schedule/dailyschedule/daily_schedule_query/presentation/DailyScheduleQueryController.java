package hwicode.schedule.dailyschedule.daily_schedule_query.presentation;

import hwicode.schedule.dailyschedule.daily_schedule_query.application.DailyScheduleQueryService;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DailyScheduleQueryController {

    private final DailyScheduleQueryService dailyScheduleQueryService;

    @GetMapping("/dailyschedule/daily-todo-lists")
    @ResponseStatus(value = HttpStatus.OK)
    public List<DailyScheduleSummaryQueryResponse> changeDailyToDoListInformation(@RequestParam YearMonth yearMonth) {
        return dailyScheduleQueryService.getDailyToDoListQueryResponse(yearMonth);
    }

}
