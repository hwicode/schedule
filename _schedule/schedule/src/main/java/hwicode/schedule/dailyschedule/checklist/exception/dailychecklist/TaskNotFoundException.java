package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;

public class TaskNotFoundException extends ChecklistBusinessException {

    public TaskNotFoundException() {
        super("과제를 찾을 수 없습니다.");
    }
}
