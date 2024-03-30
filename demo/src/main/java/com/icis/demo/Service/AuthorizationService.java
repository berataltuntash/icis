package com.icis.demo.Service;

import com.icis.demo.Utils.EncryptionUtil;
import com.icis.demo.Utils.JWTUtil;
import com.icis.demo.Utils.OBSUtil;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    OBSUtil obsUtil = new OBSUtil();
    UserService userService = new UserService();
    JWTUtil jwtUtil = new JWTUtil();
    EncryptionUtil encryptionUtil = new EncryptionUtil();

    public boolean isSessionValid() {
        return true;
    }

    public void removeSession(){

    }

    public boolean isAuthorized(){
        boolean isRealStudent = obsUtil.isRealStudent();
        if(isRealStudent){
            boolean isUserEligible = userService.isUserEligible();
            if(isUserEligible){
                String token = jwtUtil.createJWTToken("user");
                try{
                    String encryptionResult = encryptionUtil.encrypt("password");
                }
                catch (Exception e){
                    return false;
                }
                userService.getUser();
            }
        }
        return false;
    }
}
