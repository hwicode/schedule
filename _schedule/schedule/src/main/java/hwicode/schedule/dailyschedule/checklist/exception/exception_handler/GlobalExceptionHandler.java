package hwicode.schedule.dailyschedule.checklist.exception.exception_handler;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import hwicode.schedule.dailyschedule.checklist.exception.exception_handler.ErrorBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChecklistBusinessException.class)
    protected ResponseEntity<ErrorBody> handleChecklistBusinessException(ChecklistBusinessException checklistBusinessException) {
        return new ResponseEntity<>(
                new ErrorBody(checklistBusinessException.getMessage()),
                checklistBusinessException.getHttpStatus());
    }
}
