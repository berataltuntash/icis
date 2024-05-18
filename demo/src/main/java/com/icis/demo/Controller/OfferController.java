package com.icis.demo.Controller;

import com.icis.demo.Entity.*;
import com.icis.demo.ResponseEntities.ActiveOffersResponse;
import com.icis.demo.ResponseEntities.NotApprovedCompaniesResponse;
import com.icis.demo.ResponseEntities.NotApprovedOffersResponse;
import com.icis.demo.ResponseEntities.OfferDetailsResponse;
import com.icis.demo.RequestEntities.PostOfferRequest;
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

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/createoffer", consumes = "application/json")
    public ResponseEntity<?> hndCreateOffer(HttpServletRequest request,
                                          @RequestBody PostOfferRequest offerRequest) {
        String token = request.getHeader("Authorization");
        OnlineUser onlineUser = userService.getOnlineUser(token);
        Company company = userService.getCompanyUser(onlineUser.getEmail());

        offerService.createOffer(company, offerRequest.getOfferName(), offerRequest.getDescription());

        return new ResponseEntity<>("Offer posted", HttpStatus.ACCEPTED);
    }

    @PostMapping("/deleteoffer")
    public ResponseEntity<?> hndDeleteOffer(){

        return null;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/showoffers", consumes = "application/json")
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
    @GetMapping(path="/showoffers/{offerId}", consumes = "application/json")
    public ResponseEntity<?> hndShowOfferDetails(@PathVariable("offerId") int offerId) {
        Offer offer = offerService.getOfferDetailsById(offerId);
        OfferDetailsResponse offerDetailsResponse = new OfferDetailsResponse();
        if(offer == null){
            return ResponseEntity.badRequest().body(offerDetailsResponse);
        }

        offerDetailsResponse.setOffername(offer.getDescription());
        offerDetailsResponse.setCompanyname(offer.getCompanyId().getCompanyName());
        offerDetailsResponse.setDescription(offer.getDescription());

        return new ResponseEntity<>(offerDetailsResponse, HttpStatus.ACCEPTED);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/applyinternship/{offerId}", consumes = "application/json")
    public ResponseEntity<?> hndApplyForInternship(HttpServletRequest request,
                                                   @PathVariable("offerId") int offerId) {
        String token = request.getHeader("Authorization");
        OnlineUser onlineUser = userService.getOnlineUser(token);

        if (onlineUser == null) {
            return new ResponseEntity<>("Unauthorized Access", HttpStatus.UNAUTHORIZED);
        }

        Offer offer = offerService.getOfferDetailsById(offerId);
        Student student = userService.getStudentUser(onlineUser.getEmail());
        Company company = offer.getCompanyId();

        if (offer == null || student == null) {
            return new ResponseEntity<>("Error occured while retrieving the Student data or Offer not exist", HttpStatus.BAD_REQUEST);
        }

        Application stuApplication = offerService.createStudentApplication(offer, student);

        if (stuApplication == null) {
            return new ResponseEntity<>("Error occured while creating a student application", HttpStatus.BAD_REQUEST);
        }

        mailUtil.sendInternshipApplicationMail(company.getEmail(),student.getName(),company.getCompanyName(), offer.getDescription());

        return new ResponseEntity<>("Mail sent for approval", HttpStatus.ACCEPTED);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/manageCompanyApplication", consumes = "application/json")
    public ResponseEntity<?> hndShowCompanyApplications(HttpServletRequest request){
        List<Company> companies = userService.getCompanyApplications();
        List<NotApprovedCompaniesResponse> companiesNotApproved = new ArrayList<>();

        for (Company company : companies) {
            NotApprovedCompaniesResponse companyNotApproved = new NotApprovedCompaniesResponse();
            companyNotApproved.setCompanyName(company.getCompanyName());
            companyNotApproved.setCompanyId(company.getId());
            companiesNotApproved.add(companyNotApproved);
        }

        return new ResponseEntity<>(companiesNotApproved, HttpStatus.ACCEPTED);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/manageCompanyApplication/{companyId}", consumes = "application/json")
    public ResponseEntity<?> hndApproveCompany(HttpServletRequest request, @PathVariable("companyId") int companyId) {
        String isApproved = request.getHeader("companyApproved");
        boolean result = false;
        if(isApproved.equals("true")){
            result = userService.approveCompanyApplication(companyId);
            if (result) {
                return new ResponseEntity<>("Company Approved", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Error occured while approving the company", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            result = userService.rejectCompanyApplication(companyId);
            if (result) {
                return new ResponseEntity<>("Company Rejected", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Error occured while rejecting the company", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/manageOffers", consumes = "application/json")
    public ResponseEntity<?> hndShowNotApprovedCompanyOffers(HttpServletRequest request) {
        List<Offer> offers = offerService.getListOfOffers();
        List<NotApprovedOffersResponse> offersNotApproved = new ArrayList<>();
        for (Offer offer : offers) {
            NotApprovedOffersResponse offerNotApproved = new NotApprovedOffersResponse();
            if(offer.getStatus().equals("Pending")){
                offerNotApproved.setOfferid(offer.getId());
                offerNotApproved.setOffername(offer.getDescription());
                offersNotApproved.add(offerNotApproved);
            }
        }
        return new ResponseEntity<>(offersNotApproved, HttpStatus.ACCEPTED);
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/manageOffers/{offerId}", consumes = "application/json")
    public ResponseEntity<?> hndViewOfferDetailsForApproveDisapprove(HttpServletRequest request, @PathVariable("offerId") int offerId) {
        Offer offer = offerService.getOfferDetailsById(offerId);
        OfferDetailsResponse offerDetailsResponse = new OfferDetailsResponse();
        if(offer == null){
            return ResponseEntity.badRequest().body(offerDetailsResponse);
        }

        offerDetailsResponse.setOffername(offer.getDescription());
        offerDetailsResponse.setCompanyname(offer.getCompanyId().getCompanyName());
        offerDetailsResponse.setDescription(offer.getDescription());

        return new ResponseEntity<>(offerDetailsResponse, HttpStatus.ACCEPTED);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/approveDisapproveOffer/{offerId}", consumes = "application/json")
    public ResponseEntity<?> hndApproveDisapproveOffer(HttpServletRequest request, @PathVariable("offerId") int offerId) {
        String isApproved = request.getHeader("offerApproved");
        boolean result = false;
        if(isApproved.equals("true")){
            result = offerService.approveOffer(offerId);
            if (result) {
                return new ResponseEntity<>("Offer Approved", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Error occured while approving the offer", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            result = offerService.rejectOffer(offerId);
            if (result) {
                return new ResponseEntity<>("Offer Rejected", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Error occured while rejecting the offer", HttpStatus.BAD_REQUEST);
            }
        }
    }
}
