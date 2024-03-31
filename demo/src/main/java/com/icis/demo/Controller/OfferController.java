package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Service.OfferService;
import com.icis.demo.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {
    private JWTUtil JWTUtil;
    private OfferService offerService;

    @Value("${JWT_TOKEN}")
    private String secretKey;
    @Value("${LOGGED_IN_USER_ID}")
    private String userId;
    @Value("${LOGGED_IN_USER_ROLE}")
    private String role;

    public OfferController(JWTUtil JWTUtil, OfferService offerService) {
        this.JWTUtil = JWTUtil;
        this.offerService = offerService;
    }

    @PostMapping("/postoffer")
    public HttpStatus hndPostOffer(@RequestParam String description,
                                   @RequestParam Date expireDate) {
        boolean result = JWTUtil.validateJWTToken(secretKey, userId);
        if (result) {
            offerService.processOfferFromCompany(description, expireDate);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
    public HttpResponse hndDeleteOffer(){
        return null;
    }

    @GetMapping("/applyfilters")
    public HttpStatus hndApplyFilter(){
        if (role.equals("student")) {
            boolean result = JWTUtil.validateJWTToken(secretKey, userId);
            List<Offer> offerList = offerService.getListOfFilteredOffers();
            if (result) {
                return HttpStatus.OK;
            } else {
                return HttpStatus.BAD_REQUEST;
            }
        }
        return HttpStatus.BAD_REQUEST;
    }
    public HttpResponse hndShowOfferDetails(){
        return null;
    }
    @PostMapping("/applyofferstudent")
    public HttpStatus hndApplyOffer(@RequestParam String companyEmail,
                                    @RequestParam String type) {
        boolean result = JWTUtil.validateJWTToken(secretKey, userId);
        if (result) {
            offerService.processStudentDocuments(companyEmail, type);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
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
