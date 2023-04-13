package hwicode.schedule.dailyschedule.todolist.exception.domain.task;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskNameDuplicationException extends BusinessException {

    public SubTaskNameDuplicationException() {
        super("서브 과제의 이름이 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
