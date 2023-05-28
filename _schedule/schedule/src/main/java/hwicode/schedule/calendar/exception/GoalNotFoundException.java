package hwicode.schedule.calendar.exception;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class GoalNotFoundException extends BusinessException {

    public GoalNotFoundException() {
        super("목표를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
