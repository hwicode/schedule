package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.presentation.subtask.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.save.SubTaskSaveRequest;

public interface SubTaskSaveAndDeleteService {
    Long save(SubTaskSaveRequest subTaskSaveRequest);
    void delete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest);
}
