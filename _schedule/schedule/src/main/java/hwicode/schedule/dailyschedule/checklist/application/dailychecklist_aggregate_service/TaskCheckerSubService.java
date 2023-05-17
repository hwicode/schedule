package hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service;

import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.name_modify.TaskCheckerNameModifyRequest;
import hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.dto.taskchecker.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.taskchecker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.shared_domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.dailychecklist_aggregate_service.DailyChecklistFindService.findDailyChecklistWithTaskCheckers;

@RequiredArgsConstructor
@Service
public class TaskCheckerSubService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final TaskCheckerSaveOnlyRepository taskCheckerSaveOnlyRepository;

    @Transactional
    public Long saveTaskChecker(TaskCheckerSaveRequest taskCheckerSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, taskCheckerSaveRequest.getDailyChecklistId());

        TaskChecker taskChecker = taskCheckerSaveRequest.toEntity();
        dailyChecklist.addTaskChecker(taskChecker);

        return taskCheckerSaveOnlyRepository.save(taskChecker)
                .getId();
    }

    @Transactional
    public void deleteTaskChecker(Long dailyChecklistId, String taskCheckerName) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.deleteTaskChecker(taskCheckerName);
    }

    @Transactional
    public TaskStatus changeTaskStatus(String taskCheckerName, TaskStatusModifyRequest taskStatusModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, taskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskStatus(taskCheckerName, taskStatusModifyRequest.getTaskStatus());
    }

    @Transactional
    public Difficulty changeTaskDifficulty(String taskCheckerName, TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, taskDifficultyModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeDifficulty(taskCheckerName, taskDifficultyModifyRequest.getDifficulty());
    }

    @Transactional
    public String changeTaskCheckerName(String taskCheckerName, TaskCheckerNameModifyRequest taskCheckerNameModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, taskCheckerNameModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskCheckerName(taskCheckerName, taskCheckerNameModifyRequest.getNewTaskCheckerName());
    }

}
