package com.icis.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class ProfileController {

    public HttpResponse hndUploadDocument(){
        return null;
    }

    public HttpResponse hndDownloadDocument(){
        return null;
    }

    public HttpResponse hndFormSubmission(){
        return null;
    }

    public HttpResponse hndApproveCompany(){
        return null;
    }
    public HttpResponse hndDisapproveCompany(){
        return null;
    }
}

