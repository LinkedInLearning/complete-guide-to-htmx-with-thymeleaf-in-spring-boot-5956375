package com.mdrsolutions.records_management.handler;

import com.mdrsolutions.records_management.PersonDoesNotExistException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Handle 404 errors TODO - this was modifified in 04-06_end for testing
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        String htmx = request.getHeader("HX-Request");
        if ("true".equalsIgnoreCase(htmx)) {
            // For htmx requests, return a minimal response or a specific fragment
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<span class='text-danger'>Resource not found.</span>");
        }
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorMessage", "The page you are looking for does not exist.");
        return new ResponseEntity<>(mav.getModel(), HttpStatus.NOT_FOUND);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex, HttpServletRequest request) throws Exception {
        String htmx = request.getHeader("HX-Request");
        if ("true".equalsIgnoreCase(htmx)) {
            // For htmx requests, return a minimal error response
            ModelAndView mav = new ModelAndView("error/minimal-error");
            mav.addObject("message", ex.getMessage());
            return mav;
        }

        if (ex instanceof ConstraintViolationException ||
                ex instanceof MethodArgumentNotValidException ||
                ex instanceof NoHandlerFoundException) {
            // Re-throw the exception to let other handlers process it
            throw ex;
        }

        LOGGER.error("An unexpected error occurred: ", ex);

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        return mav;
    }


    // Add more specific exception handlers as needed
}