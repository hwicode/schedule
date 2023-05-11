package hwicode.schedule.dailyschedule.timetable.exception.domain.timetablevalidator;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidDateValidException extends BusinessException {

    public InvalidDateValidException() {
        super("계획표의 날짜 또는 그 다음 날까지만 허용됩니다.", HttpStatus.BAD_REQUEST);
    }
}
