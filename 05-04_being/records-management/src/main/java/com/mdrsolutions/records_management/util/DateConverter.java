package com.mdrsolutions.records_management.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConverter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Converts a String to LocalDate
    public static LocalDate convert(String date) {
        //short circuit conversion if string is empty
        if(date == null || date.isEmpty()){
            return null;
        }
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format is yyyy-MM-dd", e);
        }
    }

    // Converts a LocalDate to String
    public static String convert(LocalDate date) {
        if(null != date){
            return date.format(DATE_FORMATTER);
        } else {
            return "";
        }
    }

    // Example usage
    public static void main(String[] args) {
        DateConverter converter = new DateConverter();

        // Test conversion from String to LocalDate
        String dateString = "2024-12-08";
        LocalDate localDate = converter.convert(dateString);
        System.out.println("Converted LocalDate: " + localDate);

        // Test conversion from LocalDate to String
        String formattedDate = converter.convert(localDate);
        System.out.println("Formatted String: " + formattedDate);
    }
}
