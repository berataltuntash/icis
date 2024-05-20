package com.icis.demo.Service;

import com.icis.demo.DAO.AnnouncementDAO;
import com.icis.demo.DAO.ApplicationDAO;
import com.icis.demo.DAO.OfferDAO;
import com.icis.demo.Entity.*;
import com.icis.demo.Utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class OfferService {

    OfferDAO offerDAO;
    UserService userService;
    MailUtil mailUtil;
    ApplicationDAO applicationDAO;
    AnnouncementDAO announcementDAO;

    @Autowired
    public OfferService(OfferDAO offerDAO, UserService userService, MailUtil mailUtil, ApplicationDAO applicationDAO, AnnouncementDAO announcementDAO) {
        this.offerDAO = offerDAO;
        this.userService = userService;
        this.mailUtil = mailUtil;
        this.applicationDAO = applicationDAO;
        this.announcementDAO = announcementDAO;
    }

    public List<Offer> getListOfOffers(){
        List<Offer> offers = offerDAO.findAll();
        return offers;
    }

    public void createOffer(Company company, String offerName, String offerDescription){
        Offer offer = new Offer();
        offer.setCompanyId(company);
        offer.setDescription(offerName);
        offer.setDescription(offerDescription);
        offer.setStatus("Pending");

        Calendar calendar = Calendar.getInstance();
        offer.setShareDate(calendar.getTime());

        calendar.add(Calendar.MONTH, 1);
        offer.setExpirationDate(calendar.getTime());

        offerDAO.save(offer);
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
        return offerDAO.findById(offerId).orElse(null);
    }

    public boolean approveOffer(int offerId) {
        Offer offer = offerDAO.findById(offerId).orElse(null);
        if(offer == null) return false;

        offerDAO.delete(offer);
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

    public List<Application> getApplicationsToCompany(int id) {
        List<Application> applications = applicationDAO.findAll();
        List<Application> applicationsToCompany = new ArrayList<>();

        for(Application application : applications) {
            if(application.getOffer().getCompanyId().getId() == id) {
                applicationsToCompany.add(application);
            }
        }

        return applicationsToCompany;
    }

    public boolean approveApplicationCompany(int applicationId) {
        Application application = applicationDAO.findById(applicationId).orElse(null);
        if (application == null) return false;
        applicationDAO.delete(application);
        application.setStatus("Pending");
        applicationDAO.save(application);
        return true;
    }

    public List<Application> getApplicationsToStudent(int id) {
        List<Application> applications = applicationDAO.findAll();
        List<Application> applicationsToStudent = new ArrayList<>();

        for(Application application : applications) {
            if(application.getStudentId().getId() == id) {
                applicationsToStudent.add(application);
            }
        }

        return applicationsToStudent;
    }
}
