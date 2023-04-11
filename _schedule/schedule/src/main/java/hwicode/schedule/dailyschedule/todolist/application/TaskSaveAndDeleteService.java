package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.TaskSaveRequest;

public interface TaskSaveAndDeleteService {
    Long save(Long dailyToDoListId, TaskSaveRequest taskSaveRequest);
    void delete(Long dailyChecklistId, String taskName);
}
