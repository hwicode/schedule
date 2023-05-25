package hwicode.schedule.calendar.exception.domain.calendar;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CalendarGoalDuplicateException extends BusinessException {

    public CalendarGoalDuplicateException() {
        super("목표가 중복되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
