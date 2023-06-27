package hwicode.schedule.timetable.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TimeTableNotFoundException extends BusinessException {

    public TimeTableNotFoundException() {
        super("타임 테이블을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
