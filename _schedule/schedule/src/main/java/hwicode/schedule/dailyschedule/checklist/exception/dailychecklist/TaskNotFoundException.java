package hwicode.schedule.dailyschedule.checklist.exception.dailychecklist;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends ChecklistBusinessException {

    public TaskNotFoundException() {
        super("과제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
