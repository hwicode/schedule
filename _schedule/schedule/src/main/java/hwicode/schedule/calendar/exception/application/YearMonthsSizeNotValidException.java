package hwicode.schedule.calendar.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class YearMonthsSizeNotValidException extends BusinessException {

    public YearMonthsSizeNotValidException() {
        super("목표의 기간을 정할 때, 24개월을 넘길 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
