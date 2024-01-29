package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.application.dto.daily_schedule.DailyScheduleProvideCommand;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.DailySchedule;
import hwicode.schedule.calendar.exception.application.DailyScheduleDateException;
import hwicode.schedule.calendar.infra.jpa_repository.DailyScheduleRepository;
import hwicode.schedule.calendar.infra.other_boundedcontext.DailySchedulePostSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@RequiredArgsConstructor
@Service
public class DailyScheduleProviderService {

    private final CalendarProviderService calendarProviderService;
    private final DailySchedulePostSaveService dailySchedulePostSaveService;
    private final DailyScheduleRepository dailyScheduleRepository;

    public Long provideDailyScheduleId(DailyScheduleProvideCommand command) {
        LocalDate now = command.getNow();
        LocalDate date = command.getDate();
        if (!now.equals(date)) {
            throw new DailyScheduleDateException();
        }
        DailySchedule dailySchedule = dailyScheduleRepository.findByDate(date)
                .orElseGet(() -> saveDailySchedule(date, command.getUserId()));

        return dailySchedule.getId();
    }

    private DailySchedule saveDailySchedule(LocalDate date, Long userId) {
        YearMonth yearMonth = YearMonth.from(date);
        Calendar calendar = calendarProviderService.provideCalendar(userId, yearMonth);

        DailySchedule dailySchedule = new DailySchedule(calendar, date, userId);
        dailyScheduleRepository.save(dailySchedule);

        // DailySchedulePostSaveService는 Daily_Schedule이 생성되고 난 후 수행할 작업을 담당함
        dailySchedulePostSaveService.perform(userId, dailySchedule.getId());
        return dailySchedule;
    }

}
