package com.icis.demo.ResponseEntities;

public class ApplicationsToCompanyResponse {
    private String studentName;
    private String studentSurname;

    public ApplicationsToCompanyResponse(String studentName, String studentSurname) {
        this.studentName = studentName;
        this.studentSurname = studentSurname;
    }

    public ApplicationsToCompanyResponse() {
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

}
