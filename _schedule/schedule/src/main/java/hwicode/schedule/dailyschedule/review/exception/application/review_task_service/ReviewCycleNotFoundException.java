package hwicode.schedule.dailyschedule.review.exception.application.review_task_service;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewCycleNotFoundException extends BusinessException {

    public ReviewCycleNotFoundException() {
        super("복습 주기가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
