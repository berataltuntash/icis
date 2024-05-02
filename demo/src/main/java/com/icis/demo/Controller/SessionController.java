package com.icis.demo.Controller;

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

    @PostMapping("/login")
    public ResponseEntity<?> hndLogin(@RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      HttpServletResponse response) {
        AuthenticationResponse result;
        HttpHeaders headers = new HttpHeaders();

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

    @PostMapping("/signup")
    public ResponseEntity<?> handleSignUp(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "surname", required = false) String surname,
            @RequestParam(value = "studentNumber", required = false) Integer studentNumber,
            @RequestParam(value = "staffType", required = false) String staffType,
            HttpServletResponse response) {

        AuthenticationResponse result;
        HttpHeaders headers = new HttpHeaders();

        if (email.endsWith("@std.iyte.edu.tr") && surname != null && studentNumber != null) {
            result = authorizationService.isAuthorizedSignUpStudent(name, surname, email, studentNumber, password, response);
        } else if (email.endsWith("@iyte.edu.tr") && staffType != null) {
            result = authorizationService.isAuthorizedSignUpStaff(name, email, password, staffType, response);
        } else {
            result = authorizationService.isAuthorizedSignUpCompany(name, email, password, response);
        }

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> hndResetPassword(@RequestParam String email,
                                              @RequestParam String password,
                                              @RequestParam int codeFromEmail,
                                              HttpServletResponse response){
        boolean result = authorizationService.isEmailCodeValid(email, codeFromEmail);
        if (result) {
            authorizationService.changePassword(email, password);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your Password has been successfully changed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email code is not correct.");
        }
    }
    @PostMapping("/forgotpassword")
    public ResponseEntity<?> hndForgotPassword(@RequestParam String email,
                                               HttpServletResponse response){
        boolean result = authorizationService.sendResetPasswordEmail(email);

        if (result) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Code sent to email.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is not registered.");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> hndLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String email = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            if(cookie.getName()=="email"){
                email = cookie.getValue();
            }
        }
        authorizationService.removeSession(email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Logged out.");
    }
}
