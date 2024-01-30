package hwicode.schedule.calendar.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DailyScheduleExistException extends BusinessException {

    public DailyScheduleExistException() {
        super("계획표가 이미 존재합니다.", HttpStatus.BAD_REQUEST);
    }
}
