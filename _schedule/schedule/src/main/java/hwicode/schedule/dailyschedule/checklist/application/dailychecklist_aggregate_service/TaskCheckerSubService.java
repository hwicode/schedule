package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.TaskChecker;
import hwicode.schedule.dailyschedule.checklist.infra.other_boundedcontext.TaskCheckerPrePostService;
import hwicode.schedule.dailyschedule.shared_domain.TaskStatus;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.DailyChecklistFindRepository;
import hwicode.schedule.dailyschedule.checklist.infra.limited_repository.TaskCheckerSaveRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
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
    public Long saveTaskChecker(TaskSaveRequest taskSaveRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskSaveRequest.getDailyChecklistId());

        TaskChecker taskChecker = dailyChecklist.createTaskChecker(
                taskSaveRequest.getTaskName(), taskSaveRequest.getDifficulty()
        );
        taskCheckerSaveRepository.save(taskChecker);

        taskCheckerPrePostService.performAfterSave(taskChecker.getId(), taskSaveRequest);
        return taskChecker.getId();
    }

    @Transactional
    public Long deleteTaskChecker(Long dailyChecklistId, Long taskId, String taskCheckerName) {
        taskCheckerPrePostService.performBeforeDelete(taskId);
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(dailyChecklistId);
        dailyChecklist.deleteTaskChecker(taskCheckerName);
        return taskId;
    }

    @Transactional
    public TaskStatus changeTaskStatus(String taskCheckerName, TaskStatusModifyRequest taskStatusModifyRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskStatus(taskCheckerName, taskStatusModifyRequest.getTaskStatus());
    }

    @Transactional
    public Difficulty changeTaskDifficulty(String taskCheckerName, TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskDifficultyModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeDifficulty(taskCheckerName, taskDifficultyModifyRequest.getDifficulty());
    }

    @Transactional
    public String changeTaskCheckerName(String taskCheckerName, TaskCheckerNameModifyRequest taskCheckerNameModifyRequest) {
        DailyChecklist dailyChecklist = dailyChecklistFindRepository.findDailyChecklistWithTaskCheckers(
                taskCheckerNameModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskCheckerName(taskCheckerName, taskCheckerNameModifyRequest.getNewTaskCheckerName());
    }

}
