package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.*;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
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
    public void saveTask(Long dailyChecklistId, Task task) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);

        dailyChecklist.addTask(task);
        taskSaveOnlyRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long dailyChecklistId, String taskName) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.deleteTask(taskName);
    }

    @Transactional
    public void changeTaskStatus(Long dailyChecklistId, String taskName, Status status) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.changeTaskStatus(taskName, status);
    }

    @Transactional
    public void changeTaskDifficulty(Long dailyChecklistId, String taskName, Difficulty difficulty) {
        DailyChecklist dailyChecklist = findDailyChecklistWithTasks(dailyChecklistRepository, dailyChecklistId);
        dailyChecklist.changeTaskDifficulty(taskName, difficulty);
    }

}
