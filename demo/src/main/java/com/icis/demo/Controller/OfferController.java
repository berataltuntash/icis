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

    public OfferController(JWTUtil JWTUtil, OfferService offerService) {
        this.JWTUtil = JWTUtil;
        this.offerService = offerService;
    }

    @PostMapping("/postoffer")
    public HttpResponse hndPostOffer(@RequestParam String description,
                                   @RequestParam Date expireDate) {
        boolean result = JWTUtil.validateJWTToken("a","a");
        if (result) {
            offerService.processOfferFromCompany(description, expireDate);
        }

        return null;
    }
    public HttpResponse hndDeleteOffer(){
        return null;
    }

    @GetMapping("/applyfilters")
    public HttpResponse hndApplyFilter(){
        if (true) {
            boolean result = JWTUtil.validateJWTToken("a","A");

            if (result) {
                List<Offer> offerList = offerService.getListOfFilteredOffers();
            }
        }
        return null;
    }
    public HttpResponse hndShowOfferDetails(){
        return null;
    }
    @PostMapping("/applyofferstudent")
    public HttpResponse hndApplyOffer(@RequestParam String companyEmail,
                                      @RequestParam String type) {
        boolean result = JWTUtil.validateJWTToken("a", "a");
        if (result) {
            offerService.processStudentDocuments(companyEmail, type);
        }
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
