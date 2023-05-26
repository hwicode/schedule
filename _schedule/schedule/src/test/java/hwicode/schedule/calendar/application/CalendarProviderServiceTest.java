package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.List;

import static hwicode.schedule.calendar.CalendarDataHelper.YEAR_MONTH;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CalendarProviderServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    CalendarProviderService calendarProviderService;

    @Autowired
    CalendarRepository calendarRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void YearMonth로_캘린더를_가져올_수_있다() {
        // given
        Calendar savedCalendar = calendarRepository.save(new Calendar(YEAR_MONTH));

        // when
        Calendar providedCalendar = calendarProviderService.provideCalendar(YEAR_MONTH);

        // then
        assertThat(savedCalendar.getId()).isEqualTo(providedCalendar.getId());
    }

    @Test
    void YearMonth로_캘린더를_생성_후_가져올_수_있다() {
        // when
        Calendar calendar = calendarProviderService.provideCalendar(YEAR_MONTH);

        // then
        assertThat(calendarRepository.existsById(calendar.getId())).isTrue();
    }

    @Test
    void YearMonth의_리스트로_캘린더들을_가져올_수_있다() {
        // given
        calendarRepository.save(new Calendar(YEAR_MONTH));
        calendarRepository.save(new Calendar(YEAR_MONTH.plusMonths(1)));

        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1)
        );

        // when
        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);

        // then
        assertThat(calendars).hasSize(2);
        assertThat(calendarRepository.existsById(calendars.get(0).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(1).getId())).isTrue();
    }

    @Test
    void YearMonth의_리스트로_존재하지_않는_캘린더는_생성해서_가져올_수_있다() {
        // given
        calendarRepository.save(new Calendar(YEAR_MONTH));
        calendarRepository.save(new Calendar(YEAR_MONTH.plusMonths(1)));

        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2)
        );

        // when
        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);

        // then
        assertThat(calendars).hasSize(3);
        assertThat(calendarRepository.existsById(calendars.get(0).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(1).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(2).getId())).isTrue();
    }

    @Test
    void YearMonth의_리스트로_캘린더들을_생성_후_가져올_수_있다() {
        // given
        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2)
        );

        // when
        List<Calendar> calendars = calendarProviderService.provideCalendars(yearMonths);

        // then
        assertThat(calendars).hasSize(3);
        assertThat(calendarRepository.existsById(calendars.get(0).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(1).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(2).getId())).isTrue();
    }

}
