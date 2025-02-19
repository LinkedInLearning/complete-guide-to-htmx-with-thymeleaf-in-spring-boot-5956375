package com.mdrsolutions.records_management.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MarkForReview {
    String name();
    String description();
    RequirementLevel level() default RequirementLevel.REQUIRED;
    String relatedField() default "";
    String conditionField() default ""; // Specifies the boolean field that controls when to check the related entity.
}
