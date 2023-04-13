package hwicode.schedule.dailyschedule.todolist.exception.domain.task;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskNotFoundException extends BusinessException {

    public SubTaskNotFoundException() {
        super("서브 과제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
