package hwicode.schedule.dailyschedule.daily_schedule_query.presentation;

import hwicode.schedule.common.config.auth.LoginInfo;
import hwicode.schedule.common.config.auth.LoginUser;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.DailyScheduleQueryService;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleQueryResponse;
import hwicode.schedule.dailyschedule.daily_schedule_query.application.dto.DailyScheduleSummaryQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DailyScheduleQueryController {

    private final DailyScheduleQueryService dailyScheduleQueryService;

    @GetMapping("/dailyschedule/calendar/daily-todo-lists")
    @ResponseStatus(value = HttpStatus.OK)
    public List<DailyScheduleSummaryQueryResponse> getMonthlyDailyScheduleQueryResponses(@LoginUser LoginInfo loginInfo,
                                                                                         @RequestParam YearMonth yearMonth) {
        return dailyScheduleQueryService.getMonthlyDailyScheduleQueryResponses(loginInfo.getUserId(), yearMonth);
    }

    @GetMapping("/dailyschedule/daily-todo-lists")
    @ResponseStatus(value = HttpStatus.OK)
    public DailyScheduleQueryResponse getDailyScheduleQueryResponse(@LoginUser LoginInfo loginInfo,
                                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return dailyScheduleQueryService.getDailyScheduleQueryResponse(loginInfo.getUserId(), date);
    }

}
