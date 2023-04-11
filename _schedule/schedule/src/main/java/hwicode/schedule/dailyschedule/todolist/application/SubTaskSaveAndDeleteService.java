package hwicode.schedule.dailyschedule.todolist.application;

public interface SubTaskSaveAndDeleteService {
    Long save(SubTaskSaveRequest subTaskSaveRequest);
    void delete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest);
}
