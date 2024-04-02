package com.icis.demo.Controller;

import com.icis.demo.Service.UserService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class ProfileController {
    private UserService userService;
    public ProfileController(UserService userService) {
        this.userService = userService;
    }
    public HttpResponse hndUploadDocument(){
        return null;
    }

    public HttpResponse hndDownloadDocument(){
        return null;
    }

    public HttpResponse hndFormSubmission(){
        return null;
    }

    @PostMapping("/approvecompany")
    public HttpResponse hndApproveCompany(@RequestParam boolean approve){
        userService.processCompanyRequest(approve, "company");
        return null;
    }
}

