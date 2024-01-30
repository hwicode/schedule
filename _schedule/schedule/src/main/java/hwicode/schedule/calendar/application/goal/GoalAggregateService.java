package hwicode.schedule.calendar.application.goal;

import hwicode.schedule.calendar.application.goal.dto.*;
import hwicode.schedule.calendar.domain.Goal;
import hwicode.schedule.calendar.domain.GoalStatus;
import hwicode.schedule.calendar.domain.SubGoal;
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
    public Long saveSubGoal(SubGoalSaveCommand command) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(command.getGoalId());
        goal.checkOwnership(command.getUserId());

        SubGoal subGoal = goal.createSubGoal(command.getName());
        return subGoalSaveRepository.save(subGoal)
                .getId();
    }

    @Transactional
    public String changeSubGoalName(SubGoalModifyNameCommand command) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(command.getGoalId());
        goal.checkOwnership(command.getUserId());

        return goal.changeSubGoalName(command.getName(), command.getNewName());
    }

    @Transactional
    public String deleteSubGoal(SubGoalDeleteCommand command) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(command.getGoalId());
        goal.checkOwnership(command.getUserId());

        goal.deleteSubGoal(command.getName());
        return command.getName();
    }
    
    @Transactional
    public GoalStatus changeSubGoalStatus(SubGoalModifyStatusCommand command) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(command.getGoalId());
        goal.checkOwnership(command.getUserId());

        return goal.changeSubGoalStatus(command.getName(), command.getSubGoalStatus());
    }

    @Transactional
    public GoalStatus changeGoalStatus(GoalModifyStatusCommand command) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(command.getGoalId());
        goal.checkOwnership(command.getUserId());

        return goal.changeGoalStatus(command.getGoalStatus());
    }

    @Transactional
    public Long deleteGoal(GoalDeleteCommand command) {
        Goal goal = goalFindAndDeleteRepository.findGoalWithSubGoals(command.getGoalId());
        goal.checkOwnership(command.getUserId());

        goalFindAndDeleteRepository.delete(goal);
        return command.getGoalId();
    }

}
