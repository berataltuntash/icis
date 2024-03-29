package com.icis.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "application")
public class Application {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "status")
    private String status;
    @Column(name = "offer")
    @ManyToOne(targetEntity = Offer.class)
    private int offer;
    @Column(name = "student_id")
    @ManyToOne(targetEntity = Student.class)
    private int studentId;

    public Application(String status, int offer, int studentId) {
        this.status = status;
        this.offer = offer;
        this.studentId = studentId;
    }

    public Application() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
