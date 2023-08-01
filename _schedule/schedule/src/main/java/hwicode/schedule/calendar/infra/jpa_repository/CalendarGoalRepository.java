package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.domain.Calendar;
import hwicode.schedule.calendar.domain.CalendarGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarGoalRepository extends JpaRepository<CalendarGoal, Long> {

    List<CalendarGoal> findAllByCalendar(Calendar calendar);
}
