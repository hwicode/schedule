package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.domain.SubGoal;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GoalAggregateService {

    private final GoalRepository goalRepository;
    private final SubGoalRepository subGoalRepository;

    @Transactional
    public Long createSubGoal(Long goalId, String subGoalName) {
        Goal goal = goalRepository.findGoalWithSubGoals(goalId).orElseThrow();
        SubGoal subGoal = goal.createSubGoal(subGoalName);
        return subGoalRepository.save(subGoal)
                .getId();
    }

    @Transactional
    public String changeSubGoalName(Long goalId, String subGoalName, String newSubGoalName) {
        Goal goal = goalRepository.findGoalWithSubGoals(goalId).orElseThrow();
        return goal.changeSubGoalName(subGoalName, newSubGoalName);
    }

    @Transactional
    public String deleteSubGoal(Long goalId, String subGoalName) {
        Goal goal = goalRepository.findGoalWithSubGoals(goalId).orElseThrow();
        goal.deleteSubGoal(subGoalName);
        return subGoalName;
    }

}
