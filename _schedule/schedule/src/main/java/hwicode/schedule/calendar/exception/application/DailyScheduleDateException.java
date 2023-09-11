package hwicode.schedule.calendar.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyScheduleDateException extends BusinessException {

    public DailyScheduleDateException() {
        super("계획표는 당일에만 생성할 수 있습니다.", HttpStatus.BAD_REQUEST);
    }
}
