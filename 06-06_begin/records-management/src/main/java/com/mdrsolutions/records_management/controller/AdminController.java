package com.mdrsolutions.records_management.controller;

import com.mdrsolutions.records_management.controller.dto.PersonDto;
import com.mdrsolutions.records_management.service.PersonService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxValue;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxPushUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private final PersonService personService;

    public AdminController(PersonService personService) {
        this.personService = personService;
    }

    @HxPushUrl(HtmxValue.TRUE)
    @GetMapping("/dashboard/page/{page}/size/{size}")
    public String showAdminDashboard(@PathVariable(name = "page", required = false) Integer page,
            @PathVariable(name = "size", required = false) Integer size,
            Model model) throws InterruptedException {
        int currentPage = (page == null || page < 1 ) ? 1 : page;
        int pageSize = (size == null || size < 1) ? 20 : size;

        LOGGER.info("showAdminDashboard(...) - page {}, size {}", currentPage, pageSize);

        Page<PersonDto> personDtoPage = personService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("personPage", personDtoPage);
        int totalPages = personDtoPage.getTotalPages();
        if(totalPages >0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // pausing 1 seconds for the effect of new content load
        Thread.sleep(1000);
        // Return the Thymeleaf template name if greater than page 1
        if(currentPage >1){
            return "admin/person-records :: person-records";
        }

        //otherwise if page is 1

        return "admin/dashboard";
    }
}
