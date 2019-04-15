package com.ivanjunckes.book;

import javax.ws.rs.QueryParam;

public class BookReadFilter {

    @QueryParam("name")
    private String name;
    @QueryParam("country")
    private String country;
    @QueryParam("publisher")
    private String publisher;
    @QueryParam("year")
    private Integer year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
