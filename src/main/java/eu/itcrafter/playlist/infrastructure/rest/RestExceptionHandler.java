package eu.itcrafter.playlist.infrastructure.rest;

import eu.itcrafter.playlist.infrastructure.rest.error.ApiError;
import eu.itcrafter.playlist.infrastructure.rest.exception.DataNotFoundException;
import eu.itcrafter.playlist.infrastructure.rest.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class watches for exceptions thrown by REST controllers
 * and turns them into friendly JSON error responses.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles cases where a user is not allowed to perform an action.
     * Produces a 403 Forbidden response with an error message and request path.
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException exception, HttpServletRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.FORBIDDEN);
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles cases where requested data cannot be found.
     * Produces a 404 Not Found response with an error message and request path.
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiError> handleDataNotFoundException(DataNotFoundException exception, HttpServletRequest request) {

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND);
        apiError.setMessage(exception.getMessage());
        apiError.setPath(request.getRequestURI());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation failures on method arguments.
     * Pulls out the first validation error, then returns a 400 Bad Request
     * response with a simple message and the request path.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest webRequest) {

        FieldError firstError = exception.getBindingResult()
                .getFieldErrors()
                .get(0);

        ServletWebRequest servletReq = (ServletWebRequest) webRequest;
        String path = servletReq.getRequest().getRequestURI();

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setMessage(firstError.getField() + ": " + firstError.getDefaultMessage());
        apiError.setPath(path);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
