package hwicode.schedule.calendar.exception.domain.goal;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class GoalForbiddenException extends BusinessException {

    public GoalForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}
