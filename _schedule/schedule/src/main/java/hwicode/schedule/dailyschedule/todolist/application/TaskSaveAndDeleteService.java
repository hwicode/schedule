package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.delete.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.task.dto.save.TaskSaveRequest;

public interface TaskSaveAndDeleteService {
    Long save(TaskSaveRequest taskSaveRequest);
    void delete(String taskName, TaskDeleteRequest taskDeleteRequest);
}
