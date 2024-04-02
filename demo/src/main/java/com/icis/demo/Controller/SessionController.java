package com.icis.demo.Controller;

import com.icis.demo.Service.AuthorizationService;
import jakarta.servlet.http.HttpServletResponse;
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

        boolean success = authorizationService.isAuthorizedSignUpStudent(name, surname,email, studentNumber, password, response);

        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed.");
        }
    }

    @PostMapping("/loginstudent")
    public HttpResponse hndLogin(@RequestParam("id") int id,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 HttpServletResponse response) {

        boolean success = authorizationService.isAuthorizedLoginStudent(id, email, password,response);
        return null;
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
