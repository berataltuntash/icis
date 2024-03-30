package com.icis.demo.Service;

import com.icis.demo.DAO.StudentDAO;
import com.icis.demo.Entity.Application;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpResponse;

@Service
public class UserService {
    public boolean isUserEligible(){
        return false;
    }
    public Application getApplicationOfUser(){
        return null;
    }
    public boolean uploadDocument(){
        return false;
    }
    public String downloadDocument(){
        return null;
    }
    public String prepareDocument(){
        return null;
    }
    public void getUser(){

    }
    public void processCompanyRequest(){

    }

}
