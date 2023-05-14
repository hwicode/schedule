package hwicode.schedule.dailyschedule.timetable.exception.application;

import hwicode.schedule.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SubjectOfTaskNotFoundException extends BusinessException {

    public SubjectOfTaskNotFoundException() {
        super("과제의 학습 주제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
