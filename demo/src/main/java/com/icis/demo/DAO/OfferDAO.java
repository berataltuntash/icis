package com.icis.demo.DAO;

import com.icis.demo.Entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "offer")
public interface OfferDAO extends JpaRepository<Offer, Integer> {

}
