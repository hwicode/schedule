package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalendarProviderService {

    private final CalendarRepository calendarRepository;

    public Calendar provideCalendar(YearMonth yearMonth) {
        return calendarRepository.findByYearAndMonth(yearMonth)
                .orElseGet(() -> saveCalendar(yearMonth));
    }

    private Calendar saveCalendar(YearMonth yearMonth) {
        Calendar calendar = new Calendar(yearMonth);
        return calendarRepository.save(calendar);
    }

    public List<Calendar> provideCalendars(List<YearMonth> yearMonths) {
        List<Calendar> calendars = new ArrayList<>();
        for (YearMonth yearMonth : yearMonths) {
            calendars.add(new Calendar(yearMonth));
        }
        return calendarRepository.saveAll(calendars);
    }

}
