package com.icis.demo.Controller;

import com.icis.demo.Service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
    public HttpStatus hndSignUp(@RequestParam("name") String name,
                                @RequestParam("surname") String surname,
                                @RequestParam("email") String email,
                                @RequestParam("studentNumber") int studentNumber,
                                @RequestParam("password") String password,
                                @RequestParam("password2") String password2){
        boolean success = authorizationService.isAuthorized(name, surname,email, studentNumber, password, password2);
        System.out.println("Sign up success: " + success);
        if (success) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping("/loginstudent")
    public HttpStatus hndLogin(@RequestParam("id") int id,
                               @RequestParam("password") String password) {

        boolean success = authorizationService.isAuthorized(id, password);
        System.out.println("Log in success: " + success);
        if (success) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus hndLogout() {
        return null;
    }

}
