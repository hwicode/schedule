package hwicode.schedule.dailyschedule.todolist.exception.domain.review_cycle;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidReviewCycleDateException extends BusinessException {

    public InvalidReviewCycleDateException() {
        super("복습 주기의 날짜는 1과 60 사이의 값이여야 합니다.", HttpStatus.BAD_REQUEST);
    }
}
