package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Entity.OnlineUser;
import com.icis.demo.RequestEntities.ShowHomePageRequest;
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
        return null;
    }

    @PostMapping("/showallapplications")
    public ResponseEntity<?> hndShowAllApplications(HttpServletRequest request) {
        return null;
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

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/showstudenthomepage", consumes = "application/json")
    public ResponseEntity<?> hndShowStudentHomePage(@RequestBody ShowHomePageRequest showHomePageRequest,
                                                    HttpServletRequest request) {
        String stuJWT = showHomePageRequest.getJwt();
        if (handleJWT(stuJWT, StuRole)) {
            OnlineUser studentInfo = userService.getOnlineUser(stuJWT);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentInfo.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access.");
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/showcompanyhomepage", consumes = "application/json")
    public ResponseEntity<?> hndShowCompanyHomePage(@RequestBody ShowHomePageRequest showHomePageRequest,
                                                    HttpServletRequest request) {
        String comJWT = showHomePageRequest.getJwt();
        if (handleJWT(comJWT, ComRole)) {
            OnlineUser companyInfo = userService.getOnlineUser(comJWT);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(companyInfo.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access.");
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/showstaffhomepage", consumes = "application/json")
    public ResponseEntity<?> hndShowStaffHomePage(@RequestBody ShowHomePageRequest showHomePageRequest,
                                                  HttpServletRequest request) {
        String staJWT = showHomePageRequest.getJwt();
        if (handleJWT(staJWT, StfRole)) {
            OnlineUser staffInfo = userService.getOnlineUser(staJWT);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(staffInfo.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access.");
        }
    }

    private boolean handleJWT(String jwtToken, int role) {
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

    private boolean isValidRole(int role, String email) {
        return switch (role) {
            case 1 -> email.endsWith("@std.iyte.edu.tr");
            case 2 -> email.endsWith("@iyte.edu.tr");
            case 3 -> true;
            default -> false;
        };
    }
}
