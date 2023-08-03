package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarGoalRepository extends JpaRepository<CalendarGoal, Long> {

    @Query("SELECT c FROM CalendarGoal c "
            + "JOIN FETCH c.goal "
            + "WHERE c.calendar = :calendar")
    List<CalendarGoal> findAllByCalendarWithGoal(@Param("calendar") Calendar calendar);
}
