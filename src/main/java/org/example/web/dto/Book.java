package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

public class Book {

    private Integer id;

    /* Установим следующие требования для поля автор:
      - Имя (фамилия) могут начинаться только с заглавной буквы
      - Допускаются пробелы, для отделения имени от фамилии, либо если несколько авторов
      - Допускается символ точки, для сокращения
      - Допускается символ "-" для двойных фамилий
      - Ограничим количество символов в поле до 30
     */

    @Pattern(regexp = "^((([A-Z]{1}[a-z]*)|([A-Z]{1}\\.)|(([A-Z]{1}[a-z]*)-([A-Z]{1}[a-z]*))){1}(\\s(([A-Z]{1}[a-z]*)" +
            "|([A-Z]{1}\\.)|(([A-Z]{1}[a-z]*)-([A-Z]{1}[a-z]*))){1})*){1,30}$", message = "enter the correct author")
    private String author;

     /* Установим следующие требования для поля название:
      - Название может начинаться только с заглавной буквы
      - Допускаются пробелы
      - Допускается символ точки
      - Допускается символ "-"
      - Допускаютя цифры
      - Ограничим количество символов в поле до 30
     */

    @Pattern(regexp = "[A-Z0-9][A-Za-z0-9\\s*\\-\\.]{1,30}", message = "enter the correct title")
    private String title;

    @Digits(integer = 4, fraction = 0, message = "field value must be digit and less then 4 signs")
    private Integer size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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