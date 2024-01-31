package hwicode.schedule.timetable.exception.domain.learningtime;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class LearningTimeForbiddenException extends BusinessException {

    public LearningTimeForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}