package com.android.clockwork.model;

/**
 * Created by jiabao.tan.2012 on 2/8/2015.
 */

public class Post {
    private int id;
    private String header;
    private String company;
    private int salary;
    private String description;
    private String location;
    private String posting_Date;
    private String job_Date;

    public Post(int id, String header, String company, int salary, String description, String location, String posting_Date, String job_Date) {
        this.id = id;
        this.header = header;
        this.company = company;
        this.salary = salary;
        this.description = description;
        this.location = location;
        this.posting_Date = posting_Date;
        this.job_Date = job_Date;
    }

    public Post(String header, String company, int salary, String description, String location, String job_Date) {
        this.header = header;
        this.company = company;
        this.salary = salary;
        this.description = description;
        this.location = location;
        this.job_Date = job_Date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobDate() {
        return job_Date;
    }

    public void setJobDate(String jobDate) {
        this.job_Date = jobDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosting_Date() {
        return posting_Date;
    }

    public void setPosting_Date(String posting_Date) {
        this.posting_Date = posting_Date;
    }
}
