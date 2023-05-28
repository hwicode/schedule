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

import java.time.YearMonth;

@RequiredArgsConstructor
@Service
public class GoalAggregateService {

    private final GoalFindAndDeleteRepository goalFindAndDeleteRepository;
    private final SubGoalSaveRepository subGoalSaveRepository;

    @Transactional
    public Long createSubGoal(Long goalId, String subGoalName) {
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

    @Transactional
    public Long deleteCalendarGoal(Long goalId, YearMonth yearMonth) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(goalId);
        goal.deleteCalendarGoal(yearMonth);

        if (goal.isCalendarGoalEmpty()) {
            goalFindAndDeleteRepository.delete(goal);
        }
        return goalId;
    }

}
