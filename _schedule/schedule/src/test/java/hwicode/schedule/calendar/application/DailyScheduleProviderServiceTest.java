package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
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
        Calendar calendar = calendarProviderService.provideCalendar(YEAR_MONTH);

        LocalDate date = YEAR_MONTH.atDay(1);

        DailySchedule dailySchedule = new DailySchedule(calendar, date);
        dailyScheduleRepository.save(dailySchedule);

        LocalDate now = YEAR_MONTH.atDay(1);

        // when
        Long dailyScheduleId = dailyScheduleProviderService.provideDailyScheduleId(date, now);

        // then
        assertThat(dailySchedule.getId()).isEqualTo(dailyScheduleId);
    }

    @Test
    void 날짜로_Dailychedule를_생성_후_가져올_수_있다() {
        // given
        LocalDate date = YEAR_MONTH.atDay(1);
        LocalDate now = YEAR_MONTH.atDay(1);

        // when
        Long dailyScheduleId = dailyScheduleProviderService.provideDailyScheduleId(date, now);

        // then
        assertThat(dailyScheduleRepository.existsById(dailyScheduleId)).isTrue();
        assertThat(calendarRepository.findAll()).hasSize(1);
    }

    @Test
    void 날짜로_Dailychedule를_생성_할_때_당일이_아니면_에러가_발생한다() {
        // given
        LocalDate date = YEAR_MONTH.atDay(1);
        LocalDate now = YEAR_MONTH.atDay(2);

        // when then
        assertThatThrownBy(() -> dailyScheduleProviderService.provideDailyScheduleId(date, now))
                .isInstanceOf(DailyScheduleDateException.class);
    }

}
