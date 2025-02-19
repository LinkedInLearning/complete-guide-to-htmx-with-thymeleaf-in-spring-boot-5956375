package com.mdrsolutions.records_management.handler;

import com.mdrsolutions.records_management.PersonDoesNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle PersonDoesNotExistException
    @ExceptionHandler(PersonDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePersonDoesNotExistException(PersonDoesNotExistException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/person-not-found"; // Custom error view for PersonDoesNotExistException
    }

    // Handle IllegalArgumentException specifically
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        LOGGER.warn("Illegal argument: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error"; // Return generic error view or message
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        if (ex instanceof jakarta.validation.ConstraintViolationException ||
                ex instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
            // Let the controller handle these exceptions by rethrowing them
            throw new RuntimeException(ex);
        }
        LOGGER.error("An unexpected error occurred: ", ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error"); // Return generic error view
        return mav;
    }

    // Add more specific exception handlers as needed
}
