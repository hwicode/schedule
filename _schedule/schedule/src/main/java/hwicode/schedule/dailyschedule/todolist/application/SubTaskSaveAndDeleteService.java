package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.delete.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.presentation.subtask.dto.save.SubTaskSaveRequest;

public interface SubTaskSaveAndDeleteService {
    Long save(SubTaskSaveRequest subTaskSaveRequest);
    void delete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest);
}
