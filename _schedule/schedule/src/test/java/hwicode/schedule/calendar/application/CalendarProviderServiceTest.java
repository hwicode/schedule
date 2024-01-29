package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.exception.application.YearMonthNullException;
import hwicode.schedule.calendar.exception.application.YearMonthsSizeNotValidException;
import hwicode.schedule.calendar.infra.jpa_repository.CalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static hwicode.schedule.calendar.CalendarDataHelper.YEAR_MONTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Long userId = 1L;
        Calendar savedCalendar = calendarRepository.save(new Calendar(YEAR_MONTH, userId));

        // when
        Calendar providedCalendar = calendarProviderService.provideCalendar(userId, YEAR_MONTH);

        // then
        assertThat(savedCalendar.getId()).isEqualTo(providedCalendar.getId());
    }

    @Test
    void YearMonth로_캘린더를_생성_후_가져올_수_있다() {
        // when
        Long userId = 1L;
        Calendar savedCalendar = calendarRepository.save(new Calendar(YEAR_MONTH, userId));

        // then
        assertThat(calendarRepository.existsById(savedCalendar.getId())).isTrue();
    }

    @Test
    void YearMonth의_리스트로_캘린더들을_가져올_수_있다() {
        // given
        Long userId = 1L;
        calendarRepository.save(new Calendar(YEAR_MONTH, userId));
        calendarRepository.save(new Calendar(YEAR_MONTH.plusMonths(1), userId));

        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1)
        );

        // when
        List<Calendar> calendars = calendarProviderService.provideCalendars(userId, yearMonths);

        // then
        assertThat(calendars).hasSize(2);
        assertThat(calendarRepository.existsById(calendars.get(0).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(1).getId())).isTrue();
    }

    @Test
    void YearMonth의_리스트로_존재하지_않는_캘린더는_생성해서_가져올_수_있다() {
        // given
        Long userId = 1L;
        calendarRepository.save(new Calendar(YEAR_MONTH, userId));
        calendarRepository.save(new Calendar(YEAR_MONTH.plusMonths(1), userId));

        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2)
        );

        // when
        List<Calendar> calendars = calendarProviderService.provideCalendars(userId, yearMonths);

        // then
        assertThat(calendars).hasSize(3);
        assertThat(calendarRepository.findAll()).hasSize(3);
        assertThat(calendarRepository.existsById(calendars.get(0).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(1).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(2).getId())).isTrue();
    }

    @Test
    void YearMonth의_리스트로_캘린더들을_생성_후_가져올_수_있다() {
        // given
        Long userId = 1L;
        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH,
                YEAR_MONTH.plusMonths(1),
                YEAR_MONTH.plusMonths(2)
        );

        // when
        List<Calendar> calendars = calendarProviderService.provideCalendars(userId, yearMonths);

        // then
        assertThat(calendars).hasSize(3);
        assertThat(calendarRepository.existsById(calendars.get(0).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(1).getId())).isTrue();
        assertThat(calendarRepository.existsById(calendars.get(2).getId())).isTrue();
    }

    @Test
    void YearMonth의_리스트가_24보다_크다면_에러가_발생한다() {
        // given
        List<YearMonth> yearMonths = List.of(
                YEAR_MONTH, YEAR_MONTH.plusMonths(1), YEAR_MONTH.plusMonths(2),
                YEAR_MONTH.plusMonths(3), YEAR_MONTH.plusMonths(4), YEAR_MONTH.plusMonths(5),
                YEAR_MONTH.plusMonths(6), YEAR_MONTH.plusMonths(7), YEAR_MONTH.plusMonths(8),
                YEAR_MONTH.plusMonths(9),YEAR_MONTH.plusMonths(10),YEAR_MONTH.plusMonths(11),
                YEAR_MONTH.plusMonths(12),YEAR_MONTH.plusMonths(13),YEAR_MONTH.plusMonths(14),
                YEAR_MONTH.plusMonths(15),YEAR_MONTH.plusMonths(16),YEAR_MONTH.plusMonths(17),
                YEAR_MONTH.plusMonths(18),YEAR_MONTH.plusMonths(19),YEAR_MONTH.plusMonths(20),
                YEAR_MONTH.plusMonths(21),YEAR_MONTH.plusMonths(22),YEAR_MONTH.plusMonths(23),
                YEAR_MONTH.plusMonths(24)
        );

        // when then
        assertThatThrownBy(() -> calendarProviderService.provideCalendars(1L, yearMonths))
                .isInstanceOf(YearMonthsSizeNotValidException.class);
        assertThat(yearMonths).hasSize(25);
    }

    @Test
    void YearMonth의_리스트에_null이_존재하면_에러가_발생한다() {
        // given
        List<YearMonth> yearMonths = new ArrayList<>();
        yearMonths.add(null);

        // when then
        assertThatThrownBy(() -> calendarProviderService.provideCalendars(1L, yearMonths))
                .isInstanceOf(YearMonthNullException.class);
    }

}
