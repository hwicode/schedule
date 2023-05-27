package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.domain.SubGoal;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoalAggregateService {

    private final GoalRepository goalRepository;
    private final SubGoalRepository subGoalRepository;

    public Long createSubGoal(Long goalId, String subGoalName) {
        Goal goal = goalRepository.findGoalWithSubGoals(goalId).orElseThrow();
        SubGoal subGoal = goal.createSubGoal(subGoalName);
        return subGoalRepository.save(subGoal)
                .getId();
    }

}
