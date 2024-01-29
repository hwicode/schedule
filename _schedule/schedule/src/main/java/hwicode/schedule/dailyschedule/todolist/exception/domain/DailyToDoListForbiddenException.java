package hwicode.schedule.dailyschedule.todolist.exception.domain;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyToDoListForbiddenException extends BusinessException {

    public DailyToDoListForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}
