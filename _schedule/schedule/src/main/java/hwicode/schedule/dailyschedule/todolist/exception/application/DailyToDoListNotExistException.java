package hwicode.schedule.dailyschedule.todolist.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyToDoListNotExistException extends BusinessException {

    public DailyToDoListNotExistException() {
        super("투두 리스트가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
