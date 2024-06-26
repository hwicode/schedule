package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.common.login.validator.PermissionValidator;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.task_checker.*;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.TaskCheckerSaveRepository;
import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.TaskCheckerPrePostService;
import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.dto.TaskCheckerAfterSaveRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskCheckerSubService {

    private final DailyChecklistFindRepository dailyChecklistFindRepository;
    private final TaskCheckerSaveRepository taskCheckerSaveRepository;
    private final TaskCheckerPrePostService taskCheckerPrePostService;

    @Transactional
    public Long saveTaskChecker(TaskSaveCommand command) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        TaskChecker taskChecker = dailyChecklist.createTaskChecker(command.getTaskCheckerName(), command.getDifficulty());
        taskCheckerSaveRepository.save(taskChecker);

        TaskCheckerAfterSaveRequest request = new TaskCheckerAfterSaveRequest(
                taskChecker.getId(), command.getPriority(), command.getImportance(), command.getUserId()
        );
        taskCheckerPrePostService.performAfterSave(request);

        return taskChecker.getId();
    }

    @Transactional
    public Long deleteTaskChecker(TaskDeleteCommand command) {
        taskCheckerPrePostService.performBeforeDelete(command.getTaskId());

        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        dailyChecklist.deleteTaskChecker(command.getTaskCheckName());
        return command.getTaskId();
    }

    @Transactional
    public TaskStatus changeTaskStatus(TaskStatusModifyCommand command) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        return dailyChecklist.changeTaskStatus(command.getTaskCheckerName(), command.getTaskStatus());
    }

    @Transactional
    public Difficulty changeTaskDifficulty(TaskDifficultyModifyCommand command) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        return dailyChecklist.changeDifficulty(command.getTaskCheckerName(), command.getDifficulty());
    }

    @Transactional
    public String changeTaskCheckerName(TaskCheckerNameModifyCommand command) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                command.getDailyChecklistId());
        PermissionValidator.validateOwnership(command.getUserId(), dailyChecklist.getUserId());

        return dailyChecklist.changeTaskCheckerName(command.getTaskCheckerName(), command.getNewTaskCheckerName());
    }

}
