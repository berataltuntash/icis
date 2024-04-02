package com.icis.demo.Service;

import com.icis.demo.DAO.StudentDAO;
import com.icis.demo.Entity.Company;
import com.icis.demo.Entity.OnlineUser;
import com.icis.demo.Entity.Student;
import com.icis.demo.Utils.EncryptionUtil;
import com.icis.demo.Utils.JWTUtil;
import com.icis.demo.Utils.OBSUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private UserService userService;
    private OBSUtil OBSUtil;
    private EncryptionUtil EncryptionUtil;
    private JWTUtil JWTUtil;

    @Autowired
    public AuthorizationService(UserService userService, OBSUtil OBSUtil, EncryptionUtil EncryptionUtil, JWTUtil JWTUtil) {
        this.userService = userService;
        this.OBSUtil = OBSUtil;
        this.EncryptionUtil = EncryptionUtil;
        this.JWTUtil = JWTUtil;
    }

    public boolean isSessionValid() {
        return true;
    }

    public void removeSession(){

    }

    public boolean isAuthorizedSignUpStudent(String name,String surname,String email, int studentNumber,
                                             String password,HttpServletResponse response) {

        if(OBSUtil.isRealStudent(name,surname, email, studentNumber)){
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

            return true;
        }
        return false;
    }

    public boolean isAuthorizedLoginStudent (int id,String email, String password,
                                             HttpServletResponse response) {
        boolean result = userService.isUserEligible(id);

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

            try{
                if(student.getPassword() == EncryptionUtil.encryptPassword(password)){
                    return true;
                }
                else {
                    return false;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    // This method is for signing up a new company
    public boolean isAuthorized(String name, String email, String password, String password2) {
        String token = JWTUtil.createJWTToken(name);
        String encryptedPassword = null;
        try{
            encryptedPassword = EncryptionUtil.encryptPassword(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        userService.getUser(name, email, encryptedPassword);
        return true;

    }

    // This method is for logging in a company
    public boolean isAuthorized (String email, String password) {
        String token = JWTUtil.createJWTToken(email);
        Company company = userService.getUser(email, password);
        try{
            if(company.getPassword() == EncryptionUtil.encryptPassword(password)){
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
