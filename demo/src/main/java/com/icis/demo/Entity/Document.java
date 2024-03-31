package com.icis.demo.Entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "application_id")
    @ManyToOne(targetEntity = Application.class)
    private int applicationId;
    @Column(name = "name")
    private String name;
    @Column(name = "data")
    private String data;

    public Document(int applicationId, String name, String data) {
        this.applicationId = applicationId;
        this.name = name;
        this.data = data;
    }

    public Document() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
