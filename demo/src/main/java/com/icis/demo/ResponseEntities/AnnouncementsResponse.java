package com.icis.demo.ResponseEntities;

public class AnnouncementsResponse {
    private String title;
    private String description;

    public AnnouncementsResponse(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public AnnouncementsResponse() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
