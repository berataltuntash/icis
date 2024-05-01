package com.icis.demo.Service;

import com.icis.demo.DAO.CompanyDAO;
import com.icis.demo.DAO.OnlineUserDAO;
import com.icis.demo.DAO.StaffDAO;
import com.icis.demo.DAO.StudentDAO;
import com.icis.demo.Entity.*;
import com.icis.demo.Utils.DocumentUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private StudentDAO studentDAO;
    private CompanyDAO companyDAO;
    private DocumentUtil documentUtil;
    private OnlineUserDAO onlineUserDAO;
    private StaffDAO staffDAO;

    public UserService(StudentDAO studentDAO, DocumentUtil documentUtil, CompanyDAO companyDAO, OnlineUserDAO onlineUserDAO,
                       StaffDAO staffDAO) {
        this.studentDAO = studentDAO;
        this.documentUtil = documentUtil;
        this.companyDAO = companyDAO;
        this.onlineUserDAO = onlineUserDAO;
        this.staffDAO = staffDAO;
    }

    //TODO CHECK GRADE WHEN CONNECTED TO OBS API
    public boolean isUserEligible(String email){
        Student student = studentDAO.findByEmail(email);

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

    public void createStudentUser(String name,String surname,String email, int studentNumber, String password){
        Student student = new Student();
        student.setName(name);
        student.setSurname(surname);
        student.setEmail(email);
        student.setId(studentNumber);
        student.setPassword(password);
        studentDAO.save(student);

        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setEmail(email);
        onlineUser.setUsername(name);
        onlineUserDAO.save(onlineUser);
    }

    public Student getStudentUser(String email){
        Student student = studentDAO.findByEmail(email);
        if(student==null){
            return new Student();
        }
        return student;
    }

    public void createCompanyUser(String name, String email, String encryptedPassword) {
        Company company = new Company();
        company.setCompanyName(name);
        company.setEmail(email);
        company.setStatus("pending");
        company.setPassword(encryptedPassword);
        companyDAO.save(company);

        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setEmail(email);
        onlineUser.setUsername(name);
        onlineUserDAO.save(onlineUser);
    }

    public Company getCompanyUser(String email){
        Company company = companyDAO.findCompanyByEmail(email);
        if(company==null){
            return new Company();
        }
        return company;
    }

    public void createStaffUser(String name, String email, String encryptedPassword,String stafftype) {
        int staffDepId;

        if(stafftype.equals("SummerPracticeCoordinator")){
            staffDepId = 1;
        }
        else if(stafftype.equals("chiefEngineer")){
            staffDepId = 2;
        }
        else{
            staffDepId = 3;
        }

        Staff staff = new Staff();
        staff.setName(name);
        staff.setEmail(email);
        staff.setDepartmentId(staffDepId);
        staff.setPassword(encryptedPassword);
        staffDAO.save(staff);

        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setEmail(email);
        onlineUser.setUsername(name);
        onlineUserDAO.save(onlineUser);
    }

    public Staff getStaffUser(String email){
        Staff staff = staffDAO.findStaffByEmail(email);
        if(staff==null){
            return new Staff();
        }
        return staff;
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

    public OnlineUser getOnlineUser(String email){
        return onlineUserDAO.findOnlineUserByEmail(email);
    }

    public OnlineUser getOnlineUser(String jwt, String fill){
        return onlineUserDAO.findOnlineUserByJwtToken(jwt);
    }

    public boolean existsByEmail(String email) {
        return studentDAO.existsByEmail(email) || staffDAO.existsByEmail(email) || companyDAO.existsByEmail(email);
    }

    public void changePassword(String email, String encryptedPassword) {
        if(studentDAO.existsByEmail(email)){
            Student student = studentDAO.findByEmail(email);
            student.setPassword(encryptedPassword);
            studentDAO.save(student);
        }
        else if(staffDAO.existsByEmail(email)){
            Staff staff = staffDAO.findStaffByEmail(email);
            staff.setPassword(encryptedPassword);
            staffDAO.save(staff);
        }
        else if(companyDAO.existsByEmail(email)){
            Company company = companyDAO.findCompanyByEmail(email);
            company.setPassword(encryptedPassword);
            companyDAO.save(company);
        }
    }
}
