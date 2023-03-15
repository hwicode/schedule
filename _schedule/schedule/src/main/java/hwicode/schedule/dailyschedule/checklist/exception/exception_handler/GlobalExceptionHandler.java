package hwicode.schedule.dailyschedule.checklist.exception.exception_handler;

import hwicode.schedule.dailyschedule.checklist.exception.ChecklistBusinessException;
import hwicode.schedule.dailyschedule.checklist.exception.exception_handler.ErrorResponse.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ChecklistBusinessException.class)
    protected ResponseEntity<Object> handleChecklistBusinessException(ChecklistBusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ErrorResponse(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.INVALID_MESSAGE_BODY_TYPE;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(globalErrorCode.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, globalErrorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(MethodArgumentNotValidException e, GlobalErrorCode globalErrorCode) {
        return ResponseEntity.status(globalErrorCode.getHttpStatus())
                .body(makeErrorResponse(e, globalErrorCode));
    }

    private ErrorResponse makeErrorResponse(MethodArgumentNotValidException e, GlobalErrorCode globalErrorCode) {
        List<ValidationError> validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationError::new)
                .collect(Collectors.toList());

        return new ErrorResponse(globalErrorCode.getMessage(), validationErrors);
    }

}
