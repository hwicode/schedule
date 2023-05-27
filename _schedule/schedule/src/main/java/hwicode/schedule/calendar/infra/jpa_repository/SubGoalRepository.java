package hwicode.schedule.calendar.infra.jpa_repository;

import hwicode.schedule.calendar.domain.SubGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubGoalRepository extends JpaRepository<SubGoal, Long> {
}
