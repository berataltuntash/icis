package com.icis.demo.System;

import com.icis.demo.Entity.Offer;
import com.icis.demo.Service.OfferService;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SystemFilter {
    OfferService offerService;

    public SystemFilter(OfferService offerService) {
        this.offerService = offerService;
    }


    public void deleteInvalidOffers(List<Offer> offers){
        List<Offer> listOfOffers = offerService.getListOfFilteredOffers();
        for (Offer offer : listOfOffers) {
            if (!offerService.isOfferValid(offer)) {
                offerService.deleteofferById(offer.getId());
            }
        }
    }
}
