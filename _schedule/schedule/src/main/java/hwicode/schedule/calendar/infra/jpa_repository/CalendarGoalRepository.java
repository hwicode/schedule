package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.application.query.dto.GoalQueryResponse;
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

    @Query("SELECT new hwicode.schedule.calendar.application.query.dto.GoalQueryResponse(g.id, g.name, g.goalStatus) "
    + "FROM CalendarGoal cg "
    + "INNER JOIN cg.goal g "
    + "WHERE cg.calendar.id = :calendarId")
    List<GoalQueryResponse> findGoalQueryResponseBy(@Param("calendarId") Long calendarId);
}
