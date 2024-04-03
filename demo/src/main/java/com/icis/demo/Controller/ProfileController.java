package com.icis.demo.Controller;

import com.icis.demo.Service.UserService;
import com.icis.demo.Utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class ProfileController {
    private final UserService userService;
    private final JWTUtil JWTUtil;
    public ProfileController(UserService userService, JWTUtil JWTUtil) {
        this.userService = userService;
        this.JWTUtil = JWTUtil;
    }
    @PostMapping("/uploaddoc")
    public ResponseEntity<?> hndUploadDocument(HttpServletRequest request){

        if (handleJWT(request)) {
            return null;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }

    @PostMapping("/downloaddoc")
    public ResponseEntity<?> hndDownloadDocument(HttpServletRequest request){
        if (handleJWT(request)) {
            return null;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }

    @PostMapping("/formsubmission")
    public ResponseEntity<?> hndFormSubmission(HttpServletRequest request){
            if (handleJWT(request)) {
                return null;
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
            }
    }

    @PostMapping("/handlecompany")
    public ResponseEntity<?> hndHandleCompany(HttpServletRequest request,
                                              @RequestParam boolean isApproved,
                                              @RequestParam String companyEmail){

        if (handleJWT(request)) {
            userService.processCompanyRequest(isApproved, companyEmail);
            return ResponseEntity.ok("Company request processed.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }

    private boolean handleJWT(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jwtToken = "";
        String email = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                }
                if ("email".equals(cookie.getName())) {
                    email = cookie.getValue();
                }
            }
        }

        if (jwtToken != null && JWTUtil.validateJWTToken(jwtToken, email)){
            return true;
        } else {
            return false;
        }
    }
}

