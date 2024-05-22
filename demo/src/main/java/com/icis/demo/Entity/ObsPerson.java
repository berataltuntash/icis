package com.icis.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "obsperson")
public class ObsPerson {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "student_id")
    private int id;
    @Column(name = "department_id")
    private int departmentId;
    @Column(name = "namee")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "rolee")
    private String role;
    @Column(name = "citizen_id")
    private String citizenId;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "is_foreign")
    private boolean isForeign;

    public ObsPerson(int departmentId, String name, String surname, String email, String role, String citizenId, String telephone, boolean isForeign) {
        this.departmentId = departmentId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
        this.citizenId = citizenId;
        this.telephone = telephone;
        this.isForeign = isForeign;
    }

    public ObsPerson() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(boolean isForeign) {
        this.isForeign = isForeign;
    }


}
