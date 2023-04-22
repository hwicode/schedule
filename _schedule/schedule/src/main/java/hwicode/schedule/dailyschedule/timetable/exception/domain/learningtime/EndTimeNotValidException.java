package hwicode.schedule.dailyschedule.timetable.exception.domain.learningtime;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class EndTimeNotValidException extends BusinessException {

    public EndTimeNotValidException() {
        super("끝나는 시간은 시작 시간보다 앞설 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
