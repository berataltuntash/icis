package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Entity.OnlineUser;
import com.icis.demo.Service.OfferService;
import com.icis.demo.Utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {
    private final JWTUtil JWTUtil;
    private final OfferService offerService;

    public OfferController(JWTUtil JWTUtil, OfferService offerService) {
        this.JWTUtil = JWTUtil;
        this.offerService = offerService;
    }

    @PostMapping("/postoffer")
    public HttpResponse hndPostOffer() {
        return null;
    }
    public HttpResponse hndDeleteOffer(){
        return null;
    }

    @GetMapping("/applyfilters")
    public ResponseEntity<?> hndApplyFilter(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jwtToken = "";
        String email = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                }
                if ("email".equals(cookie.getName())) {
                    email = cookie.getValue();
                }
            }
        }

        if (jwtToken != null && JWTUtil.validateJWTToken(jwtToken, email)) {
            List<Offer> offerList = offerService.getListOfFilteredOffers();
            return ResponseEntity.ok(offerList);
        } else {
            return ResponseEntity.status(401).body("Invalid or missing JWT token.");
        }
    }
    public HttpResponse hndShowOfferDetails(){
        return null;
    }
    @PostMapping("/applyofferstudent")
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
