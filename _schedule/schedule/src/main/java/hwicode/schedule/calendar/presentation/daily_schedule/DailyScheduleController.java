package hwicode.schedule.calendar.presentation.daily_schedule;

import hwicode.schedule.calendar.application.DailyScheduleProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
public class DailyScheduleController {

    private final DailyScheduleProviderService dailyScheduleProviderService;

    @GetMapping("/daily-todo-lists")
    public String provideDailyScheduleAndRedirect(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Long dailyScheduleId = dailyScheduleProviderService.provideDailyScheduleId(date, LocalDate.now());
        return "redirect:/dailyschedule/daily-todo-lists/" + dailyScheduleId;
    }

}
