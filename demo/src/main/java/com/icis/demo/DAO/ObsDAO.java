package com.icis.demo.DAO;

import com.icis.demo.Entity.ObsPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "obsperson")
public interface ObsDAO extends JpaRepository<ObsPerson, Integer> {
    ObsPerson findObsPersonByEmail(String email);
}