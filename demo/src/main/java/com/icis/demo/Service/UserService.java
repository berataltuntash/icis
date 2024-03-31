package com.icis.demo.Service;

import com.icis.demo.DAO.StudentDAO;
import com.icis.demo.Entity.Application;
import com.icis.demo.Entity.Student;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
public class UserService {
    private StudentDAO studentDAO;

    public UserService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public boolean isUserEligible(int id){
        Student student = studentDAO.findById(id).orElse(null);
        if (student == null) {
            return false;
        }

        if(student.getGrade()<3 || student.getDepartmentId() != 0){
            return false;
        }
        return true;
    }
    public Application getApplicationOfUser(){
        return null;
    }
    public boolean uploadDocument(){
        return false;
    }
    public String downloadDocument(){
        return null;
    }
    public String prepareDocument(){
        return null;
    }

    public void getUser(String name,String surname,String email, int studentNumber, String password){
        Student student = new Student();
        student.setName(name);
        student.setSurname(surname);
        student.setEmail(email);
        student.setId(studentNumber);
        student.setPassword(password);
        studentDAO.save(student);
    }

    public Student getUser(int id, String password){
        Student student = studentDAO.findById(id).orElse(null);
        if(student==null){
            return new Student();
        }
        return student;
    }

    public void processCompanyRequest(){

    }

}
