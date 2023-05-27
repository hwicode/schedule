package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.domain.CalendarGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarGoalRepository extends JpaRepository<CalendarGoal, Long> {
}
