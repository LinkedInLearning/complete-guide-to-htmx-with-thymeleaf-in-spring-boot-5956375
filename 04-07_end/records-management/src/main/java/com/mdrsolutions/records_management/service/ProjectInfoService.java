package com.mdrsolutions.records_management.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProjectInfoService {
    private final String outerProjectFolderName;

    public ProjectInfoService() {
        // Navigate one level up from the current directory to get the outer folder
        Path currentPath = Paths.get("").toAbsolutePath();
        Path outerFolderPath = currentPath.getParent();  // Get the parent directory

        // If parent directory exists, get the folder name
        this.outerProjectFolderName = (outerFolderPath != null) ? outerFolderPath.getFileName().toString() : "Unknown";
    }

    public String getProjectFolderName() {
        return outerProjectFolderName;
    }
}
