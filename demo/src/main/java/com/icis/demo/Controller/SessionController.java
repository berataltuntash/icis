package com.icis.demo.Controller;

import com.icis.demo.Entity.OnlineUser;
import com.icis.demo.RequestEntities.*;
import com.icis.demo.ResponseEntities.AccessResponse;
import com.icis.demo.ResponseEntities.LoginResponse;
import com.icis.demo.Service.AuthorizationService;
import com.icis.demo.System.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SessionController {
    private final AuthorizationService authorizationService;

    @Autowired
    public SessionController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/login", consumes = "application/json")
    public ResponseEntity<?> hndLogin(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse result;

        String email = request.getEmail();
        String password = request.getPassword();

        if (email.endsWith("@std.iyte.edu.tr")) {
            result = authorizationService.isAuthorizedLoginStudent(email, password);
        } else if (email.endsWith("@iyte.edu.tr")) {
            result = authorizationService.isAuthorizedLoginStaff(email, password);
        } else {
            result = authorizationService.isAuthorizedLoginCompany(email, password);
        }

        String message = result.getMessage();

        if (result.isSuccess()) {
            String userJWT = result.getOnlineUser().getJwtToken();

            LoginResponse loginResponse = new LoginResponse(userJWT, message);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(loginResponse);
        } else {
            LoginResponse loginResponse = new LoginResponse("", message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/iyteregister", consumes = "application/json")
    public ResponseEntity<?> handleSignUp(@RequestBody AuthenticationRequest request) {

        AuthenticationResponse result;

        String email = request.getEmail();
        String password = request.getPassword();

        if (email.endsWith("@std.iyte.edu.tr")) {
            result = authorizationService.isAuthorizedSignUpStudent(email, password);
        } else if (email.endsWith("@iyte.edu.tr")) {
            result = authorizationService.isAuthorizedSignUpStaff(email, password);
        }
        else {
            result = new AuthenticationResponse();
            result.setSuccess(false);
            result.setMessage("Invalid email.");
        }

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/companyregister",consumes = "application/json")
    public ResponseEntity<?> handleSignUp(@RequestBody AuthenticationRequestCompany request) {

        AuthenticationResponse result;

        String name = request.getName();
        String email = request.getEmail();
        String password = request.getPassword();
        boolean isForeign = false;

        result = authorizationService.isAuthorizedSignUpCompany(name, email, isForeign, password);

        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(result.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/resetpassword",consumes = "application/json")
    public ResponseEntity<?> hndResetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
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
    @PostMapping(path="/forgotpassword",consumes = "application/json")
    public ResponseEntity<?> hndForgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        String email = forgotPasswordRequest.getEmail();
        boolean result = authorizationService.sendResetPasswordEmail(email);

        if (result) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Code sent to email.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is not registered.");
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/checktoken",consumes = "application/json")
    public ResponseEntity<?> hndCheckToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No JWT token provided.");
        }

        OnlineUser result = authorizationService.isSessionValid(token);
        String userType = "";

        if(result == null) {
            AccessResponse accessResponse = new AccessResponse("Denied", "Denied");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accessResponse);
        }

        String email = result.getEmail();

        if(email.endsWith("@std.iyte.edu.tr")){
            userType = "Student";
        }
        else if(email.endsWith("@iyte.edu.tr")){
            userType = "Staff";
        }
        else{
            userType = "Company";
        }

        AccessResponse accessResponse = new AccessResponse(userType,email.substring(0, email.indexOf("@")));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(accessResponse);
    }
}
