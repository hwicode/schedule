package hwicode.schedule.dailyschedule.review.exception.application.review_task_service;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewListNotFoundException extends BusinessException {

    public ReviewListNotFoundException() {
        super("계획표가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
