package com.mdrsolutions.records_management.handler;

import com.mdrsolutions.records_management.PersonDoesNotExistException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle PersonDoesNotExistException
    @ExceptionHandler(PersonDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePersonDoesNotExistException(PersonDoesNotExistException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/person-not-found";
    }

    // Handle IllegalArgumentException specifically
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        LOGGER.warn("Illegal argument: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    // Handle 404 errors
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        return "error/404";
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) throws Exception {
        if (ex instanceof ConstraintViolationException ||
                ex instanceof MethodArgumentNotValidException ||
                ex instanceof NoHandlerFoundException) {
            // Re-throw the exception to let other handlers process it
            throw ex;
        }

        LOGGER.error("An unexpected error occurred: ", ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }

    // Add more specific exception handlers as needed
}
