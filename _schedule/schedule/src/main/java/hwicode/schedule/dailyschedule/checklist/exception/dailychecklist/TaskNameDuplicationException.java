package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;

public class TaskNameDuplicationException extends ChecklistBusinessException {

    public TaskNameDuplicationException() {
        super("과제의 이름이 중복되었습니다.");
    }
}
