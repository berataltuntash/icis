package com.icis.demo.Service;

import com.icis.demo.DAO.OfferDAO;
import com.icis.demo.Entity.Offer;
import com.icis.demo.Utils.MailUtil;
import org.springframework.data.domain.Sort;
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

    public List<Offer> getListOfOffers(){
        return offerDAO.findAll();
    }

    public List<Offer> getListOfFilteredOffers(String sort){
        Sort sorting = switch (sort != null ? sort : "") {
            case "name" -> Sort.by("name").ascending();
            case "date" -> Sort.by("date").descending();
            default -> Sort.unsorted();
        };
        return offerDAO.findAll(sorting);
    }
    public boolean isOfferValid(Offer offer){
        return true;
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

    public Offer getOfferDetailsById(int offerId) {
        return offerDAO.findById(offerId).orElse(null);
    }
}
