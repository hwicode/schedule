package hwicode.schedule.calendar.exception.domain.goal;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubGoalNotAllTodoException extends BusinessException {

    public SubGoalNotAllTodoException() {
        super("서브 목표가 전부 TODO 상태가 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
