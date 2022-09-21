package com.example.djsce;

import java.io.Serializable;
import java.util.Date;

public class Company implements Serializable {
    String cname;
    String job_profile;
    String location;
    String salary;
    String cgpa;
    Date date;
    Date deadline;
    String branches;
    String pdf_url;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getJob_profile() {
        return job_profile;
    }

    public void setJob_profile(String job_profile) {
        this.job_profile = job_profile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBranches() {
        return branches;
    }

    public void setBranches(String branches) {
        this.branches = branches;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public Company(String cname, String job_profile, String location, String salary, String cgpa, Date date, Date deadline, String branches, String pdf_url) {
        this.cname = cname;
        this.job_profile = job_profile;
        this.location = location;
        this.salary = salary;
        this.cgpa = cgpa;
        this.date = date;
        this.deadline = deadline;
        this.branches = branches;
        this.pdf_url = pdf_url;
    }

    public Company(){

    }
}
