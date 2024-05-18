package com.icis.demo.Service;

import com.icis.demo.DAO.CompanyDAO;
import com.icis.demo.DAO.OnlineUserDAO;
import com.icis.demo.DAO.StaffDAO;
import com.icis.demo.DAO.StudentDAO;
import com.icis.demo.Entity.*;
import com.icis.demo.Utils.DocumentUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final StudentDAO studentDAO;
    private final CompanyDAO companyDAO;
    private final OnlineUserDAO onlineUserDAO;
    private final StaffDAO staffDAO;

    @Autowired
    public UserService(StudentDAO studentDAO, CompanyDAO companyDAO, OnlineUserDAO onlineUserDAO,
                       StaffDAO staffDAO) {
        this.studentDAO = studentDAO;
        this.companyDAO = companyDAO;
        this.onlineUserDAO = onlineUserDAO;
        this.staffDAO = staffDAO;
    }

    public boolean isUserEligible(String email){
        Student student = studentDAO.findStudentByEmail(email);
        return true;
    }

    public List<Company> getCompanyApplications(){
        List<Company> companies = companyDAO.findAll();
        companies.removeIf(company -> !company.getStatus().equals("pending"));
        return companies;
    }

    public boolean approveCompanyApplication(int companyId){
        Company company = companyDAO.findCompanyById(companyId);
        if(company==null || !company.getStatus().equals("pending")){
            return false;
        }
        companyDAO.delete(company);
        company.setStatus("approved");
        companyDAO.save(company);
        return true;
    }

    public boolean rejectCompanyApplication(int companyId){
        Company company = companyDAO.findCompanyById(companyId);
        if(company==null || !company.getStatus().equals("pending")){
            return false;
        }
        companyDAO.delete(company);
        return true;
    }

    public void createStudentUser(String name,String surname,String email, int studentNumber, String password){
        Student student = new Student();
        student.setName(name);
        student.setSurname(surname);
        student.setEmail(email);
        student.setId(studentNumber);
        student.setPassword(password);
        studentDAO.save(student);
    }
    public Student getStudentUser(String email){
        Student student = studentDAO.findStudentByEmail(email);
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
    }
    public Company getCompanyUser(String email){
        Company company = companyDAO.findCompanyByEmail(email);
        if(company==null){
            return new Company();
        }
        return company;
    }
    public void createStaffUser(String name, String surname ,String email, String encryptedPassword,String stafftype) {
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
        staff.setSurname(surname);
        staffDAO.save(staff);
    }
    public Staff getStaffUser(String email){
        Staff staff = staffDAO.findStaffByEmail(email);
        if(staff==null){
            return new Staff();
        }
        return staff;
    }
    public boolean existsByEmail(String email) {
        return studentDAO.existsByEmail(email) || staffDAO.existsByEmail(email) || companyDAO.existsByEmail(email);
    }
    public void changePassword(String email, String encryptedPassword) {
        if(studentDAO.existsByEmail(email)){
            Student student = studentDAO.findStudentByEmail(email);
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
    public void removeOnlineUser(String email) {
        onlineUserDAO.deleteOnlineUserByEmail(email);
    }
    public void updateOnlineUser(OnlineUser onlineUser) {
        onlineUserDAO.delete(onlineUserDAO.findOnlineUserByEmail(onlineUser.getEmail()));
        onlineUserDAO.save(onlineUser);
    }
    public OnlineUser getOnlineUser(String jwt){
        return onlineUserDAO.findOnlineUserByJwtToken(jwt);
    }
    public void saveOnlineUser(OnlineUser onlineUser){
        onlineUserDAO.save(onlineUser);
    }
    public boolean existByEmailOnlineUser(String email) {
        return onlineUserDAO.existsOnlineUserByEmail(email);
    }
}
