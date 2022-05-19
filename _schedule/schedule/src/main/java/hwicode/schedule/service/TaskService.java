package hwicode.schedule.service;

import hwicode.schedule.controller.request.TaskRequest;
import hwicode.schedule.domain.Schedule;
import hwicode.schedule.domain.Task;
import hwicode.schedule.repository.ScheduleRepository;
import hwicode.schedule.repository.TaskRepository;
import hwicode.schedule.service.response.TaskIdResponse;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ScheduleRepository scheduleRepository;

    public TaskService(TaskRepository taskRepository, ScheduleRepository scheduleRepository) {
        this.taskRepository = taskRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public TaskIdResponse addTask(Long scheduleId, TaskRequest taskRequest) {
        Schedule schedule = scheduleRepository.findOne(scheduleId);

        Task task = taskRequest.toEntity();
        task.setSchedule(schedule);

        taskRepository.save(task);

        return new TaskIdResponse(task.getId());
    }

    public Task findById(Long id) {
        return taskRepository.findOne(id);
    }
}
