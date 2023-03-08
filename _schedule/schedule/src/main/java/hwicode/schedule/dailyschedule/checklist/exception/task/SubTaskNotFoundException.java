package hwicode.schedule.dailyschedule.checklist.exception.task;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;

public class SubTaskNotFoundException extends ChecklistBusinessException {

    public SubTaskNotFoundException() {
        super("서브 과제를 찾을 수 없습니다.");
    }
}
