package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;

public class StatusNotFoundException extends ChecklistBusinessException {

    public StatusNotFoundException() {
        super("알 수 없는 진행 상태입니다.");
    }
}
