package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.application.query.dto.CalendarQueryResponse;
import hwicode.schedule.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.Optional;

// Calendar의 yearAndMonth에 세컨더리 인덱스 추가함
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("SELECT c FROM Calendar c "
            + "WHERE c.yearAndMonth = :yearAndMonth")
    Optional<Calendar> findByYearAndMonth(@Param("yearAndMonth") YearMonth yearAndMonth);

    @Query("SELECT "
            + "new hwicode.schedule.calendar.application.query.dto.CalendarQueryResponse(c.id, c.yearAndMonth, c.weeklyStudyDate) "
            + "FROM Calendar c "
            + "WHERE c.yearAndMonth = :yearAndMonth")
    Optional<CalendarQueryResponse> findCalendarQueryResponseBy(@Param("yearAndMonth") YearMonth yearAndMonth);
}
