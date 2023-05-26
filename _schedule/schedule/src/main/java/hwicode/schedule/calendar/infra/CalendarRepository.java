package hwicode.schedule.calendar.infra;

import hwicode.schedule.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.Optional;

//todo: yearAndMonth에 대한 인덱스를 어떻게 할 지 생각해보기
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("SELECT c FROM Calendar c "
            + "WHERE c.yearAndMonth = :yearAndMonth")
    Optional<Calendar> findByYearAndMonth(@Param("yearAndMonth") YearMonth yearAndMonth);
}
