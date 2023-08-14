package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.exception.application.YearMonthNullException;
import hwicode.schedule.calendar.exception.application.YearMonthsSizeNotValidException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        validateYearMonths(yearMonths);
        List<Calendar> calendars = new ArrayList<>();
        List<Calendar> noneSavedCalendars = new ArrayList<>();

        for (YearMonth yearMonth : yearMonths) {
            Optional<Calendar> calendar = calendarRepository.findByYearAndMonth(yearMonth);

            calendar.ifPresentOrElse(
                    calendars::add,
                    () -> noneSavedCalendars.add(new Calendar(yearMonth))
            );
        }

        if (!noneSavedCalendars.isEmpty()) {
            calendars.addAll(calendarRepository.saveAll(noneSavedCalendars));
        }
        return calendars;
    }

    private void validateYearMonths(List<YearMonth> yearMonths) {
        if (yearMonths.size() > 24) {
            throw new YearMonthsSizeNotValidException();
        }

        for (YearMonth yearMonth : yearMonths) {
            if (yearMonth == null) {
                throw new YearMonthNullException();
            }
        }
    }

}
