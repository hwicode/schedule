package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.difficulty_modify.TaskDifficultyModifyRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.save.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.task_dto.status_modify.TaskStatusModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTasks;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final TaskSaveOnlyRepository taskSaveOnlyRepository;

    @Transactional
    public Long saveTask(TaskSaveRequest taskSaveRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, taskSaveRequest.getDailyChecklistId());

        Task task = taskSaveRequest.toEntity();
        dailyChecklist.addTask(task);

        return taskSaveOnlyRepository.save(task)
                .getId();
    }

    @Transactional
    public void deleteTask(Long dailyChecklistId, String taskName) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.deleteTask(taskName);
    }

    @Transactional
    public Status changeTaskStatus(String taskName, TaskStatusModifyRequest taskStatusModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, taskStatusModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskStatus(taskName, taskStatusModifyRequest.getStatus());
    }

    @Transactional
    public Difficulty changeTaskDifficulty(String taskName, TaskDifficultyModifyRequest taskDifficultyModifyRequest) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(
                dailyChecklistRepository, taskDifficultyModifyRequest.getDailyChecklistId());

        return dailyChecklist.changeTaskDifficulty(taskName, taskDifficultyModifyRequest.getDifficulty());
    }

}
