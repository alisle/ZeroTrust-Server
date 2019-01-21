package com.notrust.server.web;


import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.exception.InsertFailedException;
import com.notrust.server.exception.InvalidIPAddress;
import com.notrust.server.exception.NoConnectionFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {InsertFailedException.class})
    protected ResponseEntity<Object> handleInsertFailedException(InsertFailedException exception) {
        return new ResponseEntity<>("Insert failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NoConnectionFoundException.class})
    protected ResponseEntity<Object> handleNoConnectionFoundException(NoConnectionFoundException exception) {
        return new ResponseEntity<>("No Connection Found!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AgentNotFoundException.class})
    protected ResponseEntity<Object> handleAgentNotFoundException(AgentNotFoundException exception) {
        return new ResponseEntity<>("Agent not Found!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidIPAddress.class})
    protected ResponseEntity<Object> handleInvalidIPAddressException(InvalidIPAddress exception) {
        return new ResponseEntity<>("Invalid IP Address!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
