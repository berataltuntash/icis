package com.icis.demo.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "documentstorable")
public class DocumentStorable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "application_id")
    @ManyToOne(targetEntity = Application.class)
    private Application applicationId;
    @Column(name = "namee")
    private String name;
    @Column(name = "dataa")
    private String data;

    public DocumentStorable(Application applicationId, String name, String data) {
        this.applicationId = applicationId;
        this.name = name;
        this.data = data;
    }

    public DocumentStorable() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Application getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Application applicationId) {
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
