package hwicode.schedule.calendar.exception.domain.goal;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubGoalNotFoundException extends BusinessException {

    public SubGoalNotFoundException() {
        super("서브 목표를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
