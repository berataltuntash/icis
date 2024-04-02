package com.icis.demo.Controller;

import com.icis.demo.Service.AuthorizationService;
import com.icis.demo.System.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.icis.demo.System.AuthenticationResponse;

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
            headers.add("Set-Cookie", "jwt=" + result.getJwtToken() + "; Path=/; HttpOnly; Secure");
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
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Registration successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed.");
        }
    }

    @PostMapping("/signupcompany")
    public HttpResponse hndSignUp(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String password2){
        boolean success = authorizationService.isAuthorized(name,email,password, password2);
        return null;
    }

    @PostMapping("/logincompany")
    public HttpResponse hndLogin(@RequestParam String email,
                                 @RequestParam String password) {

        boolean success = authorizationService.isAuthorized(email, password);
        return null;
    }


    @PostMapping("/logout")
    public HttpResponse hndLogout() {
        authorizationService.removeSession();
        return null;
    }

}
