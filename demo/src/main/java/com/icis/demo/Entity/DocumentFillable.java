package com.icis.demo.Entity;

import jakarta.persistence.*;

public class DocumentFillable {
    @Column(name = "name")
    private String name;
    @Column(name = "data")
    private String data;

    public DocumentFillable(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public DocumentFillable() {
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
