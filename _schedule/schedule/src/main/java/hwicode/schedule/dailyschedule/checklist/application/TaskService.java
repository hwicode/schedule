package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.presentation.TaskSaveRequest;
import hwicode.schedule.dailyschedule.checklist.presentation.TaskStatusModifyRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hwicode.schedule.dailyschedule.checklist.application.DailyChecklistFindService.findDailyChecklistWithTasks;

@Service
public class TaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final TaskSaveOnlyRepository taskSaveOnlyRepository;

    public TaskService(DailyChecklistRepository dailyChecklistRepository, TaskSaveOnlyRepository taskSaveOnlyRepository) {
        this.dailyChecklistRepository = dailyChecklistRepository;
        this.taskSaveOnlyRepository = taskSaveOnlyRepository;
    }

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
    public void changeTaskDifficulty(Long dailyChecklistId, String taskName, Difficulty difficulty) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.changeTaskDifficulty(taskName, difficulty);
    }

}
