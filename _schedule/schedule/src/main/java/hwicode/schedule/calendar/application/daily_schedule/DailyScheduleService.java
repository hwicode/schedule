package hwicode.schedule.calendar.application.daily_schedule;

import hwicode.schedule.calendar.application.calendar.CalendarProviderService;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.DailySchedule;
import hwicode.schedule.calendar.exception.application.DailyScheduleDateException;
import hwicode.schedule.calendar.exception.application.DailyScheduleExistException;
import hwicode.schedule.calendar.infra.jpa_repository.DailyScheduleRepository;
import hwicode.schedule.calendar.infra.other_boundedcontext.DailySchedulePostSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;

@RequiredArgsConstructor
@Service
public class DailyScheduleService {

    private final CalendarProviderService calendarProviderService;
    private final DailySchedulePostSaveService dailySchedulePostSaveService;
    private final DailyScheduleRepository dailyScheduleRepository;
    private final Time time;

    @Transactional
    public Long saveDailySchedule(DailyScheduleSaveCommand command) {
        LocalDate date = command.getDate();
        validateDate(time.now(), date);

        if (dailyScheduleRepository.findByDate(command.getUserId(), date).isPresent()) {
            throw new DailyScheduleExistException();
        }

        DailySchedule dailySchedule = saveDailySchedule(command.getUserId(), date);
        return dailySchedule.getId();
    }

    private void validateDate(LocalDate now, LocalDate date) {
        if (!now.equals(date)) {
            throw new DailyScheduleDateException();
        }
    }

    private DailySchedule saveDailySchedule(Long userId, LocalDate date) {
        YearMonth yearMonth = YearMonth.from(date);
        Calendar calendar = calendarProviderService.provideCalendar(userId, yearMonth);

        DailySchedule dailySchedule = new DailySchedule(calendar, date, userId);
        dailyScheduleRepository.save(dailySchedule);

        // DailySchedulePostSaveService는 Daily_Schedule이 생성되고 난 후 수행할 작업을 담당함
        dailySchedulePostSaveService.perform(userId, dailySchedule.getId());
        return dailySchedule;
    }

}
