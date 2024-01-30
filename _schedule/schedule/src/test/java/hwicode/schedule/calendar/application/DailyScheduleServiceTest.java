package hwicode.schedule.calendar.application;

import hwicode.schedule.DatabaseCleanUp;
import hwicode.schedule.calendar.application.dto.daily_schedule.DailyScheduleSaveCommand;
import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.DailySchedule;
import hwicode.schedule.calendar.exception.application.DailyScheduleDateException;
import hwicode.schedule.calendar.exception.application.DailyScheduleExistException;
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
class DailyScheduleServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    DailyScheduleService dailyScheduleService;

    @Autowired
    CalendarProviderService calendarProviderService;

    @Autowired
    DailyScheduleRepository dailyScheduleRepository;

    @BeforeEach
    void clearDatabase() {
        databaseCleanUp.execute();
    }

    @Test
    void 날짜로_DailySchedule를_생성할_수_있다() {
        // given
        Long userId = 1L;
        LocalDate date = YEAR_MONTH.atDay(1);
        LocalDate now = YEAR_MONTH.atDay(1);

        DailyScheduleSaveCommand command = new DailyScheduleSaveCommand(userId, date, now);

        // when
        Long dailyScheduleId = dailyScheduleService.saveDailySchedule(command);

        // then
        assertThat(dailyScheduleRepository.existsById(dailyScheduleId)).isTrue();
    }

    @Test
    void Dailychedule가_이미_존재하면_에러가_발생한다() {
        // given
        Long userId = 1L;
        LocalDate date = YEAR_MONTH.atDay(1);
        LocalDate now = YEAR_MONTH.atDay(1);

        DailyScheduleSaveCommand command = new DailyScheduleSaveCommand(userId, date, now);
        dailyScheduleService.saveDailySchedule(command);

        // when then
        assertThatThrownBy(() -> dailyScheduleService.saveDailySchedule(command))
                .isInstanceOf(DailyScheduleExistException.class);
    }

    @Test
    void 날짜로_Dailychedule를_생성_할_때_당일이_아니면_에러가_발생한다() {
        // given
        Long userId = 1L;
        LocalDate date = YEAR_MONTH.atDay(1);
        LocalDate now = YEAR_MONTH.atDay(2);

        DailyScheduleSaveCommand command = new DailyScheduleSaveCommand(userId, date, now);

        // when then
        assertThatThrownBy(() -> dailyScheduleService.saveDailySchedule(command))
                .isInstanceOf(DailyScheduleDateException.class);
    }

}
