package hwicode.schedule.dailyschedule.review.exception.application.review_task_service;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewTaskNotFoundException extends BusinessException {

    public ReviewTaskNotFoundException() {
        super("복습할 과제가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
