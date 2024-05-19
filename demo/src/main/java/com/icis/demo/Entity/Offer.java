package com.icis.demo.Entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "statuss")
    private String status;

    @Column(name = "namee")
    private String name;

    @JoinColumn(name = "company_id")
    @ManyToOne(targetEntity = Company.class)
    private Company companyId;
    @Column(name = "share_date")
    private Date shareDate;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "description")
    private String description;

    public Offer(String status, Company companyId,  Date shareDate, Date expirationDate, String description, String name) {
        this.status = status;
        this.companyId = companyId;
        this.shareDate = shareDate;
        this.expirationDate = expirationDate;
        this.description = description;
        this.name = name;
    }

    public Offer() {
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

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
