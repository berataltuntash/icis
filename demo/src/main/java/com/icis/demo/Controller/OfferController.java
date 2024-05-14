package com.icis.demo.Controller;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Entity.OnlineUser;
import com.icis.demo.ResponseEntities.ActiveOffersResponse;
import com.icis.demo.ResponseEntities.OfferDetailsResponse;
import com.icis.demo.Service.OfferService;
import com.icis.demo.Service.UserService;
import com.icis.demo.Utils.JWTUtil;
import com.icis.demo.Utils.MailUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {
    private final JWTUtil JWTUtil;
    private final OfferService offerService;
    private final UserService userService;
    private final MailUtil mailUtil;
    private final int StuRole = 1;
    private final int StfRole = 2;
    private final int ComRole = 3;


    @Autowired
    public OfferController(JWTUtil JWTUtil, OfferService offerService, UserService userService, MailUtil mailUtil) {
        this.JWTUtil = JWTUtil;
        this.offerService = offerService;
        this.userService = userService;
        this.mailUtil = mailUtil;
    }

    @PostMapping("/postoffer")
    public ResponseEntity<?> hndPostOffer() {
        return null;
    }

    @PostMapping("/deleteoffer")
    public ResponseEntity<?> hndDeleteOffer(){
        return null;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/showalloffers")
    public ResponseEntity<?> hndShowAllOffers() {
        List<Offer> offers = offerService.getListOfOffers();
        List<ActiveOffersResponse> activeOffers = new ArrayList<>();

        for (Offer offer : offers) {
            ActiveOffersResponse activeOffer = new ActiveOffersResponse();
            if(offer.getStatus().equals("Active")){
                activeOffer.setOfferid(offer.getId());
                activeOffer.setOffername(offer.getDescription());
                activeOffers.add(activeOffer);
            }
        }

        return ResponseEntity.ok(activeOffers);
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/showoffers/{offerId}")
    public ResponseEntity<?> hndShowOfferDetails(@PathVariable("offerId") int offerId) {
        Offer offer = offerService.getOfferDetailsById(offerId);
        OfferDetailsResponse offerDetailsResponse = new OfferDetailsResponse();
        if(offer == null){
            return ResponseEntity.badRequest().body(offerDetailsResponse);
        }

        offerDetailsResponse.setOffername(offer.getDescription());
        offerDetailsResponse.setCompanyname(offer.getCompanyId().getCompanyName());
        offerDetailsResponse.setDescription(offer.getDescription());

        return new ResponseEntity<>(offerDetailsResponse, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/applyinternship/{offerId}")
    public ResponseEntity<?> hndApplyForInternship(HttpServletRequest request,
                                                   @PathVariable("offerId") int offerId) {

        String token = request.getHeader("Authorization");
        OnlineUser onlineUser = userService.getOnlineUser(token);

        if (onlineUser == null) {
            return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
        }

        String email = onlineUser.getEmail();
        String companyEmail = offerService.getOfferDetailsById(offerId).getCompanyId().getEmail();
        mailUtil.sendMail(companyEmail, "internship application", "application body");

        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }

    @PostMapping("/applyofferstudent")
    public ResponseEntity<?> hndHandleOffer(HttpServletRequest request,
                                            @RequestParam boolean isApproved) {
        return null;
    }

    @PostMapping("/showallapplications")
    public ResponseEntity<?> hndShowAllApplications(HttpServletRequest request) {
        return null;
    }
    @PostMapping("/handledocument")
    public ResponseEntity<?> hndHandleDocument(HttpServletRequest request,
                                               @RequestParam boolean isApproved) {
        return null;
    }


}
