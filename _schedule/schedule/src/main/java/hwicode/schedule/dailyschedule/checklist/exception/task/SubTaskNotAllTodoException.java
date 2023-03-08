package hwicode.schedule.dailyschedule.checklist.exception.task;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;

public class SubTaskNotAllTodoException extends ChecklistBusinessException {

    public SubTaskNotAllTodoException() {
        super("서브 과제가 전부 TODO 상태가 아닙니다.");
    }
}
