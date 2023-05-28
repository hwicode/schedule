package hwicode.schedule.calendar.application;

import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.domain.SubGoal;
import hwicode.schedule.calendar.domain.SubGoalStatus;
import hwicode.schedule.calendar.exception.application.GoalNotFoundException;
import hwicode.schedule.calendar.infra.jpa_repository.GoalRepository;
import hwicode.schedule.calendar.infra.jpa_repository.SubGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;

@RequiredArgsConstructor
@Service
public class GoalAggregateService {

    private final GoalRepository goalRepository;
    private final SubGoalRepository subGoalRepository;

    @Transactional
    public Long createSubGoal(Long goalId, String subGoalName) {
        Goal goal = findGoalWithSubGoals(goalId);
        SubGoal subGoal = goal.createSubGoal(subGoalName);
        return subGoalRepository.save(subGoal)
                .getId();
    }

    @Transactional
    public String changeSubGoalName(Long goalId, String subGoalName, String newSubGoalName) {
        Goal goal = findGoalWithSubGoals(goalId);
        return goal.changeSubGoalName(subGoalName, newSubGoalName);
    }

    @Transactional
    public String deleteSubGoal(Long goalId, String subGoalName) {
        Goal goal = findGoalWithSubGoals(goalId);
        goal.deleteSubGoal(subGoalName);
        return subGoalName;
    }
    
    @Transactional
    public GoalStatus changeSubGoalStatus(Long goalId, String subGoalName, SubGoalStatus subGoalStatus) {
        Goal goal = findGoalWithSubGoals(goalId);
        return goal.changeSubGoalStatus(subGoalName, subGoalStatus);
    }

    @Transactional
    public GoalStatus changeGoalStatus(Long goalId, GoalStatus goalStatus) {
        Goal goal = findGoalWithSubGoals(goalId);
        return goal.changeGoalStatus(goalStatus);
    }

    @Transactional
    public Long deleteGoal(Long goalId) {
        Goal goal = findGoalWithSubGoals(goalId);
        goalRepository.delete(goal);
        return goalId;
    }

    @Transactional
    public Long deleteCalendarGoal(Long goalId, YearMonth yearMonth) {
        Goal goal = findGoalWithSubGoals(goalId);
        goal.deleteCalendarGoal(yearMonth);

        if (goal.isCalendarGoalEmpty()) {
            goalRepository.delete(goal);
        }
        return goalId;
    }

    private Goal findGoalWithSubGoals(Long id) {
        return goalRepository.findGoalWithSubGoals(id)
                .orElseThrow(GoalNotFoundException::new);
    }

}
