package hwicode.schedule.dailyschedule.todolist.application;

import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskDeleteRequest;
import hwicode.schedule.dailyschedule.todolist.application.dto.SubTaskSaveRequest;

public interface SubTaskSaveAndDeleteService {
    Long save(SubTaskSaveRequest subTaskSaveRequest);
    void delete(String subTaskName, SubTaskDeleteRequest subTaskDeleteRequest);
}
