package com.mdrsolutions.records_management.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class MissingDetailsDto {
    private long missingCount;
    private List<String> missingFields = new ArrayList<>();

    public MissingDetailsDto(long missingCount, List<String> missingFields) {
        this.missingCount = missingCount;
        this.missingFields = missingFields;
    }

    public MissingDetailsDto(long missingCount, List<String> missingFields, boolean sumUp) {
        this.missingCount += missingCount;
        this.missingFields.addAll(missingFields);
    }

    public long getMissingCount() {
        return missingCount;
    }

    public void setMissingCount(long missingCount) {
        this.missingCount = missingCount;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(List<String> missingFields) {
        this.missingFields = missingFields;
    }
}