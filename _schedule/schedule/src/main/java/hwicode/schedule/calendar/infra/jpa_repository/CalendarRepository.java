package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.application.query.dto.CalendarQueryResponse;
import hwicode.schedule.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.Optional;

//todo: yearAndMonth에 대한 인덱스를 어떻게 할 지 생각해보기 -> 일단 yearAndMonth에 unique 제약 조건으로 논-클러스터링 인덱스 생성
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

