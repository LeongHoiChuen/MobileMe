package com.example.jiabaotan2012.cw;

/**
 * Created by jiabao.tan.2012 on 2/8/2015.
 */
import java.util.Date;

public class Post {
    private String header;
    private String company;
    private int salary;
    private String description;
    private String location;
    private String jobDate;

    public Post(String header, String company, int salary, String description, String location, String jobDate) {
        this.header = header;
        this.company = company;
        this.salary = salary;
        this.description = description;
        this.location = location;
        this.jobDate = jobDate;
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
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }
}
