package com.icis.demo.Controller;

import com.icis.demo.RequestEntities.AuthenticationRequest;
import com.icis.demo.RequestEntities.AuthenticationRequestCompany;
import com.icis.demo.RequestEntities.ForgotPasswordRequest;
import com.icis.demo.RequestEntities.ResetPasswordRequest;
import com.icis.demo.Service.AuthorizationService;
import com.icis.demo.System.AuthenticationResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class SessionController {
    private final AuthorizationService authorizationService;

    @Autowired
    public SessionController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @CrossOrigin(origins = "http://localhost:5173")  // Allow CORS only for this controller
    @PostMapping(path="/login", consumes = "application/json")
    public ResponseEntity<?> hndLogin(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse result;
        HttpHeaders headers = new HttpHeaders();

        String email = request.getEmail();
        String password = request.getPassword();

        if (email.endsWith("@std.iyte.edu.tr")) {
            result = authorizationService.isAuthorizedLoginStudent(email, password, response);
        } else if (email.endsWith("@iyte.edu.tr")) {
            result = authorizationService.isAuthorizedLoginStaff(email, password, response);
        } else {
            result = authorizationService.isAuthorizedLoginCompany(email, password, response);
        }

        if (result.isSuccess()) {
            return new ResponseEntity<>("Login successful.", headers, HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed.");
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/iyteregister")
    public ResponseEntity<?> handleSignUp(@RequestBody AuthenticationRequest request,
                                          HttpServletResponse response) {

        AuthenticationResponse result;
        HttpHeaders headers = new HttpHeaders();

        String email = request.getEmail();
        String password = request.getPassword();

        if (email.endsWith("@std.iyte.edu.tr")) {
            result = authorizationService.isAuthorizedSignUpStudent(email, password, response);
        } else if (email.endsWith("@iyte.edu.tr")) {
            result = authorizationService.isAuthorizedSignUpStaff(email, password, response);
        }
        else {
            result = new AuthenticationResponse();
            result.setSuccess(false);
            result.setMessage("Invalid email.");
        }

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/companyregister")
    public ResponseEntity<?> handleSignUp(@RequestBody AuthenticationRequestCompany request,
                                          HttpServletResponse response) {

        AuthenticationResponse result;
        HttpHeaders headers = new HttpHeaders();

        String name = request.getName();
        String email = request.getEmail();
        String password = request.getPassword();

        result = authorizationService.isAuthorizedSignUpCompany(name, email, password, response);

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/resetpassword")
    public ResponseEntity<?> hndResetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest,
                                              HttpServletResponse response){

        String email = resetPasswordRequest.getEmail();
        int codeFromEmail = resetPasswordRequest.getEmailCode();
        String password = resetPasswordRequest.getPassword();

        boolean result = authorizationService.isEmailCodeValid(email, codeFromEmail);
        if (result) {
            authorizationService.changePassword(email, password);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your Password has been successfully changed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email code is not correct.");
        }
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/forgotpassword")
    public ResponseEntity<?> hndForgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest,
                                               HttpServletResponse response){
        String email = forgotPasswordRequest.getEmail();
        boolean result = authorizationService.sendResetPasswordEmail(email);

        if (result) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Code sent to email.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is not registered.");
        }
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/logout")
    public ResponseEntity<?> hndLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                jwtToken = cookie.getValue();
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        authorizationService.removeSession(jwtToken);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Logged out.");
    }
}
