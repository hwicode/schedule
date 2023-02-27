package hwicode.schedule.dailyschedule.checklist.application;

import hwicode.schedule.dailyschedule.checklist.domain.DailyChecklist;
import hwicode.schedule.dailyschedule.checklist.domain.Difficulty;
import hwicode.schedule.dailyschedule.checklist.domain.Status;
import hwicode.schedule.dailyschedule.checklist.domain.Task;
import hwicode.schedule.dailyschedule.checklist.infra.DailyChecklistRepository;
import hwicode.schedule.dailyschedule.checklist.infra.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final DailyChecklistRepository dailyChecklistRepository;
    private final TaskRepository taskRepository;

    public TaskService(DailyChecklistRepository dailyChecklistRepository, TaskRepository taskRepository) {
        this.dailyChecklistRepository = dailyChecklistRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void saveTask(Long dailyChecklistId, Task task) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.addTask(task);

        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long dailyChecklistId, String taskName) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.deleteTask(taskName);
    }

    @Transactional
    public void changeTaskStatus(Long dailyChecklistId, String taskName, Status status) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.changeTaskStatus(taskName, status);
    }

    @Transactional
    public void changeTaskDifficulty(Long dailyChecklistId, String taskName, Difficulty difficulty) {
        DailyChecklist dailyChecklist = dailyChecklistRepository.findDailyChecklistWithTasks(dailyChecklistId)
                .orElseThrow();

        dailyChecklist.changeTaskDifficulty(taskName, difficulty);
    }

}
