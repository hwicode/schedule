package hwicode.schedule.calendar.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class YearMonthNullException extends BusinessException {

    public YearMonthNullException() {
        super("존재하지 않는 년, 달을 입력하셨습니다.", HttpStatus.BAD_REQUEST);
    }
}
