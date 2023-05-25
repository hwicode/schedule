package hwicode.schedule.calendar.exception.domain.goal;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubGoalDuplicateException extends BusinessException {

    public SubGoalDuplicateException() {
        super("서브 목표가 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
