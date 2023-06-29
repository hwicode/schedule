package hwicode.schedule.dailyschedule.todolist.exception.domain.review_cycle;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ReviewCycleNullException extends BusinessException {

    public ReviewCycleNullException() {
        super("존재하지 않는 복습 주기를 입력했습니다.", HttpStatus.BAD_REQUEST);
    }
}
