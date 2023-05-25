package hwicode.schedule.calendar.exception.domain.calendar;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class WeeklyDateNotValidException extends BusinessException {

    public WeeklyDateNotValidException() {
        super("일주일동안 가능한 일수가 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
