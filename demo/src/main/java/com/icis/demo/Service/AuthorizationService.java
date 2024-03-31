package com.icis.demo.Service;

import com.icis.demo.DAO.StudentDAO;
import com.icis.demo.Entity.Company;
import com.icis.demo.Entity.Student;
import com.icis.demo.Utils.EncryptionUtil;
import com.icis.demo.Utils.JWTUtil;
import com.icis.demo.Utils.OBSUtil;
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

    // This method is for signing up a new student
    public boolean isAuthorized(String name,String surname,String email,
                                int studentNumber,String password, String password2) {

        if (name == null || surname == null || email == null || password == null || password2 == null) {
            return false;
        }

        if(!password.equals(password2)){
            return false;
        }

        if(studentNumber<99999999 || studentNumber>1000000000){
            return false;
        }

        if(OBSUtil.isRealStudent(name,surname, email, studentNumber)){
            String idStr = Integer.toString(studentNumber);
            String token = JWTUtil.createJWTToken(idStr);
            String encryptedPassword = null;
            try{
                encryptedPassword = EncryptionUtil.encryptPassword(password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.setProperty("LOGGED_IN_USER_ID", idStr);
            System.setProperty("LOGGED_IN_USER_ROLE", "student");

            userService.getUser(name,surname, email,studentNumber, encryptedPassword);
            return true;
        }
        return false;
    }

    // This method is for logging in a student
    public boolean isAuthorized (int id, String password) {

        if (password == null) {
            return false;
        }

        boolean result = userService.isUserEligible(id);

        if (result) {
            String idStr = Integer.toString(id);
            String token = JWTUtil.createJWTToken(idStr);
            Student student = userService.getUser(id, password);
            try{
                if(student.getPassword() == EncryptionUtil.encryptPassword(password)){
                    System.setProperty("LOGGED_IN_USER_ID", idStr);
                    System.setProperty("LOGGED_IN_USER_ROLE", "student");
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
        if (name == null ||email == null || password == null || password2 == null) {
            return false;
        }

        if(!password.equals(password2)){
            return false;
        }

        String token = JWTUtil.createJWTToken(name);
        String encryptedPassword = null;
        try{
            encryptedPassword = EncryptionUtil.encryptPassword(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.setProperty("LOGGED_IN_USER_ID", name);
        System.setProperty("LOGGED_IN_USER_ROLE", "student");

        userService.getUser(name, email, encryptedPassword);
        return true;

    }

    // This method is for logging in a company
    public boolean isAuthorized (String email, String password) {

        if (password == null) {
            return false;
        }

        String token = JWTUtil.createJWTToken(email);
        Company company = userService.getUser(email, password);
        try{
            if(company.getPassword() == EncryptionUtil.encryptPassword(password)){
                System.setProperty("LOGGED_IN_USER_ID", email);
                System.setProperty("LOGGED_IN_USER_ROLE", "student");
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
