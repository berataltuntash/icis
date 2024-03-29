package com.icis.demo.DAO;

import com.icis.demo.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "staff")
public interface StaffDAO extends JpaRepository<Staff, Integer> {
}
