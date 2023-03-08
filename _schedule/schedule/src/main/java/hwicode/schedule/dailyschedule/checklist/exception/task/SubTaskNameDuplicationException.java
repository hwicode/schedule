package hwicode.schedule.dailyschedule.checklist.exception.task;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;

public class SubTaskNameDuplicationException extends ChecklistBusinessException {

    public SubTaskNameDuplicationException() {
        super("서브 과제의 이름이 중복되었습니다.");
    }
}
