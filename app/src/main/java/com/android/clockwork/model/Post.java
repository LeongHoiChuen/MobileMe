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
    private String posting_date;
    private String job_date;

    public Post(int id, String header, String company, int salary, String description, String location, String posting_date, String job_date) {
        this.id = id;
        this.header = header;
        this.company = company;
        this.salary = salary;
        this.description = description;
        this.location = location;
        this.posting_date = posting_date;
        this.job_date = job_date;
    }

    public Post(String header, String company, int salary, String description, String location, String job_date) {
        this.header = header;
        this.company = company;
        this.salary = salary;
        this.description = description;
        this.location = location;
        this.job_date = job_date;
    }

    public Post(String header, int salary, String description, String location, String job_date) {
        this.header = header;
        this.salary = salary;
        this.description = description;
        this.location = location;
        this.job_date = job_date;
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
        return job_date;
    }

    public void setJobDate(String jobdate) {
        this.job_date = jobdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosting_Date() {
        return posting_date;
    }

    public void setPosting_Date(String posting_Date) {
        this.posting_date = posting_Date;
    }
}
