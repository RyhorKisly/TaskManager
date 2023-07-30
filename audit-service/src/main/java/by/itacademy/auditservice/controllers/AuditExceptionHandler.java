package by.itacademy.auditservice.controllers;

import by.itacademy.auditservice.core.enums.ErrorType;
import by.itacademy.auditservice.core.errors.ErrorMessage;
import by.itacademy.auditservice.core.errors.ErrorResponse;
import by.itacademy.auditservice.core.errors.StructuredErrorResponse;
import by.itacademy.auditservice.core.exceptions.FindEntityException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@RestControllerAdvice
public class AuditExceptionHandler {

//    Если в интерфейсе сервиса проставить @Valid и неверные данные ввести в dto
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleInvalidArgument(ConstraintViolationException ex) {
        StructuredErrorResponse response = new StructuredErrorResponse(ErrorType.structured_error, new ArrayList<>());
        response.setLogref(ErrorType.structured_error);

        ex.getConstraintViolations().stream().forEach(violation -> {
            response.getErrors().add(new ErrorMessage(violation.getPropertyPath().toString(), violation.getMessage()));
        });

        LOGGER.error(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //    Если в json неверные данные передать в дто в соответствии с валидацией
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidArgument(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getFieldErrors();
        StructuredErrorResponse response = new StructuredErrorResponse(ErrorType.structured_error, new ArrayList<>());

        for (FieldError error : errors) {
            response.getErrors().add( new ErrorMessage(error.getField(), error.getDefaultMessage()));
        }

        LOGGER.error(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleBadRequest(HttpMessageConversionException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setLogref(ErrorType.error);
        response.setMessage("The request contains incorrect data. Change request and try again or contact support!");
        LOGGER.error(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class,      // Если сработал constraint form db
            FindEntityException.class
    })
    public ResponseEntity<?> handleInvalidArgument(RuntimeException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setLogref(ErrorType.error);
        response.setMessage(ex.getMessage());
        LOGGER.error(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Если передать неверный тип данных в параметры
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleInvalidArgument(MethodArgumentTypeMismatchException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setLogref(ErrorType.error);
        response.setMessage("Incorrect characters. Change request and try it again!");
        LOGGER.error(ex.getMessage(), ex);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<?> handleInnerError(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                ErrorType.error,
                "Internal server Error. Please, contact support!"
        );
        LOGGER.error(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}