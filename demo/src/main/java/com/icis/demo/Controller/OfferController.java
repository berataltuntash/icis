package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Service.OfferService;
import com.icis.demo.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {
    private JWTUtil JWTUtil;
    private OfferService offerService;
    @Value("${jwt.token}")
    private String secretKey;
    @Value("${logged.in.user.id }")
    private String userId;
    @Value("${logged.in.user.role}")
    private String role;

    public OfferController(JWTUtil JWTUtil, OfferService offerService) {
        this.JWTUtil = JWTUtil;
        this.offerService = offerService;
    }

    public HttpResponse hndPostOffer(){
        return null;
    }
    public HttpResponse hndDeleteOffer(){
        return null;
    }

    @GetMapping("/applyfilters")
    public HttpStatus hndApplyFilter(){
        if (role.equals("student")) {
            boolean result = JWTUtil.validateJWTToken(secretKey, userId);
            List<Offer> offerList = offerService.getListOfFilteredOffers();

        }
        return null;
    }
    public HttpResponse hndShowOfferDetails(){
        return null;
    }
    public HttpResponse hndApplyOffer() {
        return null;
    }
    public HttpResponse hndApproveOffer(){
        return null;
    }
    public HttpResponse hndApproveDocument(){
        return null;
    }
    public HttpResponse hndDisapproveOffer() {
        return null;
    }
}
