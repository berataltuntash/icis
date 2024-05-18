package com.icis.demo.Controller;

import com.icis.demo.Entity.Company;
import com.icis.demo.Service.UserService;
import com.icis.demo.Utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final UserService userService;
    private final JWTUtil JWTUtil;

    @Autowired
    public ProfileController(UserService userService, JWTUtil JWTUtil) {
        this.userService = userService;
        this.JWTUtil = JWTUtil;
    }
    @PostMapping("/uploaddoc")
    public ResponseEntity<?> hndUploadDocument(HttpServletRequest request){

        return null;
    }

    @PostMapping("/downloaddoc")
    public ResponseEntity<?> hndDownloadDocument(HttpServletRequest request){
        return null;
    }

    @PostMapping("/formsubmission")
    public ResponseEntity<?> hndFormSubmission(@RequestParam String formType,
                                                HttpServletRequest request){
        return null;
    }

}

