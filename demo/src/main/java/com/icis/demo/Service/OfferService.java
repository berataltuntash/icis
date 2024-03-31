package com.icis.demo.Service;

import com.icis.demo.DAO.OfferDAO;
import com.icis.demo.Entity.DocumentFillable;
import com.icis.demo.Entity.Offer;
import com.icis.demo.Utils.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OfferService {

    OfferDAO offerDAO;
    UserService userService;
    MailUtil mailUtil;

    @Value("${LOGGED_IN_USER_ID}")
    private String userId;

    public OfferService(OfferDAO offerDAO, UserService userService, MailUtil mailUtil) {
        this.offerDAO = offerDAO;
        this.userService = userService;
        this.mailUtil = mailUtil;
    }
    public List<Offer> getListOfFilteredOffers(){
        return null;
    }
    public boolean isOfferValid(Offer offer){
        return false;
    }
    public void deleteofferById(int id){
    }
    public boolean processOfferFromCompany(String description, Date expireDate){
        Offer offer = new Offer();
        offer.setDescription(description);
        offer.setExpirationDate(expireDate);
        offerDAO.save(offer);
        return true;
    }
    //TODO switch case fillable storable needs to be implemented
    public boolean processStudentDocuments(String companyEmail, String type){
        boolean result = userService.isUserEligible(Integer.parseInt(userId));
        if (result) {
            DocumentFillable documentFillable = userService.prepareDocument(type);
            mailUtil.sendMail(companyEmail, type, documentFillable.getData());
            return true;
        }
        return false;
    }
}
