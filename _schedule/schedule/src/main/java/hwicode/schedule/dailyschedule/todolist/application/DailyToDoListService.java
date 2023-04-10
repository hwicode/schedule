package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.domain.DailyToDoList;
import hwicode.schedule.dailyschedule.todolist.domain.Task;
import hwicode.schedule.dailyschedule.todolist.domain.TaskSaveService;
import hwicode.schedule.dailyschedule.todolist.infra.DailyToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DailyToDoListService {

    private final DailyToDoListRepository dailyToDoListRepository;
    private final TaskSaveService taskSaveService;

    @Transactional
    public Long saveTask(Long dailyToDoListId, TaskSaveRequest taskSaveRequest) {
        DailyToDoList dailyToDoList = dailyToDoListRepository.findToDoListWithTasks(dailyToDoListId)
                .orElseThrow(IllegalArgumentException::new);

        Task task = dailyToDoList.createTask(taskSaveRequest.toTaskCreateDto());
        return taskSaveService.save(task)
                .getId();
    }
}
