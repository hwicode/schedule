package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.DailySchedule;
import hwicode.schedule.calendar.infra.jpa_repository.DailyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@RequiredArgsConstructor
@Service
public class DailyScheduleProviderService {

    private final CalendarProviderService calendarProviderService;
    private final DailyScheduleRepository dailyScheduleRepository;

    public DailySchedule provideDailySchedule(LocalDate date) {
        return dailyScheduleRepository.findByDate(date)
                .orElseGet(() -> saveDailySchedule(date));
    }

    private DailySchedule saveDailySchedule(LocalDate date) {
        YearMonth yearMonth = YearMonth.from(date);
        Calendar calendar = calendarProviderService.provideCalendar(yearMonth);

        DailySchedule dailySchedule = new DailySchedule(calendar, date);
        return dailyScheduleRepository.save(dailySchedule);
    }

}
