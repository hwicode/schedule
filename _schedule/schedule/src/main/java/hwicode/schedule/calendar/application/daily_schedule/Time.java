package hwicode.schedule.calendar.application.daily_schedule;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Time {

    public LocalDate now() {
        return LocalDate.now();
    }
}
