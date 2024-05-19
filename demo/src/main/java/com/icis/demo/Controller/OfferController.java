package com.icis.demo.Controller;

import com.icis.demo.Entity.*;
import com.icis.demo.RequestEntities.ApproveDisapproveRequest;
import com.icis.demo.ResponseEntities.ActiveOffersResponse;
import com.icis.demo.ResponseEntities.NotApprovedCompaniesResponse;
import com.icis.demo.ResponseEntities.NotApprovedOffersResponse;
import com.icis.demo.ResponseEntities.OfferDetailsResponse;
import com.icis.demo.RequestEntities.PostOfferRequest;
import com.icis.demo.Service.DocumentGeneratorService;
import com.icis.demo.Service.OfferService;
import com.icis.demo.Service.UserService;
import com.icis.demo.Utils.MailUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OfferController {
    private final OfferService offerService;
    private final UserService userService;
    private final MailUtil mailUtil;
    private final DocumentGeneratorService documentGenerationService;

    @Autowired
    public OfferController(OfferService offerService, UserService userService, MailUtil mailUtil, DocumentGeneratorService documentGenerationService) {
        this.offerService = offerService;
        this.userService = userService;
        this.mailUtil = mailUtil;
        this.documentGenerationService = documentGenerationService;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/createoffer", consumes = "application/json")
    public ResponseEntity<?> hndCreateOffer(HttpServletRequest request,
                                          @RequestBody PostOfferRequest offerRequest) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Company company = userService.getCompanyUser(onlineUser.getEmail());

            offerService.createOffer(company, offerRequest.getOfferName(), offerRequest.getDescription());

            return new ResponseEntity<>("Offer posted", HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            return new ResponseEntity<>("Error occured while creating the offer", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deleteoffer")
    public ResponseEntity<?> hndDeleteOffer(){

        return null;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/showoffers")
    public ResponseEntity<?> hndShowAllOffers() {

        try{
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
        } catch(Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offers", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/showoffers/{offerId}")
    public ResponseEntity<?> hndShowOfferDetails(@PathVariable("offerId") int offerId) {

        try{
            Offer offer = offerService.getOfferDetailsById(offerId);
            OfferDetailsResponse offerDetailsResponse = new OfferDetailsResponse();
            if(offer == null){
                return ResponseEntity.badRequest().body(offerDetailsResponse);
            }

            offerDetailsResponse.setOffername(offer.getDescription());
            offerDetailsResponse.setCompanyname(offer.getCompanyId().getCompanyName());
            offerDetailsResponse.setDescription(offer.getDescription());

            return new ResponseEntity<>(offerDetailsResponse, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offer details", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/applyinternship/{offerId}")
    public ResponseEntity<?> hndApplyForInternship(HttpServletRequest request,
                                                   @PathVariable("offerId") int offerId) {
        try{
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
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offer details", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/managecompanyapplication")
    public ResponseEntity<?> hndShowCompanyApplications(HttpServletRequest request){

        try{
            List<Company> companies = userService.getCompanyApplications();
            List<NotApprovedCompaniesResponse> companiesNotApproved = new ArrayList<>();

            for (Company company : companies) {
                NotApprovedCompaniesResponse companyNotApproved = new NotApprovedCompaniesResponse();
                companyNotApproved.setCompanyName(company.getCompanyName());
                companyNotApproved.setCompanyId(company.getId());
                companiesNotApproved.add(companyNotApproved);
            }

            return new ResponseEntity<>(companiesNotApproved, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the company applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/managecompanyapplication/{companyId}")
    public ResponseEntity<?> hndApproveCompany(HttpServletRequest request,
                                               @RequestBody ApproveDisapproveRequest approveDisapproveRequest,
                                               @PathVariable("companyId") int companyId) {

        try{
            boolean isApproved = approveDisapproveRequest.isApprove();
            boolean result = false;
            if(isApproved){
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
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while approving the company", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/manageoffers")
    public ResponseEntity<?> hndShowNotApprovedCompanyOffers(HttpServletRequest request) {
        try{
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
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offers", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/manageoffers/{offerId}")
    public ResponseEntity<?> hndViewOfferDetailsForApproveDisapprove(HttpServletRequest request, @PathVariable("offerId") int offerId) {
        try{
            Offer offer = offerService.getOfferDetailsById(offerId);
            OfferDetailsResponse offerDetailsResponse = new OfferDetailsResponse();
            if(offer == null){
                return ResponseEntity.badRequest().body(offerDetailsResponse);
            }

            offerDetailsResponse.setOffername(offer.getDescription());
            offerDetailsResponse.setCompanyname(offer.getCompanyId().getCompanyName());
            offerDetailsResponse.setDescription(offer.getDescription());

            return new ResponseEntity<>(offerDetailsResponse, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offer details", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/approverejectoffer/{offerId}", consumes = "application/json")
    public ResponseEntity<?> hndApproveDisapproveOffer(HttpServletRequest request,
                                                       @RequestBody ApproveDisapproveRequest approveDisapproveRequest,
                                                       @PathVariable("offerId") int offerId) {
        try{
            boolean isApproved = approveDisapproveRequest.isApprove();

            boolean result = false;
            if(isApproved){
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
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while approving the offer", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/downloadapplicationletter")
    public ResponseEntity<?> downloadApplicationLetter(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String email = userService.getOnlineUser(token).getEmail();

        if (email == null || !email.endsWith("@std.iyte.edu.tr")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Student student = userService.getStudentUser(email);

        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Map<String, String> studentData = new HashMap<>();
            studentData.put("ADI - SOYADI", student.getName() + " " + student.getSurname());
            studentData.put("SINIFI", String.valueOf(student.getGrade()));
            studentData.put("OKUL NUMARASI", String.valueOf(student.getId()));
            studentData.put("E-POSTA", student.getEmail());

            String templatePath = "classpath:1_TR_SummerPracticeApplicationLetter2023.docx";
            String outputPath = "classpath:application_letter_" + student.getId() + ".docx";
            documentGenerationService.generateApplicationLetter(studentData, templatePath, outputPath);

            File file = new File(outputPath);
            byte[] contents = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(contents);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file.getName());

            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
