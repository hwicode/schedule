package hwicode.schedule.dailyschedule.todolist.application;

public interface TaskSaveService {
    Long save(Long dailyToDoListId, TaskSaveRequest taskSaveRequest);
}
