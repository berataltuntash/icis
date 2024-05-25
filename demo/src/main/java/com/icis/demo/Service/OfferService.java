package com.icis.demo.Service;

import com.icis.demo.DAO.AnnouncementDAO;
import com.icis.demo.DAO.ApplicationDAO;
import com.icis.demo.DAO.OfferDAO;
import com.icis.demo.Entity.*;
import com.icis.demo.Utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class OfferService {

    OfferDAO offerDAO;
    UserService userService;
    MailUtil mailUtil;
    ApplicationDAO applicationDAO;
    AnnouncementDAO announcementDAO;
    DocumentGeneratorService documentGenerationService;

    @Autowired
    public OfferService(OfferDAO offerDAO, UserService userService, MailUtil mailUtil, ApplicationDAO applicationDAO,
                        AnnouncementDAO announcementDAO, DocumentGeneratorService documentGenerationService) {
        this.offerDAO = offerDAO;
        this.userService = userService;
        this.mailUtil = mailUtil;
        this.applicationDAO = applicationDAO;
        this.announcementDAO = announcementDAO;
        this.documentGenerationService = documentGenerationService;
    }

    public List<Offer> getListOfOffers(){
        List<Offer> offers = offerDAO.findAll();
        return offers;
    }

    public Offer createOffer(Company company, String offerName, String offerDescription){
        Offer offer = new Offer();
        offer.setCompanyId(company);
        offer.setName(offerName);
        offer.setDescription(offerDescription);
        offer.setStatus("Pending");

        Calendar calendar = Calendar.getInstance();
        offer.setShareDate(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        offer.setExpirationDate(calendar.getTime());

        offerDAO.save(offer);
        return offer;
    }

    public Application createStudentApplication(Offer offer, Student student){
        Application studentApplication = new Application();
        studentApplication.setStatus("Pending");
        studentApplication.setOffer(offer);
        studentApplication.setStudentId(student);

        applicationDAO.save(studentApplication);

        return studentApplication;
    }

    public Offer getOfferDetailsById(int offerId) {
        return offerDAO.findById(offerId).orElse(new Offer());
    }

    public boolean approveOffer(int offerId) {
        Offer offer = offerDAO.findById(offerId).orElse(null);
        if(offer == null) return false;
        offer.setStatus("Active");
        offerDAO.save(offer);
        return true;
    }

    public boolean rejectOffer(int offerId) {
        Offer offer = offerDAO.findById(offerId).orElse(null);
        if(offer == null) return false;
        offerDAO.delete(offer);
        return true;
    }

    public List<Announcement> getListOfAnnouncements() {
        return announcementDAO.findAll();
    }

    public List<Application> getApplicationsToCompany(int companyId) {
        List<Application> applications = applicationDAO.findAll();
        List<Application> applicationsToCompany = new ArrayList<>();

        for(Application application : applications) {
            if(application.getOffer().getCompanyId().getId() == companyId) {
                applicationsToCompany.add(application);
            }
        }

        return applicationsToCompany;
    }

    public boolean approveApplicationCompany(int applicationId, boolean isApproved) {
        Application application = applicationDAO.findById(applicationId).orElse(null);
        if (application == null) return false;

        if(isApproved) {
            application.setStatus("Company_Approved");
            applicationDAO.save(application);
            return true;
        }
        else {
            application.setStatus("Company_Rejected");
            applicationDAO.save(application);
            return false;
        }
    }

    public List<Application> getApplicationsToStudent(int studentId) {
        List<Application> applications = applicationDAO.findAll();
        List<Application> applicationsToStudent = new ArrayList<>();

        for(Application application : applications) {
            if(application.getStudentId().getId() == studentId) {
                applicationsToStudent.add(application);
            }
        }
        return applicationsToStudent;
    }

    public String approveApplicationStudent(int applicationId, boolean isApproved) {
        Application application = applicationDAO.findById(applicationId).orElse(null);
        if (application == null) return "NotFound";

        if(isApproved && application.getStatus().equals("Company_Approved")) {
            application.setStatus("Student_Approved");
            applicationDAO.save(application);
            return "Approved";
        } else if (!isApproved && application.getStatus().equals("Company_Approved")) {
            application.setStatus("Student_Rejected");
            applicationDAO.save(application);
            return "Rejected";
        }
        return "ErrorOccured";
    }

    public boolean hasOngoingInternship(Student student) {
        List<Application> applications = applicationDAO.findAll();
        for (Application application : applications) {
            if (application.getStudentId().getId() == student.getId() && application.getStatus().equals("Student_Approved") ||
                application.getStatus().equals("Company_Submitted") || application.getStatus().equals("Staff_Approved")
            || application.getStatus().equals("Staff_Submitted")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAppliedToOffer(Student student, Offer offer) {
        List<Application> applications = applicationDAO.findAll();
        for (Application application : applications) {
            if (application.getStudentId().getId() == student.getId() && application.getOffer().getId() == offer.getId()) {
                return true;
            }
        }
        return false;
    }

    public void sendInternshipApplicationMail(Student student, Company company) throws IOException {
        Map<String, String> studentData = new HashMap<>();
        studentData.put("ADI - SOYADI", student.getName() + " " + student.getSurname());
        studentData.put("SINIFI", String.valueOf(student.getGrade()));
        studentData.put("OKUL NUMARASI", String.valueOf(student.getId()));
        studentData.put("TC", student.getCitizenId());
        studentData.put("TELEFON", student.getTelephone());
        studentData.put("EPOSTA", student.getEmail());

        String templatePath;
        if (company.getIsForeign()) {
            templatePath = "1_EN_SummerPracticeApplicationLetter2023.docx";
        } else {
            templatePath = "1_TR_SummerPracticeApplicationLetter2023.docx";
        }

        String outputPath = "Application_letter_" + student.getId() + ".docx";
        documentGenerationService.generateApplicationLetter(studentData, templatePath, outputPath);

        File file = new File(outputPath);
        byte[] contents = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(contents);
        }

        mailUtil.sendMessageWithAttachment(company.getEmail(),student, company ,file.getName());
    }

    public byte[] sendInternshipApplicationMailAfterStudentApprove(Student student, Company company) throws IOException {
        Map<String, String> studentData = new HashMap<>();
        studentData.put("ADI - SOYADI", student.getName() + " " + student.getSurname());
        studentData.put("SINIFI", String.valueOf(student.getGrade()));
        studentData.put("OKUL NUMARASI", String.valueOf(student.getId()));
        studentData.put("TC", student.getCitizenId());
        studentData.put("TELEFON", student.getTelephone());
        studentData.put("EPOSTA", student.getEmail());

        String templatePath = "2_TR_SummerPracticeApplicationForm2023.docx";

        String outputPath = "Application_form_" + student.getId() + ".docx";
        documentGenerationService.generateApplicationLetter(studentData, templatePath, outputPath);

        File file = new File(outputPath);
        byte[] contents = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(contents);
        }

        mailUtil.sendMessageWithAttachment(company.getEmail(),student, company ,file.getName());
        return contents;
    }

    public Application getApplicationDetailsById(int applicationId) {

        return applicationDAO.findById(applicationId).orElse(new Application());
    }

    public List<Application> getListOfApplications() {
        return applicationDAO.findAll();
    }

    public Application saveSgk(byte[] bytes, int applicationId) {
        Application application = applicationDAO.findById(applicationId).orElse(new Application());
        if (application.getStudentId()==null) return new Application();
        application.setSgkDocument(bytes);
        applicationDAO.save(application);
        return application;
    }

    public Application saveApplicationForm(byte[] bytes, int applicationId) {
        Application application = applicationDAO.findById(applicationId).orElse(new Application());
        if (application.getStudentId()==null) return new Application();
        application.setApplicationForm(bytes);
        applicationDAO.save(application);
        return application;
    }

    public boolean checkIfSgkDocumentExists(int applicationId) {
        Application application = applicationDAO.findById(applicationId).orElse(new Application());
        if(application.getStudentId()==null) return false;
        return application.getSgkDocument() != null;
    }

    public boolean submitApplicationFormCompany(int applicationId) {
        Application application = applicationDAO.findById(applicationId).orElse(new Application());
        if(application.getStudentId()==null) return false;
        application.setStatus("Company_Submitted");
        applicationDAO.save(application);
        return true;
    }

    public boolean submitApplicationFormStaff(int applicationId) {
        Application application = applicationDAO.findById(applicationId).orElse(new Application());
        if(application.getStudentId()==null) return false;
        application.setStatus("Staff_Submitted");
        applicationDAO.save(application);
        return true;
    }

    public String approveApplicationFormStaff(int applicationId, boolean isApproved) {
        Application application = applicationDAO.findById(applicationId).orElse(new Application());
        if(application.getStudentId()==null) return "NotFound";
        if(isApproved) {
            application.setStatus("Staff_Approved");
            applicationDAO.save(application);
            return "Approved";
        } else {
            application.setStatus("Staff_Rejected");
            applicationDAO.save(application);
            return "Rejected";
        }
    }
}
