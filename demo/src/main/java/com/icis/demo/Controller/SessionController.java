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

        System.out.println(result.toString());
        if (result.isSuccess()) {
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Login successful.", headers, HttpStatus.CREATED);
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
            headers.add("Set-Cookie", "jwt=" + result.getOnlineUser().getJwtToken() + "; Path=/; HttpOnly; Secure");
            return new ResponseEntity<>("Registration successful.", headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed.");
        }
    }

    @PostMapping("/resetpassword")
    public void hndResetPassword(){

    }
    @PostMapping("/logout")
    public HttpResponse hndLogout() {
        authorizationService.removeSession();
        return null;
    }

}
