package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Entity.OnlineUser;
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
    private final int StuRole = 1;
    private final int StfRole = 2;
    private final int ComRole = 3;


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

    @GetMapping("/showalloffers")
    public ResponseEntity<?> hndShowAllOffers(HttpServletRequest request) {
        if (handleJWT(request, StuRole)) {
            List<Offer> offers = offerService.getListOfOffers();
            return new ResponseEntity<>(offers, new HttpHeaders(), HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }
    }

    @PostMapping("/showallapplications")
    public ResponseEntity<?> hndShowAllApplications(HttpServletRequest request) {
        if(handleJWT(request, StuRole)) {
            return null;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }
    }

    @PostMapping("/showoffers")
    public ResponseEntity<?> hndApplyFilter(@RequestParam(required = false) String sort,
                                            HttpServletRequest request) {
        return null;
    }
    @PostMapping("/showoffers/{offerId}")
    public ResponseEntity<?> hndShowOfferDetails(HttpServletRequest request,
                                                 @PathVariable("offerId") int offerId) {
        return null;
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

    private boolean handleJWT(HttpServletRequest request, int role) {
        String jwtToken = extractJWTFromCookies(request);
        if (jwtToken == null || jwtToken.isEmpty()) {
            return false;
        }

        OnlineUser onlineUser = userService.getOnlineUser(jwtToken);
        if (onlineUser == null || !jwtToken.equals(onlineUser.getJwtToken())) {
            return false;
        }

        String email = onlineUser.getEmail();
        return isValidRole(role, email);
    }

    private String extractJWTFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isValidRole(int role, String email) {
        return switch (role) {
            case 1 -> email.endsWith("@std.iyte.edu.tr");
            case 2 -> email.endsWith("@iyte.edu.tr");
            case 3 -> true;
            default -> false;
        };
    }
}
