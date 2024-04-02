package com.icis.demo.Service;

import com.icis.demo.DAO.OfferDAO;
import com.icis.demo.Entity.DocumentFillable;
import com.icis.demo.Entity.Offer;
import com.icis.demo.Entity.OnlineUser;
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

    public OfferService(OfferDAO offerDAO, UserService userService, MailUtil mailUtil) {
        this.offerDAO = offerDAO;
        this.userService = userService;
        this.mailUtil = mailUtil;
    }
    public List<Offer> getListOfFilteredOffers(){
        return null;
    }
    public boolean isOfferValid(Offer offer){
        if (offer.getExpirationDate().before(new Date())) {
            return false;
        }
        return true;
    }
    public void deleteofferById(int id){
        offerDAO.deleteById(id);
    }
    public boolean processOfferFromCompany(String description, Date expireDate){
        Offer offer = new Offer();
        offer.setDescription(description);
        offer.setExpirationDate(expireDate);
        offerDAO.save(offer);
        return true;
    }
    public boolean processStudentDocuments(String companyEmail, String type){
        return false;
    }
}
