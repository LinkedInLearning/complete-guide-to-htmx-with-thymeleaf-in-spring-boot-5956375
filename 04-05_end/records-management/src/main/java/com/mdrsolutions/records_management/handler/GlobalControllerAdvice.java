package com.mdrsolutions.records_management.handler;

import com.mdrsolutions.records_management.service.ProjectInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final ProjectInfoService projectInfoService;

    public GlobalControllerAdvice(ProjectInfoService projectInfoService) {
        this.projectInfoService = projectInfoService;
    }

    @ModelAttribute("projectFolderName")
    public String projectFolderName(){
        return projectInfoService.getProjectFolderName();
    }
}
