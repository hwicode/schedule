package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.dailyschedule_domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTaskCheckers;

@RequiredArgsConstructor
@Service
public class TaskCheckerService {

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
    public String changeTaskName(Long dailyChecklistId, String taskName, String newTaskName) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTaskCheckers(
                dailyChecklistRepository, dailyChecklistId);

        return dailyChecklist.changeTaskCheckerName(taskName, newTaskName);
    }

}
