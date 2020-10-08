package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

public class Book {


    private String id;

    @Pattern(regexp = "[A-Z][a-z]{1,12}", message = "enter the correct author")
    private String author;

    @Pattern(regexp = "[A-Z][a-z]{1,12}", message = "enter the correct title")
    private String title;

    @Digits(integer = 4, fraction = 0, message = "field value must be digit and less then 4 signs")
    private Integer size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                '}';
    }
}