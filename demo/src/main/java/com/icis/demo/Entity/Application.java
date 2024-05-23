package com.icis.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "application")
public class Application {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "statuss")
    private String status;
    @JoinColumn(name = "offer_id")
    @ManyToOne(targetEntity = Offer.class)
    private Offer offer;
    @JoinColumn(name = "student_id")
    @ManyToOne(targetEntity = Student.class)
    private Student studentId;
    @Lob
    @Column(name = "sgk_document")
    private byte[] sgkDocument;

    public Application(String status, Offer offer, Student studentId, byte[] sgkDocument) {
        this.status = status;
        this.offer = offer;
        this.studentId = studentId;
        this.sgkDocument = sgkDocument;
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

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public byte[] getSgkDocument() {
        return sgkDocument;
    }

    public void setSgkDocument(byte[] sgkDocument) {
        this.sgkDocument = sgkDocument;
    }
}
