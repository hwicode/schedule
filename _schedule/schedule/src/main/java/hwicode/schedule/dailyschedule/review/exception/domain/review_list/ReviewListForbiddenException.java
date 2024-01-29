package hwicode.schedule.dailyschedule.review.exception.domain.review_list;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewListForbiddenException extends BusinessException {

    public ReviewListForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}
