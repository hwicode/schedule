package hwicode.schedule.dailyschedule.checklist.exception.domain.dailychecklist;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyChecklistForbiddenException extends BusinessException {

    public DailyChecklistForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}
