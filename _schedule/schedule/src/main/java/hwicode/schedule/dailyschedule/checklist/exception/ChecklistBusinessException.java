package hwicode.schedule.dailyschedule.checklist.exception;

import org.springframework.http.HttpStatus;

public abstract class ChecklistBusinessException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected ChecklistBusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
