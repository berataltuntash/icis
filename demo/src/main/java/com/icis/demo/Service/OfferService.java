package com.icis.demo.Service;

import com.icis.demo.DAO.OfferDAO;
import com.icis.demo.Entity.Document;
import com.icis.demo.Entity.Offer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class OfferService {

    OfferDAO offerDAO;

    public OfferService(OfferDAO offerDAO){
        this.offerDAO = offerDAO;
    }
    public List<Offer> getListOfFilteredOffers(){
        return null;
    }
    public boolean isOfferValid(Offer offer){
        return false;
    }
    public void deleteofferById(int id){
    }
    public boolean processOfferFromCompany(Offer offer){
        return false;
    }
    public boolean processStudentDocuments(Document document){
        return false;
    }
}
