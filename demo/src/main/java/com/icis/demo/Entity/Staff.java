package com.icis.demo.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "department_id")
    private int departmentId;
    @Column(name = "authorization_level")
    private int authorizationLevel;
    @Column(name = "passwordd")
    private String password;
    @Column(name = "last_access_time")
    private Date lastAccessTime;
    @Column(name = "namee")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;

    public Staff(int departmentId, int authorizationLevel, String password, Date lastAccessTime, String name, String surname, String email) {
        this.departmentId = departmentId;
        this.authorizationLevel = authorizationLevel;
        this.password = password;
        this.lastAccessTime = lastAccessTime;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Staff() {
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

    public int getAuthorizationLevel() {
        return authorizationLevel;
    }

    public void setAuthorizationLevel(int authorizationLevel) {
        this.authorizationLevel = authorizationLevel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
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
}
