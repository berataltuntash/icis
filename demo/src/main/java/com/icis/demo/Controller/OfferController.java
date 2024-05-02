package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Service.OfferService;
import com.icis.demo.Service.UserService;
import com.icis.demo.Utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {
    private final JWTUtil JWTUtil;
    private final OfferService offerService;
    private final UserService userService;
    @Autowired
    public OfferController(JWTUtil JWTUtil, OfferService offerService, UserService userService) {
        this.JWTUtil = JWTUtil;
        this.offerService = offerService;
        this.userService = userService;
    }

    @PostMapping("/postoffer")
    public ResponseEntity<?> hndPostOffer() {
        return null;
    }
    @PostMapping("/deleteoffer")
    public ResponseEntity<?> hndDeleteOffer(){
        return null;
    }
    @PostMapping("/showoffers")
    public ResponseEntity<?> hndApplyFilter(@RequestParam(required = false) String sort,
                                            HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        if (handleJWT(request)) {
            List<Offer> offers = offerService.getListOfFilteredOffers(sort);
            return new ResponseEntity<>(offers, headers, HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }
    @PostMapping("/showoffers/{offerId}")
    public ResponseEntity<?> hndShowOfferDetails(HttpServletRequest request,
                                                 @PathVariable("offerId") int offerId) {
        HttpHeaders headers = new HttpHeaders();
        if (handleJWT(request)) {
            Offer offer = offerService.getOfferDetailsById(offerId);
            return new ResponseEntity<>(offer, headers, HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }
    @PostMapping("/applyofferstudent")
    public ResponseEntity<?> hndHandleOffer(HttpServletRequest request,
                                            @RequestParam boolean isApproved) {
        return null;
    }
    @PostMapping("/handledocument")
    public ResponseEntity<?> hndHandleDocument(HttpServletRequest request,
                                               @RequestParam boolean isApproved) {
        return null;
    }
    @PostMapping("/applyinternship")
    public ResponseEntity<?> hndHandleInternship(HttpServletRequest request,
                                                 @RequestParam boolean isApproved) {
        return null;
    }

    private boolean handleJWT(HttpServletRequest request) {
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

        if (jwtToken.isEmpty() || email.isEmpty()) {
            return false;
        }
        if (!JWTUtil.validateJWTToken(jwtToken, email)) {
            return false;
        }
        if(!userService.getOnlineUser(email).getJwtToken().equals(jwtToken)){
            return false;
        }
        return true;
    }
}
