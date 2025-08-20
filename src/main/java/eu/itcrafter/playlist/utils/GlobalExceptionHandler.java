package eu.itcrafter.playlist.utils;

import eu.itcrafter.playlist.utils.exceptions.DatabaseConstraintException;
import eu.itcrafter.playlist.utils.exceptions.DatabaseNameConflictException;
import eu.itcrafter.playlist.utils.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                request.getRequestURI());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(DatabaseNameConflictException.class)
    protected ResponseEntity<ApiError>handleDatabaseNameConflict(DatabaseNameConflictException ex, HttpServletRequest request) {
        ApiError apiError =new ApiError(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()

        );
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(DatabaseConstraintException.class)
    protected ResponseEntity<ApiError> handleDataBaseConstraint(DatabaseConstraintException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}
