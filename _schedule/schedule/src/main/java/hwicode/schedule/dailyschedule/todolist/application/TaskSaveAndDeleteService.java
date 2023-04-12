package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.TaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.TaskSaveRequest;

public interface TaskSaveAndDeleteService {
    Long save(TaskSaveRequest taskSaveRequest);
    void delete(String taskName, TaskDeleteRequest taskDeleteRequest);
}
