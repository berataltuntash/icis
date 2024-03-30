package com.icis.demo.Controller;

import com.icis.demo.Service.AuthorizationService;
import org.springframework.stereotype.Controller;

import java.net.http.HttpResponse;

@Controller
public class SessionController {
    AuthorizationService authorizationService = new AuthorizationService();
    public HttpResponse hndSignUp() {

        boolean isAuth = authorizationService.isAuthorized();

        return null;
    }

    public HttpResponse hndLogin() {
        return null;
    }

    public HttpResponse hndLogout() {
        return null;
    }
}
