package hwicode.schedule.timetable.exception.domain.subject_of_subtask;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubjectOfSubTaskForbiddenException extends BusinessException {

    public SubjectOfSubTaskForbiddenException() {
        super("직접 생성한 사용자만 접근할 수 있습니다", HttpStatus.FORBIDDEN);
    }
}