package hwicode.schedule.dailyschedule.todolist.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskNotExistException extends BusinessException {

    public TaskNotExistException() {
        super("과제가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
