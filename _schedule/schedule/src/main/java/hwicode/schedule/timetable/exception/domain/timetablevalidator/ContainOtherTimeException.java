package hwicode.schedule.timetable.exception.domain.timetablevalidator;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ContainOtherTimeException extends BusinessException {

    public ContainOtherTimeException() {
        super("다른 학습 시간 사이에 끼어들 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
