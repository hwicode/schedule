package hwicode.schedule.calendar.presentation.calendar;

import hwicode.schedule.calendar.application.query.CalendarQueryService;
import hwicode.schedule.calendar.application.query.dto.CalendarQueryResponse;
import hwicode.schedule.common.login.LoginInfo;
import hwicode.schedule.common.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RequiredArgsConstructor
@RestController
public class CalendarQueryController {

    private final CalendarQueryService calendarQueryService;

    @GetMapping("/calendars")
    @ResponseStatus(value = HttpStatus.OK)
    public CalendarQueryResponse getCalendarQueryResponse(@LoginUser LoginInfo loginInfo,
                                                          @RequestParam YearMonth yearMonth) {
        return calendarQueryService.getCalendarQueryResponse(loginInfo.getUserId(), yearMonth);
    }

}
