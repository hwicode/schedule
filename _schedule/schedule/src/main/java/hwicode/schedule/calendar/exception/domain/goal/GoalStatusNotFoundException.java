package hwicode.schedule.calendar.exception.domain.goal;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class GoalStatusNotFoundException extends BusinessException {

    public GoalStatusNotFoundException() {
        super("존재하지 않는 목표의 진행 상태입니다.", HttpStatus.BAD_REQUEST);
    }
}
