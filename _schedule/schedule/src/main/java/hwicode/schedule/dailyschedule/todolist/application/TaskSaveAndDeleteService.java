package hwicode.schedule.dailyschedule.todolist.application;

public interface TaskSaveAndDeleteService {
    Long save(Long dailyToDoListId, TaskSaveRequest taskSaveRequest);
    void delete(Long dailyChecklistId, String taskName);
}
