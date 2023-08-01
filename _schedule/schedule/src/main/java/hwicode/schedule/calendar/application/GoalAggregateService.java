package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.domain.SubGoal;
import hwicode.schedule.calendar.domain.SubGoalStatus;
import hwicode.schedule.calendar.infra.limited_repository.GoalFindAndDeleteRepository;
import hwicode.schedule.calendar.infra.limited_repository.SubGoalSaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GoalAggregateService {

    private final GoalFindAndDeleteRepository goalFindAndDeleteRepository;
    private final SubGoalSaveRepository subGoalSaveRepository;

    @Transactional
    public Long saveSubGoal(Long goalId, String subGoalName) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(goalId);
        SubGoal subGoal = goal.createSubGoal(subGoalName);
        return subGoalSaveRepository.save(subGoal)
                .getId();
    }

    @Transactional
    public String changeSubGoalName(Long goalId, String subGoalName, String newSubGoalName) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(goalId);
        return goal.changeSubGoalName(subGoalName, newSubGoalName);
    }

    @Transactional
    public String deleteSubGoal(Long goalId, String subGoalName) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(goalId);
        goal.deleteSubGoal(subGoalName);
        return subGoalName;
    }
    
    @Transactional
    public GoalStatus changeSubGoalStatus(Long goalId, String subGoalName, SubGoalStatus subGoalStatus) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(goalId);
        return goal.changeSubGoalStatus(subGoalName, subGoalStatus);
    }

    @Transactional
    public GoalStatus changeGoalStatus(Long goalId, GoalStatus goalStatus) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(goalId);
        return goal.changeGoalStatus(goalStatus);
    }

    @Transactional
    public Long deleteGoal(Long goalId) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(goalId);
        goalFindAndDeleteRepository.delete(goal);
        return goalId;
    }

}
