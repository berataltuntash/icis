package com.icis.demo.Service;

import com.icis.demo.Entity.Company;
import com.icis.demo.Entity.OnlineUser;
import com.icis.demo.Entity.Staff;
import com.icis.demo.Entity.Student;
import com.icis.demo.System.AuthenticationResponse;
import com.icis.demo.Utils.EncryptionUtil;
import com.icis.demo.Utils.JWTUtil;
import com.icis.demo.Utils.OBSUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserService userService;
    private final OBSUtil obsUtil;
    private final EncryptionUtil encryptionUtil;
    private final JWTUtil JWTUtil;

    @Autowired
    public AuthorizationService(UserService userService, OBSUtil obsUtil, EncryptionUtil encryptionUtil, JWTUtil JWTUtil) {
        this.userService = userService;
        this.obsUtil = obsUtil;
        this.encryptionUtil = encryptionUtil;
        this.JWTUtil = JWTUtil;
    }

    public boolean isSessionValid() {
        return true;
    }

    public void removeSession(){

    }

    public AuthenticationResponse isAuthorizedSignUpStudent(String name, String surname, String email, int studentNumber,
                                                            String password, HttpServletResponse response) {

        AuthenticationResponse authResponse = new AuthenticationResponse();

        if (userService.existsByEmail(email)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("Registration failed: Email already in use.");
            return authResponse;
        }

        if (obsUtil.isRealStudent(name, surname, email, studentNumber)) {
            String encryptedPassword;
            try {
                encryptedPassword = encryptionUtil.encryptPassword(password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            userService.createStudentUser(name, surname, email, studentNumber, encryptedPassword);
            authResponse.setSuccess(true);
            authResponse.setMessage("Student Registration successful.");
            return authResponse;
        }
        authResponse.setSuccess(false);
        authResponse.setMessage("Student Registration failed.");
        return authResponse;
    }

    public AuthenticationResponse isAuthorizedSignUpCompany(String name, String email, String password,
                                                             HttpServletResponse response) {
        AuthenticationResponse authResponse = new AuthenticationResponse();
        String encryptedPassword;

        if (userService.existsByEmail(email)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("Registration failed: Email already in use.");
            return authResponse;
        }

        try {
            encryptedPassword = encryptionUtil.encryptPassword(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userService.createCompanyUser(name, email, encryptedPassword);
        authResponse.setSuccess(true);
        authResponse.setMessage("Company Registration successful.");
        return authResponse;
    }

    public AuthenticationResponse isAuthorizedSignUpStaff(String name, String email, String password, String staffType,
                                                          HttpServletResponse response) {
        AuthenticationResponse authResponse = new AuthenticationResponse();
        String encryptedPassword;

        if (userService.existsByEmail(email)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("Registration failed: Email already in use.");
            return authResponse;
        }

        try {
            encryptedPassword = encryptionUtil.encryptPassword(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userService.createStaffUser(name, email, encryptedPassword, staffType);
        authResponse.setSuccess(true);
        authResponse.setMessage("Staff Registration successful.");
        return authResponse;
    }

    public AuthenticationResponse isAuthorizedLoginStudent(String email, String password, HttpServletResponse response) {
        AuthenticationResponse authResponse = new AuthenticationResponse();

        boolean isUserEligible = userService.isUserEligible(email);
        if (!isUserEligible) {
            authResponse.setSuccess(false);
            authResponse.setMessage("Email not found or user not eligible for login.");
            return authResponse;
        }

        try {
            Student student = userService.getStudentUser(email);
            if (student == null) {
                authResponse.setSuccess(false);
                authResponse.setMessage("No user found with the given email.");
                return authResponse;
            }

            if (!student.getPassword().equals(encryptionUtil.encryptPassword(password))) {
                authResponse.setSuccess(false);
                authResponse.setMessage("Incorrect password.");
                return authResponse;
            }

            // Create JWT token and set cookies if the password matches
            String jwtToken = JWTUtil.createJWTToken(email);
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            Cookie emailCookie = new Cookie("email", email);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
            response.addCookie(emailCookie);

            OnlineUser onlineUser = userService.getOnlineUser(email);
            onlineUser.setJwtToken(jwtToken);

            authResponse.setSuccess(true);
            authResponse.setMessage("Student Login successful.");
            authResponse.setOnlineUser(onlineUser);
        } catch (Exception e) {
            authResponse.setSuccess(false);
            authResponse.setMessage("An error occurred during login.");
        }

        return authResponse;
    }

    public AuthenticationResponse isAuthorizedLoginCompany(String email, String password, HttpServletResponse response) {
        AuthenticationResponse authResponse = new AuthenticationResponse();

        Company company = userService.getCompanyUser(email);
        if (company == null) {
            authResponse.setSuccess(false);
            authResponse.setMessage("No company found with the given email.");
            return authResponse;
        }

        if (!"approved".equalsIgnoreCase(company.getStatus())) {
            authResponse.setSuccess(false);
            authResponse.setMessage("Company account is not approved.");
            return authResponse;
        }

        try {
            if (!company.getPassword().equals(encryptionUtil.encryptPassword(password))) {
                authResponse.setSuccess(false);
                authResponse.setMessage("Incorrect password.");
                return authResponse;
            }

            String jwtToken = JWTUtil.createJWTToken(email);
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            Cookie emailCookie = new Cookie("email", email);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
            response.addCookie(emailCookie);

            OnlineUser onlineUser = userService.getOnlineUser(email);
            onlineUser.setJwtToken(jwtToken);

            authResponse.setSuccess(true);
            authResponse.setMessage("Company Login successful.");
            authResponse.setOnlineUser(onlineUser);
        } catch (Exception e) {
            authResponse.setSuccess(false);
            authResponse.setMessage("An error occurred during the login process.");
        }

        return authResponse;
    }

    public AuthenticationResponse isAuthorizedLoginStaff(String email, String password, HttpServletResponse response) {
        AuthenticationResponse authResponse = new AuthenticationResponse();

        Staff staff = userService.getStaffUser(email);
        if (staff == null) {
            authResponse.setSuccess(false);
            authResponse.setMessage("No staff member found with the given email.");
            return authResponse;
        }

        try {
            if (!staff.getPassword().equals(encryptionUtil.encryptPassword(password))) {
                authResponse.setSuccess(false);
                authResponse.setMessage("Incorrect password.");
                return authResponse;
            }

            String jwtToken = JWTUtil.createJWTToken(email);
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            Cookie emailCookie = new Cookie("email", email);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
            response.addCookie(emailCookie);

            OnlineUser onlineUser = userService.getOnlineUser(email);
            onlineUser.setJwtToken(jwtToken);

            authResponse.setSuccess(true);
            authResponse.setMessage("Staff Login successful.");
            authResponse.setOnlineUser(onlineUser);
        } catch (Exception e) {
            authResponse.setSuccess(false);
            authResponse.setMessage("An error occurred during the login process.");
        }

        return authResponse;
    }
}
