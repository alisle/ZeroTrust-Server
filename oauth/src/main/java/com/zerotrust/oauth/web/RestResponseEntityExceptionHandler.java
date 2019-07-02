package com.zerotrust.oauth.web;

import com.zerotrust.oauth.exception.InvalidRoleException;
import com.zerotrust.oauth.exception.NoSuchUserException;
import com.zerotrust.oauth.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.management.relation.InvalidRelationIdException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoSuchUserException.class})
    protected ResponseEntity<Object> handleNoSuchUserException(NoSuchUserException exception) {
        return new ResponseEntity<>("Unable to find user", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value =  {UserAlreadyExistsException.class})
    protected ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException exception) {
        return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidRoleException.class})
    protected ResponseEntity<Object> handleInvalidRoleException(InvalidRoleException exception){
        return new ResponseEntity<>(exception.getRole() + " doesn't exist", HttpStatus.FAILED_DEPENDENCY);
    }
}
