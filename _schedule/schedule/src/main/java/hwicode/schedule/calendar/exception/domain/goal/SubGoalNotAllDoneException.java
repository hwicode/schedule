package hwicode.schedule.calendar.exception.domain.goal;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubGoalNotAllDoneException extends BusinessException {

    public SubGoalNotAllDoneException() {
        super("서브 목표가 전부 DONE 상태가 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
