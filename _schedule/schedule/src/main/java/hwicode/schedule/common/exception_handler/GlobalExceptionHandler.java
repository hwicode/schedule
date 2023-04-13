package hwicode.schedule.common.exception_handler;

import hwicode.schedule.common.exception_handler.ErrorResponse.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Message : {}";
    private static final String CUSTOM_LOG_FORMAT = "Class : {}, Message : {}, CustomMessage : {}";

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        log.info(CUSTOM_LOG_FORMAT,
                ex.getClass().getSimpleName(),
                null,
                ex.getMessage());

        return ResponseEntity.status(ex.getHttpStatus())
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(Exception ex) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;

        log.error(CUSTOM_LOG_FORMAT,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                globalErrorCode.getMessage());

        return ResponseEntity.status(globalErrorCode.getHttpStatus())
                .body(new ErrorResponse(globalErrorCode.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        log.info(LOG_FORMAT,
                ex.getClass().getSimpleName(),
                ex.getMessage());

        return ResponseEntity.status(status)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        log.info(LOG_FORMAT,
                ex.getClass().getSimpleName(),
                ex.getMessage());

        return ResponseEntity.status(status)
                .body(null);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.NOT_FOUND;
        return ResponseEntity.status(globalErrorCode.getHttpStatus())
                .body(new ErrorResponse(globalErrorCode.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.INVALID_MESSAGE_BODY_TYPE;

        log.info(CUSTOM_LOG_FORMAT,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                globalErrorCode.getMessage());

        return ResponseEntity.status(status)
                .body(new ErrorResponse(globalErrorCode.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        GlobalErrorCode globalErrorCode = GlobalErrorCode.INVALID_PARAMETER;

        log.info(CUSTOM_LOG_FORMAT,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                globalErrorCode.getMessage());

        return handleExceptionInternal(ex, globalErrorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(MethodArgumentNotValidException ex, GlobalErrorCode globalErrorCode) {
        return ResponseEntity.status(globalErrorCode.getHttpStatus())
                .body(makeErrorResponse(ex, globalErrorCode));
    }

    private ErrorResponse makeErrorResponse(MethodArgumentNotValidException ex, GlobalErrorCode globalErrorCode) {
        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationError::new)
                .collect(Collectors.toList());

        return new ErrorResponse(globalErrorCode.getMessage(), validationErrors);
    }

}
