package com.github.ibole.infrastructure.web.spring;

import com.github.ibole.infrastructure.common.exception.MoreThrowables;
import com.github.ibole.infrastructure.common.exception.MyResourceNotFoundException;
import com.github.ibole.infrastructure.common.exception.UnauthorizedException;
import com.github.ibole.infrastructure.web.exception.ApiError;
import com.github.ibole.infrastructure.web.exception.ValidationErrorDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 异常处理器,该类会处理所有在执行标有@RequestMapping注解的方法时发生的异常.
 * 
 * @author bwang
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    public RestResponseEntityExceptionHandler() {
        super();
    }

    // API

    // 400

    @Override
    protected final ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info("Bad Request: {}", ex.getMessage());
        log.debug("Bad Request: ", ex);

        final ApiError apiError = message(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected final ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info("Bad Request: {}", ex.getMessage());
        log.debug("Bad Request: ", ex);

        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        final ValidationErrorDTO dto = processFieldErrors(fieldErrors);

        return handleExceptionInternal(ex, dto, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class, ValidationException.class })
    public final ResponseEntity<Object> handleBadRequest(final ValidationException ex, final WebRequest request) {
        log.info("Bad Request: {}", ex.getLocalizedMessage());
        log.debug("Bad Request: ", ex);

        final ApiError apiError = message(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 401

    @ExceptionHandler({ UnauthorizedException.class })
    public ResponseEntity<Object> handleUnauthorization(final UnauthorizedException ex, final WebRequest request) {
        logger.error("401 Status Code", ex);

        final ApiError apiError = message(HttpStatus.UNAUTHORIZED, ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }    
    
    // 403

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleEverything(final AccessDeniedException ex, final WebRequest request) {
        logger.error("403 Status Code", ex);

        final ApiError apiError = message(HttpStatus.FORBIDDEN, ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    // 404

    @ExceptionHandler({ MyResourceNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        log.warn("Not Found: {}", ex.getMessage());

        final ApiError apiError = message(HttpStatus.NOT_FOUND, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 409

//    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class, MyConflictException.class })
//    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
//        log.warn("Conflict: {}", ex.getMessage());
//
//        final ApiError apiError = message(HttpStatus.CONFLICT, ex);
//        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }

    // 415

    @ExceptionHandler({ InvalidMimeTypeException.class, InvalidMediaTypeException.class })
    protected ResponseEntity<Object> handleInvalidMimeTypeException(final IllegalArgumentException ex, final WebRequest request) {
        log.warn("Unsupported Media Type: {}", ex.getMessage());

        final ApiError apiError = message(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    // 500

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handle500s(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);

        final ApiError apiError = message(HttpStatus.INTERNAL_SERVER_ERROR, ex);

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // UTIL

    private ValidationErrorDTO processFieldErrors(final List<FieldError> fieldErrors) {
        final ValidationErrorDTO dto = new ValidationErrorDTO();

        for (final FieldError fieldError : fieldErrors) {
            final String localizedErrorMessage = fieldError.getDefaultMessage();
            dto.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return dto;
    }

    private ApiError message(final HttpStatus httpStatus, final Exception ex) {
        final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = MoreThrowables.getRootCauseStackTraceAsString(ex);
        return new ApiError(httpStatus.value(), message, devMessage);
    }

}
