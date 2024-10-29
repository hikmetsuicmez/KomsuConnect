package com.hikmetsuicmez.komsu_connect.handler;

import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidationException(WebRequest request,MethodArgumentNotValidException ex) throws UnknownHostException {

        return new ResponseEntity<>(createValidationErrors(ex.getMessage(), request, ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(WebRequest request, UserNotFoundException ex) throws UnknownHostException {

        return new ResponseEntity<>(createNotFoundErrors(ex.getMessage(), request, ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex, WebRequest request) throws UnknownHostException {
        Map<String, String> errors = Map.of("error", "An unexpected error occurred.");
        ErrorResponse<Object> errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .error("Internal Error")
                .hostName(getHostName())
                .messages(errors)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public <E> ErrorResponse<E> createNotFoundErrors(E message, WebRequest request, UserNotFoundException ex) throws UnknownHostException {
        Map<String, String> errors = Map.of("error", ex.getMessage());

        return ErrorResponse.<E>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .error("Not Found Error")
                .hostName(getHostName())
                .messages(errors)
                .build();

    }

    public <E> ErrorResponse<E> createValidationErrors(E message, WebRequest request, MethodArgumentNotValidException ex) throws UnknownHostException {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ErrorResponse.<E>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .error("Validation Error")
                .hostName(getHostName())
                .messages(errors)
                .build();
    }

    private String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }


}
