package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
