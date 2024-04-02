package com.icis.demo.Service;

import com.icis.demo.Entity.Company;
import com.icis.demo.Entity.OnlineUser;
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
    private final EncryptionUtil EncryptionUtil;
    private final JWTUtil JWTUtil;

    @Autowired
    public AuthorizationService(UserService userService, OBSUtil obsUtil, EncryptionUtil EncryptionUtil, JWTUtil JWTUtil) {
        this.userService = userService;
        this.obsUtil = obsUtil;
        this.EncryptionUtil = EncryptionUtil;
        this.JWTUtil = JWTUtil;
    }

    public boolean isSessionValid() {
        return true;
    }

    public void removeSession(){

    }

    public AuthenticationResponse isAuthorizedSignUpStudent(String name,String surname,String email, int studentNumber,
                                             String password,HttpServletResponse response) {

        AuthenticationResponse authResponse = new AuthenticationResponse();
        if(obsUtil.isRealStudent(name,surname, email, studentNumber)){
            String jwtToken = JWTUtil.createJWTToken(email);
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            Cookie emailCookie = new Cookie("email", email);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
            response.addCookie(emailCookie);

            String encryptedPassword = null;
            try{
                encryptedPassword = EncryptionUtil.encryptPassword(password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            userService.createStudentUser(name,surname, email,studentNumber, encryptedPassword,jwtToken);
            OnlineUser onlineUser = userService.getOnlineUser(email);
            onlineUser.setJwtToken(jwtToken);

            authResponse.setSuccess(true);
            authResponse.setMessage("Registration successful.");
            authResponse.setOnlineUser(onlineUser);

            return authResponse;
        }
        authResponse.setSuccess(false);
        authResponse.setMessage("Registration failed.");
        return authResponse;
    }

    public AuthenticationResponse isAuthorizedLoginStudent (int id, String email, String password,
                                                            HttpServletResponse response) {
        boolean result = userService.isUserEligible(id);
        AuthenticationResponse authResponse = new AuthenticationResponse();

        if (result) {
            String jwtToken = JWTUtil.createJWTToken(email);
            Cookie jwtCookie = new Cookie("jwt", jwtToken);
            Cookie emailCookie = new Cookie("email", email);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
            response.addCookie(emailCookie);

            Student student = userService.getStudentUser(id, password);
            OnlineUser onlineUser = userService.getOnlineUser(email);
            onlineUser.setJwtToken(jwtToken);

            try{
                if(student.getPassword().equals(EncryptionUtil.encryptPassword(password))){
                    authResponse.setSuccess(true);
                    authResponse.setMessage("Login successful.");
                    authResponse.setOnlineUser(onlineUser);
                    return authResponse;
                }
                else {
                    authResponse.setSuccess(false);
                    authResponse.setMessage("Login failed.");
                    return authResponse;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        authResponse.setSuccess(false);
        authResponse.setMessage("Login failed.");
        return authResponse;
    }

    public AuthenticationResponse isAuthorizedSignUpCompany(String name, String email, String password,
                                             HttpServletResponse response) {

        AuthenticationResponse authResponse = new AuthenticationResponse();

        String jwtToken = JWTUtil.createJWTToken(name);
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        Cookie emailCookie = new Cookie("email", email);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        response.addCookie(emailCookie);

        String encryptedPassword = null;
        try{
            encryptedPassword = EncryptionUtil.encryptPassword(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userService.createCompanyUser(name, email, encryptedPassword, jwtToken);

        OnlineUser onlineUser = userService.getOnlineUser(email);
        onlineUser.setJwtToken(jwtToken);

        authResponse.setSuccess(true);
        authResponse.setMessage("Registration successful.");
        authResponse.setOnlineUser(onlineUser);

        return authResponse;
    }

    // This method is for logging in a company
    public AuthenticationResponse isAuthorizedLoginCompany (String email, String password,
                                             HttpServletResponse response) {
        String jwtToken = JWTUtil.createJWTToken(email);
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        Cookie emailCookie = new Cookie("email", email);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        response.addCookie(emailCookie);

        Company company = userService.getUser(email, password);

        AuthenticationResponse authResponse = new AuthenticationResponse();

        try{
            if(company.getPassword().equals(EncryptionUtil.encryptPassword(password))){
                OnlineUser onlineUser = userService.getOnlineUser(email);
                onlineUser.setJwtToken(jwtToken);

                authResponse.setSuccess(true);
                authResponse.setMessage("Login successful.");
                authResponse.setOnlineUser(onlineUser);
                return authResponse;
            }
            else {
                authResponse.setSuccess(false);
                authResponse.setMessage("Login failed.");
                return authResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
