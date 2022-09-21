package com.example.djsce;

public class MyDetails {

    String name;
    String email;
    String number;
    String department;
    String cgpa;
    String xiiMarks;
    String xMarks;
    String resumeURL;

    public String getKTs() {
        return KTs;
    }

    public void setKTs(String KTs) {
        this.KTs = KTs;
    }

    String KTs;

    public MyDetails(String name, String email, String number, String department, String cgpa, String xiiMarks, String xMarks, String resumeURL,String KTs) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.department = department;
        this.cgpa = cgpa;
        this.xiiMarks = xiiMarks;
        this.xMarks = xMarks;
        this.resumeURL = resumeURL;
        this.KTs = KTs;
    }

    public MyDetails(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public String getXiiMarks() {
        return xiiMarks;
    }

    public void setXiiMarks(String xiiMarks) {
        this.xiiMarks = xiiMarks;
    }

    public String getxMarks() {
        return xMarks;
    }

    public void setxMarks(String xMarks) {
        this.xMarks = xMarks;
    }

    public String getResumeURL() {
        return resumeURL;
    }

    public void setResumeURL(String resumeURL) {
        this.resumeURL = resumeURL;
    }
}
