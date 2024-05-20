package com.icis.demo.ResponseEntities;

public class ApplicationsToCompanyResponse {
    private int id;
    private String studentName;
    private String studentSurname;
    private String offerName;
    private String grade;
    private String gpa;

    public ApplicationsToCompanyResponse(int id,String studentName, String offerName, String grade, String gpa, String studentSurname) {
        this.id = id;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.offerName = offerName;
        this.grade = grade;
        this.gpa = gpa;
    }

    public ApplicationsToCompanyResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }
}
