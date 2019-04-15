package com.ivanjunckes.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ivanjunckes.book.external.IceAndFireBook;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Book {

    @GeneratedValue
    @Id
    private Integer id;
    private String name;
    private String isbn;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authors = new ArrayList<>();
    @JsonProperty("number_of_pages")
    private Integer numberOfPages;
    private String publisher;
    private String country;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("release_date")
    private Date releaseDate;

    public Book() {

    }

    public Book(IceAndFireBook iceAndFireBook) {
        if (iceAndFireBook != null) {
            this.name = iceAndFireBook.getName();
            this.isbn = iceAndFireBook.getIsbn();
            this.authors = iceAndFireBook.getAuthors();
            this.numberOfPages = iceAndFireBook.getNumberOfPages();
            this.publisher = iceAndFireBook.getPublisher();
            this.country = iceAndFireBook.getCountry();
            this.releaseDate = iceAndFireBook.getReleased();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public Book patch(Book bookToPatch) {
        if (bookToPatch == null) {
            return this;
        }

        if (bookToPatch.getName() != null) {
            this.setName(bookToPatch.getName());
        }

        if (bookToPatch.getReleaseDate() != null) {
            this.setReleaseDate(bookToPatch.getReleaseDate());
        }

        if (bookToPatch.getCountry() != null) {
            this.setCountry(bookToPatch.getCountry());
        }

        if (bookToPatch.getAuthors() != null && bookToPatch.getAuthors().size() > 0) {
            this.setAuthors(bookToPatch.getAuthors());
        }

        if (bookToPatch.getIsbn() != null) {
            this.setIsbn(bookToPatch.getIsbn());
        }

        if (bookToPatch.getNumberOfPages() != null) {
            this.setNumberOfPages(bookToPatch.getNumberOfPages());
        }

        if (bookToPatch.getPublisher() != null) {
            this.setPublisher(bookToPatch.getPublisher());
        }
        return this;
    }
}
