package hwicode.schedule.dailyschedule.checklist.exception.task;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;

public class SubTaskNotAllDoneException extends ChecklistBusinessException {

    public SubTaskNotAllDoneException() {
        super("서브 과제가 전부 DONE 상태가 아닙니다.");
    }
}
