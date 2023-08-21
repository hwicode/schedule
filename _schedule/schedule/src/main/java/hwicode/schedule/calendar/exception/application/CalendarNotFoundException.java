package hwicode.schedule.calendar.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CalendarNotFoundException extends BusinessException {

    public CalendarNotFoundException() {
        super("캘린더를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
