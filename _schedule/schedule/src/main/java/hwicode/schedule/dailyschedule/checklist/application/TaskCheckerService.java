package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.save.TaskCheckerSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_checker.dto.status_modify.TaskStatusModifyRequest;
import hwicode.schedule.dailyschedule.common.domain.Difficulty;
import hwicode.schedule.dailyschedule.common.domain.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTasks;

@RequiredArgsConstructor
@Service
public class TaskCheckerService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final TaskCheckerSaveOnlyRepository taskCheckerSaveOnlyRepository;

    @Transactional
    public Long saveTask(TaskCheckerSaveRequest taskCheckerSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, taskCheckerSaveRequest.getDailyChecklistId());

        TaskChecker taskChecker = taskCheckerSaveRequest.toEntity();
        dailyChecklist.addTaskChecker(taskChecker);

        return taskCheckerSaveOnlyRepository.save(taskChecker)
                .getId();
    }

    @Transactional
    public void deleteTask(Long dailyChecklistId, String taskName) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.deleteTaskChecker(taskName);
    }

    @Transactional
    public TaskStatus changeTaskStatus(String taskName, TaskStatusModifyRequest taskStatusModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, taskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskStatus(taskName, taskStatusModifyRequest.getTaskStatus());
    }

    @Transactional
    public Difficulty changeTaskDifficulty(String taskName, TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, taskDifficultyModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeDifficulty(taskName, taskDifficultyModifyRequest.getDifficulty());
    }

}
