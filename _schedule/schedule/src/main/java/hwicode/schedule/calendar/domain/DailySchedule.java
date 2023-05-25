package hwicode.schedule.calendar.domain;

import java.time.LocalDate;

public class DailySchedule {

    private Calendar calendar;
    private LocalDate today;

    public DailySchedule(Calendar calendar, LocalDate today) {
        this.calendar = calendar;
        this.today = today;
    }

}
