package com.icis.demo.Entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "department_id")
    private int departmentId;
    @Column(name = "namee")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "passwordd")
    private String password;
    @Column(name = "last_access_time")
    private Date lastAccessTime;
    @Column(name = "grade")
    private int grade;
    @Column(name = "email")
    private String email;

    public Student(int departmentId, String name, String surname, String password, Date lastAccessTime, int grade, String email) {
        this.departmentId = departmentId;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.lastAccessTime = lastAccessTime;
        this.grade = grade;
        this.email = email;
    }

    public Student() {
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
