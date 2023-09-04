package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.application.query.dto.GoalQueryResponse;
import hwicode.schedule.calendar.domain.CalendarGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarGoalRepository extends JpaRepository<CalendarGoal, Long> {

    @Query("SELECT c FROM CalendarGoal c "
            + "JOIN FETCH c.goal "
            + "WHERE c.calendar.id = :calendarId")
    List<CalendarGoal> findAllByCalendarWithGoal(@Param("calendarId") Long calendarId);

    // 여기부터 조회기능
    @Query("SELECT new hwicode.schedule.calendar.application.query.dto.GoalQueryResponse(g.id, g.name, g.goalStatus) "
            + "FROM CalendarGoal cg "
            + "INNER JOIN cg.goal g "
            + "WHERE cg.calendar.id = :calendarId "
            + "ORDER BY g.id ASC")
    List<GoalQueryResponse> findGoalQueryResponseBy(@Param("calendarId") Long calendarId);
}
