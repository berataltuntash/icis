package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Service.OfferService;
import com.icis.demo.Utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> hndPostOffer() {
        return null;
    }
    public ResponseEntity<?> hndDeleteOffer(){
        return null;
    }

    @PostMapping("/showoffers")
    public ResponseEntity<?> hndApplyFilter(HttpServletRequest request) {
        if (handleJWT(request)) {
            List<Offer> offerList = offerService.getListOfFilteredOffers();
            return ResponseEntity.ok(offerList);
        } else {
            return ResponseEntity.status(401).body("Invalid or missing JWT token.");
        }
    }
    @PostMapping("/showoffers/{offerId}")
    public ResponseEntity<?> hndShowOfferDetails(HttpServletRequest request,
                                                 @PathVariable("offerId") int offerId) {
        if (handleJWT(request)) {
            Offer offerDetails = offerService.getOfferDetailsById(offerId);
            if (offerDetails != null) {
                return ResponseEntity.ok(offerDetails);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }
    @PostMapping("/applyofferstudent")
    public ResponseEntity<?> hndHandleOffer(HttpServletRequest request,
                                            @RequestParam boolean isApproved) {
        if (handleJWT(request)) {
            return null;
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }
    @PostMapping("/handledocument")
    public ResponseEntity<?> hndHandleDocument(HttpServletRequest request,
                                               @RequestParam boolean isApproved) {

        if (handleJWT(request)) {
            return null;
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }
    @PostMapping("/applyinternship")
    public ResponseEntity<?> hndHandleInternship(HttpServletRequest request,
                                                 @RequestParam boolean isApproved) {
        if (handleJWT(request)) {
            return null;
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
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

        if (jwtToken != null && JWTUtil.validateJWTToken(jwtToken, email)){
            return true;
        } else {
            return false;
        }
    }
}
