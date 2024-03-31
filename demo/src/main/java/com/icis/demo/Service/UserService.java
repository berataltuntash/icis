package com.icis.demo.Service;

import com.icis.demo.DAO.CompanyDAO;
import com.icis.demo.DAO.StudentDAO;
import com.icis.demo.Entity.*;
import com.icis.demo.Utils.DocumentUtil;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private StudentDAO studentDAO;
    private CompanyDAO companyDAO;

    private DocumentUtil documentUtil;

    public UserService(StudentDAO studentDAO, DocumentUtil documentUtil, CompanyDAO companyDAO) {
        this.studentDAO = studentDAO;
        this.documentUtil = documentUtil;
        this.companyDAO = companyDAO;
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
    public DocumentFillable prepareDocument(String type){
        DocumentFillable documentFillable = documentUtil.createDocumentFillable(type);
        return documentFillable;
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

    public void getUser(String name, String email, String encryptedPassword) {
        Company company = new Company();
        company.setCompanyName(name);
        company.setEmail(email);
        company.setStatus("pending");
        company.setPassword(encryptedPassword);
        companyDAO.save(company);
    }

    public Company getUser(String email, String password){
        Company company = companyDAO.findCompanyByEmail(email);
        if(company==null){
            return new Company();
        }
        return company;
    }

    public void processCompanyRequest(boolean isApproved, String companyEmail){
        Company company = companyDAO.findCompanyByEmail(companyEmail);
        if(isApproved){
            company.setStatus("approved");
            companyDAO.save(company);
        }
        else{
            company.setStatus("rejected");
            companyDAO.delete(company);
        }

    }

}
