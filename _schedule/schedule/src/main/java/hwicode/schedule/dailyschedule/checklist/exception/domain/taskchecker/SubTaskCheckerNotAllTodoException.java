package hwicode.schedule.dailyschedule.checklist.exception.domain.taskchecker;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubTaskCheckerNotAllTodoException extends BusinessException {

    public SubTaskCheckerNotAllTodoException() {
        super("서브 과제가 전부 TODO 상태가 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
