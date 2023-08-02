package hwicode.schedule.calendar.infra.jpa_repository.goal;

import hwicode.schedule.calendar.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long>, GoalFindRepository {

    @Query("SELECT g FROM Goal g "
    + "LEFT JOIN FETCH g.subGoals "
    + "WHERE g.id = :id")
    Optional<Goal> findGoalWithSubGoals(@Param("id") Long id);
}
