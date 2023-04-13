package hwicode.schedule.dailyschedule.todolist.exception.domain.dailytodolist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends BusinessException {

    public TaskNotFoundException() {
        super("과제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
