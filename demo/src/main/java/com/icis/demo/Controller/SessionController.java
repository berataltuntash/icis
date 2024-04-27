package com.icis.demo.Controller;

import com.icis.demo.Service.AuthorizationService;
import com.icis.demo.System.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class SessionController {
    private final AuthorizationService authorizationService;

    public SessionController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signupstudent")
    public ResponseEntity<?> hndSignUp(@RequestParam("name") String name,
                                       @RequestParam("surname") String surname,
                                       @RequestParam("email") String email,
                                       @RequestParam("studentNumber") int studentNumber,
                                       @RequestParam("password") String password,
                                    HttpServletResponse response) {

        AuthenticationResponse result = authorizationService.isAuthorizedSignUpStudent(name, surname,email, studentNumber, password, response);

        HttpHeaders headers = new HttpHeaders();
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Registration successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed.");
        }
    }

    @PostMapping("/loginstudent")
    public ResponseEntity<?> hndLogin(@RequestParam("id") int id,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      HttpServletResponse response) {

        AuthenticationResponse result = authorizationService.isAuthorizedLoginStudent(id, email, password,response);

        HttpHeaders headers = new HttpHeaders();
        System.out.println(result.toString());
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Login successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed.");
        }
    }

    @PostMapping("/signupcompany")
    public ResponseEntity<?> hndSignUp(@RequestParam String name,
                                       @RequestParam String email,
                                       @RequestParam String password,
                                       HttpServletResponse response){

        AuthenticationResponse result = authorizationService.isAuthorizedSignUpCompany(name,email,password, response);
        HttpHeaders headers = new HttpHeaders();
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Registration successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed.");
        }
    }

    @PostMapping("/logincompany")
    public ResponseEntity<?> hndLogin(@RequestParam String email,
                                      @RequestParam String password,
                                      HttpServletResponse response) {

        AuthenticationResponse result = authorizationService.isAuthorizedLoginCompany(email, password, response);
        HttpHeaders headers = new HttpHeaders();
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Login successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed.");
        }
    }

    @PostMapping("/signupstaff")
    public ResponseEntity<?> hndSignUp(@RequestParam String name,
                                       @RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String staffType,
                                       HttpServletResponse response){

        AuthenticationResponse result = authorizationService.isAuthorizedSignUpCompany(name,email,password, response);
        HttpHeaders headers = new HttpHeaders();
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Registration successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed.");
        }
    }

    @PostMapping("/loginstaff")
    public ResponseEntity<?> hndLogin(@RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String staffType,
                                      HttpServletResponse response) {

        AuthenticationResponse result = authorizationService.isAuthorizedLoginCompany(email, password, response);
        HttpHeaders headers = new HttpHeaders();
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Login successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed.");
        }
    }

    @PostMapping("/resetpassword")
    public void hndResetPassword(@RequestParam String newPassword,
                                 @RequestParam String email,
                                 @RequestParam String userType,
                                 HttpServletResponse response){



    }


    @PostMapping("/logout")
    public HttpResponse hndLogout() {
        authorizationService.removeSession();
        return null;
    }

}
