package com.icis.demo.DAO;

import com.icis.demo.Entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "application")
public interface ApplicationDAO extends JpaRepository<Application, Integer>{
}
