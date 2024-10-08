package hwicode.schedule.calendar.infra.jpa_repository.goal;

import hwicode.schedule.calendar.application.query.dto.SubGoalQueryResponse;
import hwicode.schedule.calendar.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long>, GoalFindRepository {

    @Query("SELECT g FROM Goal g "
            + "LEFT JOIN FETCH g.subGoals "
            + "WHERE g.id = :id")
    Optional<Goal> findGoalWithSubGoals(@Param("id") Long id);

    // 여기부터 조회기능
    @Query("SELECT "
            + "new hwicode.schedule.calendar.application.query.dto.SubGoalQueryResponse(s.id, s.name, s.subGoalStatus, g.id) "
            + "FROM Goal g "
            + "INNER JOIN g.subGoals s "
            + "WHERE g.id in :goalIds "
            + "ORDER BY s.id ASC")
    List<SubGoalQueryResponse> findSubGoalQueryResponsesBy(@Param("goalIds") List<Long> goalIds);
}
