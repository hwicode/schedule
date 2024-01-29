package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.application.dto.daily_schedule.DailyScheduleProvideCommand;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.DailySchedule;
import hwicode.schedule.calendar.exception.application.DailyScheduleDateException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import hwicode.schedule.calendar.infra.jpa_repository.DailyScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static hwicode.schedule.calendar.CalendarDataHelper.YEAR_MONTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DailyScheduleProviderServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyScheduleProviderService dailyScheduleProviderService;

    @Autowired
    CalendarProviderService calendarProviderService;

    @Autowired
    DailyScheduleRepository dailyScheduleRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 날짜로_DailySchedule를_가져올_수_있다() {
        // given
        Long userId = 1L;
        Calendar calendar = calendarProviderService.provideCalendar(userId, YEAR_MONTH);

        LocalDate date = YEAR_MONTH.atDay(1);

        DailySchedule dailySchedule = new DailySchedule(calendar, date, userId);
        dailyScheduleRepository.save(dailySchedule);

        LocalDate now = YEAR_MONTH.atDay(1);

        DailyScheduleProvideCommand command = new DailyScheduleProvideCommand(userId, date, now);

        // when
        Long dailyScheduleId = dailyScheduleProviderService.provideDailyScheduleId(command);

        // then
        assertThat(dailySchedule.getId()).isEqualTo(dailyScheduleId);
    }

    @Test
    void 날짜로_Dailychedule를_생성_후_가져올_수_있다() {
        // given
        Long userId = 1L;
        LocalDate date = YEAR_MONTH.atDay(1);
        LocalDate now = YEAR_MONTH.atDay(1);

        DailyScheduleProvideCommand command = new DailyScheduleProvideCommand(userId, date, now);

        // when
        Long dailyScheduleId = dailyScheduleProviderService.provideDailyScheduleId(command);

        // then
        assertThat(dailyScheduleRepository.existsById(dailyScheduleId)).isTrue();
        assertThat(calendarRepository.findAll()).hasSize(1);
    }

    @Test
    void 날짜로_Dailychedule를_생성_할_때_당일이_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        LocalDate date = YEAR_MONTH.atDay(1);
        LocalDate now = YEAR_MONTH.atDay(2);

        DailyScheduleProvideCommand command = new DailyScheduleProvideCommand(userId, date, now);

        // when then
        assertThatThrownBy(() -> dailyScheduleProviderService.provideDailyScheduleId(command))
                .isInstanceOf(DailyScheduleDateException.class);
    }

}
