package hwicode.schedule.dailyschedule.review.exception.domain.review_date;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewDateForbiddenException extends BusinessException {

    public ReviewDateForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}
