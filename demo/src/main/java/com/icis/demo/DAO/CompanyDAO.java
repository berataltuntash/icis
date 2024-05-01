package com.icis.demo.DAO;

import com.icis.demo.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "company")
public interface CompanyDAO extends JpaRepository<Company, Integer> {
    Company findCompanyByEmail(String companyEmail);
    boolean existsByEmail(String email);
}
