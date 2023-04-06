package hwicode.schedule.dailyschedule.checklist.exception.task;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNotAllTodoException extends ChecklistBusinessException {

    public SubTaskCheckerNotAllTodoException() {
        super("서브 과제가 전부 TODO 상태가 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}