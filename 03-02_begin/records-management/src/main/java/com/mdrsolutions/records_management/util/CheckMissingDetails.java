package com.mdrsolutions.records_management.util;

import com.mdrsolutions.records_management.annotation.MarkForReview;
import com.mdrsolutions.records_management.controller.dto.MissingDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CheckMissingDetails<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMissingDetails.class);

    private final T entity;

    public CheckMissingDetails(T entity) {
        this.entity = entity;
    }

    public MissingDetailsDto getMissingDetails() {
        List<String> missingFields = new ArrayList<>();
        long missingCount = checkFields(entity, missingFields);

        return new MissingDetailsDto(missingCount, missingFields);
    }

    private long checkFields(Object object, List<String> missingFields) {
        long missingCount = 0;

        if (object == null) {
            return 1; // Count as one missing item if the object itself is null.
        }

        // Iterate through fields directly on the object.
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(MarkForReview.class)) {
                field.setAccessible(true);
                MarkForReview annotation = field.getAnnotation(MarkForReview.class);

                try {
                    Object fieldValue = field.get(object);

                    // Check if the field is missing.
                    if (fieldValue == null || (fieldValue instanceof String && ((String) fieldValue).isEmpty())) {
                        missingFields.add(annotation.name() + ": " + annotation.description());
                        missingCount++;
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.warn("Failed to access field {}: {}", field.getName(), e.getMessage());
                }
            }
        }

        return missingCount;
    }
}
