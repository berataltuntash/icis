package com.icis.demo.DAO;

import com.icis.demo.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "student")
public interface StudentDAO extends JpaRepository<Student, Integer>{
    Student findStudentByEmail(String email);
    boolean existsByEmail(String email);
}