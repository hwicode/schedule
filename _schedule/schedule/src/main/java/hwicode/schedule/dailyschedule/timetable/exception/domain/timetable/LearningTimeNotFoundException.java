package hwicode.schedule.dailyschedule.timetable.exception.domain.timetable;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class LearningTimeNotFoundException extends BusinessException {

    public LearningTimeNotFoundException() {
        super("학습 시간를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
