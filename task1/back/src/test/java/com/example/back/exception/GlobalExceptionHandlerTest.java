package com.example.back.exception;

import com.example.back.dto.response.MessageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    public void testHandleValidationExceptions() {
        // Setup
        MethodArgumentNotValidException validationException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("user", "email", "Invalid email format"));
        fieldErrors.add(new FieldError("user", "name", "Name cannot be empty"));

        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));

        // Execute
        Map<String, String> errors = exceptionHandler.handleValidationExceptions(validationException);

        // Verify
        assertThat(errors).hasSize(2);
        assertThat(errors).containsEntry("email", "Invalid email format");
        assertThat(errors).containsEntry("name", "Name cannot be empty");
    }

    @Test
    public void testHandleBadCredentialsException() {
        // Setup
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");

        // Execute
        ResponseEntity<MessageResponse> response = exceptionHandler.handleBadCredentialsException(ex);

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid credentials");
    }

    @Test
    public void testHandleAllUncaughtException() {
        // Setup
        Exception ex = new Exception("Test exception");

        // Execute
        ResponseEntity<MessageResponse> response = exceptionHandler.handleAllUncaughtException(ex);

        // Verify
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).contains("Internal server error");
        assertThat(response.getBody().getMessage()).contains("Test exception");
    }

}