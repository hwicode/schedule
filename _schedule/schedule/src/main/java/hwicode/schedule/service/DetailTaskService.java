package hwicode.schedule.service;

import hwicode.schedule.domain.DetailTask;
import hwicode.schedule.domain.Task;
import hwicode.schedule.repository.DetailTaskRepository;
import hwicode.schedule.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class DetailTaskService {

    private final DetailTaskRepository detailTaskRepository;
    private final TaskRepository taskRepository;

    public DetailTaskService(DetailTaskRepository detailTaskRepository, TaskRepository taskRepository) {
        this.detailTaskRepository = detailTaskRepository;
        this.taskRepository = taskRepository;
    }

    public Long addDetailTask(Long taskId, String name) {
        Task task = taskRepository.findOne(taskId);

        DetailTask detailTask = DetailTask.createDetailTask(task, name);

        detailTaskRepository.save(detailTask);

        return detailTask.getId();
    }

    public DetailTask findById(Long id) {
        return detailTaskRepository.findOne(id);
    }
}
