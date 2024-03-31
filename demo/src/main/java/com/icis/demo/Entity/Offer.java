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
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "company_id")
    @ManyToOne(targetEntity = Company.class)
    private int companyId;
    @Column(name = "share_date")
    private Date shareDate;
    @Column(name = "expiration_date")
    private Date expirationDate;

    public Offer(String status, int companyId,  Date shareDate, Date expirationDate) {
        this.status = status;
        this.companyId = companyId;
        this.shareDate = shareDate;
        this.expirationDate = expirationDate;
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
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
}
