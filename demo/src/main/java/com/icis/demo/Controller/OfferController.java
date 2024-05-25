package com.icis.demo.Controller;

import com.icis.demo.Entity.*;
import com.icis.demo.RequestEntities.*;
import com.icis.demo.ResponseEntities.*;
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
import org.springframework.web.multipart.MultipartFile;

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

    //#####################################################################################################################
    //sirket offer yarattigi yer
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/createoffer", consumes = "application/json")
    public ResponseEntity<?> hndCreateOffer(HttpServletRequest request, @RequestBody PostOfferRequest offerRequest) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }
            Company company = userService.getCompanyUser(onlineUser.getEmail());
            offerService.createOffer(company, offerRequest.getOffername(), offerRequest.getDescription());
            return new ResponseEntity<>("Offer is sent for approval.", HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            return new ResponseEntity<>("Error occured while creating the offer", HttpStatus.BAD_REQUEST);
        }
    }

    //#####################################################################################################################
    //Ogrenci staj imkanlarini gordugu yer
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/showoffers")
    public ResponseEntity<?> hndShowAllOffers(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            List<Offer> offers = offerService.getListOfOffers();
            List<ActiveOffersResponse> activeOffers = new ArrayList<>();

            for (Offer offer : offers) {
                ActiveOffersResponse activeOffer = new ActiveOffersResponse();
                if(offer.getStatus().equals("Active")){
                    activeOffer.setOfferid(offer.getId());
                    activeOffer.setOffername(offer.getName());
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
    public ResponseEntity<?> hndShowOfferDetails(HttpServletRequest request, @PathVariable("offerId") int offerId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }
            Offer offer = offerService.getOfferDetailsById(offerId);
            OfferDetailsResponse offerDetailsResponse = new OfferDetailsResponse();
            if(offer == null){
                return ResponseEntity.badRequest().body(offerDetailsResponse);
            }

            offerDetailsResponse.setOffername(offer.getName());
            offerDetailsResponse.setCompanyname(offer.getCompanyId().getCompanyName());
            offerDetailsResponse.setDescription(offer.getDescription());

            return new ResponseEntity<>(offerDetailsResponse, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offer details", HttpStatus.BAD_REQUEST);
        }
    }

    //#####################################################################################################################
    //Student staja basvurdugu yer

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/applyinternship/{offerId}")
    public ResponseEntity<?> hndApplyForInternship(HttpServletRequest request, @PathVariable("offerId") int offerId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.UNAUTHORIZED);
            }

            Offer offer = offerService.getOfferDetailsById(offerId);
            Student student = userService.getStudentUser(onlineUser.getEmail());
            Company company = offer.getCompanyId();

            if (offer.getName()==null || student.getName() == null) {
                return new ResponseEntity<>("Error occured while retrieving the Student data or Offer not exist", HttpStatus.BAD_REQUEST);
            }

            if(offerService.hasOngoingInternship(student)){
                return new ResponseEntity<>("You have an ongoing internship", HttpStatus.BAD_REQUEST);
            }

            if(offerService.hasAppliedToOffer(student, offer)){
                return new ResponseEntity<>("You have already applied for this internship", HttpStatus.BAD_REQUEST);
            }

            Application stuApplication = offerService.createStudentApplication(offer, student);

            if (stuApplication.getStatus() == null) {
                return new ResponseEntity<>("Error occured while creating a student application", HttpStatus.BAD_REQUEST);
            }

            offerService.sendInternshipApplicationMail(student, company);

            return new ResponseEntity<>("Mail sent for approval", HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offer details", HttpStatus.BAD_REQUEST);
        }
    }

    //#####################################################################################################################
    //Staff company register onayladigi yer
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/managecompanyapplication")
    public ResponseEntity<?> hndShowCompanyApplications(HttpServletRequest request){
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

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
    @PostMapping(path="/managecompanyapplication/{companyId}", consumes = "application/json")
    public ResponseEntity<?> hndApproveCompany(HttpServletRequest request, @PathVariable("companyId") int companyId) {

        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Staff staff = userService.getStaffUser(onlineUser.getEmail());
            if (staff.getAuthorizationLevel() != 1) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            boolean isApproved = Boolean.parseBoolean(request.getHeader("isApprove"));
            boolean result = false;
            Company company = userService.getCompanyUser(onlineUser.getEmail());
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

    //#####################################################################################################################
    //Staff companylerin yayinladigi offerlari onayladigi yer
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/manageoffers")
    public ResponseEntity<?> hndShowNotApprovedCompanyOffers(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            List<Offer> offers = offerService.getListOfOffers();
            List<NotApprovedOffersResponse> offersNotApproved = new ArrayList<>();
            for (Offer offer : offers) {
                NotApprovedOffersResponse offerNotApproved = new NotApprovedOffersResponse();
                if(offer.getStatus().equals("Pending")){
                    offerNotApproved.setOfferid(offer.getId());
                    offerNotApproved.setOffername(offer.getName());
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
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Offer offer = offerService.getOfferDetailsById(offerId);
            OfferDetailsResponse offerDetailsResponse = new OfferDetailsResponse();
            if(offer == null){
                return new ResponseEntity<>("Error occured while retrieving the offer details", HttpStatus.BAD_REQUEST);
            }

            offerDetailsResponse.setOffername(offer.getName());
            offerDetailsResponse.setCompanyname(offer.getCompanyId().getCompanyName());
            offerDetailsResponse.setDescription(offer.getDescription());

            return new ResponseEntity<>(offerDetailsResponse, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the offer details", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/approverejectoffer/{offerId}")
    public ResponseEntity<?> hndApproveDisapproveOffer(HttpServletRequest request, @PathVariable("offerId") int offerId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Staff staff = userService.getStaffUser(onlineUser.getEmail());
            if (staff.getAuthorizationLevel() != 1) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            boolean isApproved = Boolean.parseBoolean(request.getHeader("isApprove"));
            boolean result = false;
            if(isApproved){
                result = offerService.approveOffer(offerId);
                Offer offer = offerService.getOfferDetailsById(offerId);
                if (result) {
                    mailUtil.sendOfferAcceptedMailToTheCompany(offer.getCompanyId().getEmail(), offer.getCompanyId().getCompanyName(),offer.getName());
                    return new ResponseEntity<>("Offer Approved", HttpStatus.ACCEPTED);
                } else {
                    mailUtil.sendOfferRejectedMailToTheCompany(offer.getCompanyId().getEmail(), offer.getCompanyId().getCompanyName(),offer.getName());
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

    //#####################################################################################################################
    //Company kendisine gelen applicationlari onayladigi yer
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/applicationstocompany")
    public ResponseEntity<?> hndShowApplicationsToCompany(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Company company = userService.getCompanyUser(onlineUser.getEmail());

            if (company == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.UNAUTHORIZED);
            }

            List<Application> applications = offerService.getApplicationsToCompany(company.getId());
            List<ApplicationsToCompanyResponse> applicationsToCompany = new ArrayList<>();

            for (Application application : applications) {
                if(application.getStatus().equals("Pending")){
                    ApplicationsToCompanyResponse applicationToCompany = new ApplicationsToCompanyResponse();
                    applicationToCompany.setApplicationId(application.getId());
                    applicationToCompany.setStudentName(application.getStudentId().getName());
                    applicationToCompany.setStudentSurname(application.getStudentId().getSurname());
                    applicationsToCompany.add(applicationToCompany);
                }
            }

            return new ResponseEntity<>(applicationsToCompany, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/applicationstocompany/{applicationId}")
    public ResponseEntity<?> hndShowApplicationsToCompanyDetails(HttpServletRequest request,
                                                                 @PathVariable("applicationId") int applicationId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Company company = userService.getCompanyUser(onlineUser.getEmail());

            if (company.getCompanyName() == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Application application = offerService.getApplicationDetailsById(applicationId);

            if (application.getStatus() == null) {
                return new ResponseEntity<>("Error occured while retrieving the application details", HttpStatus.BAD_REQUEST);
            }

            ApplicationsToCompanyDetailsResponse applicationToCompany = new ApplicationsToCompanyDetailsResponse();

            applicationToCompany.setStudentName(application.getStudentId().getName());
            applicationToCompany.setStudentSurname(application.getStudentId().getSurname());
            applicationToCompany.setGrade(String.valueOf(application.getStudentId().getGrade()));
            applicationToCompany.setStudentId(String.valueOf(application.getStudentId().getId()));
            applicationToCompany.setOfferName(application.getOffer().getName());

            return new ResponseEntity<>(applicationToCompany, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/approveapplicationstocompany/{applicationId}")
    public ResponseEntity<?> hndApproveApplication(HttpServletRequest request,
                                                   @PathVariable("applicationId") int applicationId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Application application = offerService.getApplicationDetailsById(applicationId);
            if (application.getStatus().equals("Company_Approved") || application.getStatus().equals("Company_Rejected")){
                return new ResponseEntity<>("Already Rejected or Approved", HttpStatus.BAD_REQUEST);
            }

            boolean isApproved = Boolean.parseBoolean(request.getHeader("isApprove"));
            boolean result = offerService.approveApplicationCompany(applicationId, isApproved);
            if (result) {
                return new ResponseEntity<>("Application Approved", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Application Rejected", HttpStatus.ACCEPTED);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while approving the application", HttpStatus.BAD_REQUEST);
        }
    }
    //#####################################################################################################################
    //Studentin companylerin onayladigi staj basvurularini gordugu ve kendisinin de onayladigi yer
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/studentapprovedapplications")
    public ResponseEntity<?> hndShowStudentApprovedApplications(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Student student = userService.getStudentUser(onlineUser.getEmail());

            if (student == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.UNAUTHORIZED);
            }

            List<Application> applications = offerService.getApplicationsToStudent(student.getId());
            List<ApprovedApplicationResponse> approvedApplications = new ArrayList<>();

            for (Application application : applications) {
                if (application.getStatus().equals("Company_Approved")) {
                    ApprovedApplicationResponse approvedApplication = new ApprovedApplicationResponse();
                    approvedApplication.setApplicationId(application.getId());
                    approvedApplication.setOfferName(application.getOffer().getName());
                    approvedApplication.setCompanyName(application.getOffer().getCompanyId().getCompanyName());
                    approvedApplications.add(approvedApplication);
                }
            }
            return new ResponseEntity<>(approvedApplications, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/studentapprovedapplications/{applicationId}")
    public ResponseEntity<?> hndApproveApplicationStudent(HttpServletRequest request,
                                                          @PathVariable("applicationId") String applicationId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Student student = userService.getStudentUser(onlineUser.getEmail());

            if (student.getName() == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            List<Application> applications = offerService.getApplicationsToStudent(student.getId());
            if (offerService.hasOngoingInternship(student))
            {
                return new ResponseEntity<>("You have an ongoing internship", HttpStatus.BAD_REQUEST);
            }

            boolean isApproved = Boolean.parseBoolean(request.getHeader("isApprove"));
            String result = offerService.approveApplicationStudent(Integer.parseInt(applicationId) , isApproved);
            Application application = offerService.getApplicationDetailsById(Integer.parseInt(applicationId));
            Company company = application.getOffer().getCompanyId();
            if (result.equals("Approved")) {
                byte[] content = offerService.sendInternshipApplicationMailAfterStudentApprove(student, company);
                offerService.saveApplicationForm(content, Integer.parseInt(applicationId));
                return new ResponseEntity<>("Application Approved", HttpStatus.ACCEPTED);
            } else if(result.equals("Rejected")) {
                mailUtil.sendStudentIsRejectedTheOfferToCompany(application);
                return new ResponseEntity<>("Application Rejected", HttpStatus.ACCEPTED);
            }
            else{
                return new ResponseEntity<>("Error occured while approving the application", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while approving the application", HttpStatus.BAD_REQUEST);
        }
    }
    //#####################################################################################################################
    //staff sirket student karsilikli kabul edildip staj basladigi yer
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/staffshowinternshipsstarted")
    public ResponseEntity<?> hndShowInternshipsStarted(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            List<Application> applications = offerService.getListOfApplications();
            List<StartedInternshipsResponse> approvedApplications = new ArrayList<>();

            for (Application application : applications) {
                if (application.getStatus().equals("Staff_Approved")) {
                    StartedInternshipsResponse startedInternship = new StartedInternshipsResponse();
                    startedInternship.setApplicationId(application.getId());
                    startedInternship.setOfferName(application.getOffer().getName());
                    startedInternship.setStudentName(application.getStudentId().getName());
                    startedInternship.setStudentSurname(application.getStudentId().getSurname());
                    approvedApplications.add(startedInternship);
                }
            }
            return new ResponseEntity<>(approvedApplications, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/staffshowinternshipsstarted/{applicationId}")
    public ResponseEntity<?> hndShowInternshipDetails(HttpServletRequest request,
                                                      @PathVariable("applicationId") int applicationId) {

        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Application application = offerService.getApplicationDetailsById(applicationId);
            if (application.getStudentId() == null) {
                return new ResponseEntity<>("Error occured while retrieving the application details", HttpStatus.BAD_REQUEST);
            }

            StartedInternshipDetailsResponse startedInternship = new StartedInternshipDetailsResponse();

            startedInternship.setStudentName(application.getStudentId().getName());
            startedInternship.setStudentSurname(application.getStudentId().getSurname());
            startedInternship.setOfferName(application.getOffer().getName());
            startedInternship.setCompanyName(application.getOffer().getCompanyId().getCompanyName());
            startedInternship.setGrade(String.valueOf(application.getStudentId().getGrade()));
            startedInternship.setStudentId(String.valueOf(application.getStudentId().getId()));

            return new ResponseEntity<>(startedInternship, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    //#####################################################################################################################
    //sirketin form yukledigi yer
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/applicationforms")
    public ResponseEntity<?> hndShowUploadApplicationFormCompany(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Company company = userService.getCompanyUser(onlineUser.getEmail());

            if (company == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.UNAUTHORIZED);
            }

            List<Application> applications = offerService.getApplicationsToCompany(company.getId());
            List<ApplicationsToCompanyResponse> applicationsToCompany = new ArrayList<>();

            for (Application application : applications) {
                if(application.getStatus().equals("Student_Approved")){
                    ApplicationsToCompanyResponse applicationToCompany = new ApplicationsToCompanyResponse();
                    applicationToCompany.setApplicationId(application.getId());
                    applicationToCompany.setStudentName(application.getStudentId().getName());
                    applicationToCompany.setStudentSurname(application.getStudentId().getSurname());
                    applicationsToCompany.add(applicationToCompany);
                }
            }

            return new ResponseEntity<>(applicationsToCompany, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/applicationforms/{applicationId}")
    public ResponseEntity<?> hndShowUploadApplicationFormCompanyDetails(HttpServletRequest request,
                                                                        @PathVariable("applicationId") int applicationId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Company company = userService.getCompanyUser(onlineUser.getEmail());

            if (company.getCompanyName() == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Application application = offerService.getApplicationDetailsById(applicationId);

            if (application.getStatus() == null) {
                return new ResponseEntity<>("Error occured while retrieving the application details", HttpStatus.BAD_REQUEST);
            }

            ApplicationsToCompanyDetailsResponse applicationToCompany = new ApplicationsToCompanyDetailsResponse();

            applicationToCompany.setStudentName(application.getStudentId().getName());
            applicationToCompany.setStudentSurname(application.getStudentId().getSurname());
            applicationToCompany.setGrade(String.valueOf(application.getStudentId().getGrade()));
            applicationToCompany.setStudentId(String.valueOf(application.getStudentId().getId()));
            applicationToCompany.setOfferName(application.getOffer().getName());

            return new ResponseEntity<>(applicationToCompany, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    //#####################################################################################################################
    //staff approve form
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/approveforms")
    public ResponseEntity<?> hndStaffApplyApplicationForm(HttpServletRequest request) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Staff staff = userService.getStaffUser(onlineUser.getEmail());

            if (staff.getName() == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            List<Application> applications = offerService.getListOfApplications();
            List<ApplicationsToCompanyResponse> applicationsToCompany = new ArrayList<>();

            for (Application application : applications) {
                if(application.getStatus().equals("Company_Submitted")){
                    ApplicationsToCompanyResponse applicationToCompany = new ApplicationsToCompanyResponse();
                    applicationToCompany.setApplicationId(application.getId());
                    applicationToCompany.setStudentName(application.getStudentId().getName());
                    applicationToCompany.setStudentSurname(application.getStudentId().getSurname());
                    applicationsToCompany.add(applicationToCompany);
                }
            }

            return new ResponseEntity<>(applicationsToCompany, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/approveforms/{applicationId}")
    public ResponseEntity<?> hndStaffApplyApplicationFormDetails(HttpServletRequest request,
                                                                        @PathVariable("applicationId") int applicationId) {
        try{
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            Staff staff = userService.getStaffUser(onlineUser.getEmail());

            if (staff.getName() == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Application application = offerService.getApplicationDetailsById(applicationId);

            if (application.getStatus() == null) {
                return new ResponseEntity<>("Error occured while retrieving the application details", HttpStatus.BAD_REQUEST);
            }

            ApplicationsToCompanyDetailsResponse applicationToCompany = new ApplicationsToCompanyDetailsResponse();

            applicationToCompany.setStudentName(application.getStudentId().getName());
            applicationToCompany.setStudentSurname(application.getStudentId().getSurname());
            applicationToCompany.setGrade(String.valueOf(application.getStudentId().getGrade()));
            applicationToCompany.setStudentId(String.valueOf(application.getStudentId().getId()));
            applicationToCompany.setOfferName(application.getOffer().getName());

            return new ResponseEntity<>(applicationToCompany, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while retrieving the applications", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping(path="/approveapplicationform/{applicationId}")
    public ResponseEntity<?> hndApproveApplicationForm(HttpServletRequest request,
                                                        @PathVariable("applicationId") int applicationId) {
        try {
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);

            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            boolean isApproved = Boolean.parseBoolean(request.getHeader("isApprove"));
            Application application = offerService.getApplicationDetailsById(applicationId);

            String status = offerService.approveApplicationFormStaff(applicationId, isApproved);

            if (status.equals("Approved")) {
                return new ResponseEntity<>("Application Approved", HttpStatus.ACCEPTED);
            } else if(status.equals("Rejected")) {
                return new ResponseEntity<>("Application Rejected", HttpStatus.ACCEPTED);
            }

            return new ResponseEntity<>("Error occured while approving the application form", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>("Error occured while approving the application form", HttpStatus.BAD_REQUEST);
        }
    }



    //#####################################################################################################################
    //upload download dokumanlar kismi
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/uploadsgkdocument/{applicationId}")
    public ResponseEntity<?> hndStaffUploadSgk(HttpServletRequest request,
                                               @RequestParam("file") MultipartFile file,
                                               @PathVariable("applicationId") int applicationId) {
        try {
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            Staff staff = userService.getStaffUser(onlineUser.getEmail());
            if(staff.getAuthorizationLevel()!= 2){
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            boolean result = offerService.checkIfSgkDocumentExists(applicationId);
            if (result) {
                return new ResponseEntity<>("SGK Document already exists", HttpStatus.BAD_REQUEST);
            }

            byte[] bytes = file.getBytes();
            Application application = offerService.saveSgk(bytes, applicationId);

            if (application.getStudentId() == null) {
                return new ResponseEntity<>("Error occured while uploading file", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("File Uploaded Successfully", HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occured while uploading file", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/downloadsgkdocument/{applicationId}")
    public ResponseEntity<?> hndDownloadSgkDocument(HttpServletRequest request,
                                                    @PathVariable("applicationId") int applicationId) {
        try {
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            byte[] document = offerService.getApplicationDetailsById(applicationId).getSgkDocument();

            if (document == null) {
                return new ResponseEntity<>("Error occured while retrieving the document", HttpStatus.BAD_REQUEST);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sgkDocument.docx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            return new ResponseEntity<>(document, headers, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occured while downloading file", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/downloadapplicationform/{applicationId}")
    public ResponseEntity<?> hndDownloadApplicationForm(HttpServletRequest request,
                                                        @PathVariable("applicationId") int applicationId){
        try {
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            byte[] document = offerService.getApplicationDetailsById(applicationId).getApplicationForm();

            if (document == null) {
                return new ResponseEntity<>("Error occured while retrieving the document", HttpStatus.BAD_REQUEST);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applicationForm.docx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            return new ResponseEntity<>(document, headers, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occured while downloading file", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/uploadapplicationform/{applicationId}")
    public ResponseEntity<?> hndStaffUploadApplicationForm(HttpServletRequest request,
                                               @RequestParam("file") MultipartFile file,
                                               @PathVariable("applicationId") int applicationId) {
        try {
            String token = request.getHeader("Authorization");
            OnlineUser onlineUser = userService.getOnlineUser(token);
            if (onlineUser == null) {
                return new ResponseEntity<>("Unauthorized Access", HttpStatus.BAD_REQUEST);
            }

            byte[] bytes = file.getBytes();
            Application application = offerService.saveApplicationForm(bytes, applicationId);

            if (application.getStudentId() == null) {
                return new ResponseEntity<>("Error occured while uploading file", HttpStatus.BAD_REQUEST);
            }

            String email = onlineUser.getEmail();

            if (email.endsWith("@iyte.edu.tr")){
                offerService.submitApplicationFormStaff(applicationId);
            } else {
                offerService.submitApplicationFormCompany(applicationId);
            }

            return new ResponseEntity<>("File Uploaded Successfully", HttpStatus.ACCEPTED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occured while uploading file", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(path="/announcements")
    public ResponseEntity<?> hndShowAnnouncements() {
        try{
            List<Announcement> announcements = offerService.getListOfAnnouncements();
            List<AnnouncementsResponse> announcementsList = new ArrayList<>();

            for (Announcement announcement : announcements) {
                AnnouncementsResponse announcementsResponse = new AnnouncementsResponse();
                announcementsResponse.setTitle(announcement.getTitle());
                announcementsResponse.setDescription(announcement.getDescription());
                announcementsList.add(announcementsResponse);
            }

            return new ResponseEntity<>(announcementsList, HttpStatus.ACCEPTED);
        } catch(Exception e){
            return new ResponseEntity<>("Error occured while retrieving the announcements", HttpStatus.BAD_REQUEST);
        }
    }
}
