package com.ivanjunckes.book.external;

import com.ivanjunckes.book.Book;

import java.util.Date;
import java.util.List;

public class IceAndFireBook {

    private String url;
    private String name;
    private String isbn;
    private List<String> authors;
    private Integer numberOfPages;
    private String publisher;
    private String country;
    private Date released;
    private List<String> characters;
    private List<String> povCharacters;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getReleased() {
        return released;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public List<String> getPovCharacters() {
        return povCharacters;
    }

    public void setPovCharacters(List<String> povCharacters) {
        this.povCharacters = povCharacters;
    }

    public Book asBook() {
        Book book = new Book();
        book.setName(this.name);
        book.setIsbn(this.isbn);
        book.setAuthors(this.authors);
        book.setNumberOfPages(this.numberOfPages);
        book.setPublisher(this.publisher);
        book.setCountry(this.country);
        book.setReleaseDate(this.released);
        return book;
    }
}
