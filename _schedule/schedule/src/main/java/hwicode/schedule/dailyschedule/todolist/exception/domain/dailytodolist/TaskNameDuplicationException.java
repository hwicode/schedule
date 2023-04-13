package hwicode.schedule.dailyschedule.todolist.exception.domain.dailytodolist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TaskNameDuplicationException extends BusinessException {

    public TaskNameDuplicationException() {
        super("과제의 이름이 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
