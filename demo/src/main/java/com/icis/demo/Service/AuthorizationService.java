package com.icis.demo.Service;

import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    public boolean isSessionValid() {
        return true;
    }

    public void removeSession(){

    }

    public boolean isAuthorized(){
        return true;
    }
}
