package hwicode.schedule.calendar.exception.domain.calendar;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CalendarGoalNotFoundException extends BusinessException {

    public CalendarGoalNotFoundException() {
        super("캘린더의 목표를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
