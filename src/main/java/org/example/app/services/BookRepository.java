package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retrieveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books (author, title, size) VALUES (:author, :title, :size)", parameterSource);

        logger.info("store new book: " + book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        int result; // переменная будет хранить результат обращения к базе данных
        result = jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
        if (result == 1) {
            logger.info("removing a book by id " + bookIdToRemove);
            return true;
        }
        return false;
    }

    //сделаем метод удаления кних по нескольким параметрам
    @Override
    public boolean remove(Book book) {
        boolean result = false; // переменная будет хранить результат выполнения действия уделения
        if (book.getId() != null) {
            result = removeItemById(book.getId());
        }
        if (book.getAuthor().length() != 0) {
            result = removeByAuthor(book.getAuthor());
        }
        if (book.getTitle().length() != 0) {
            result = removeByTitle(book.getTitle());
        }
        if (book.getSize() != null) {
            result = removeBySize(book.getSize());
        }
        return result;
    }

    // метод фильтрации книг по одному или нескольким параметрам
    @Override
    public Set<Book> filter(Book book) {
        Set<Book> filterBook = new HashSet<>();
        if (book.getAuthor().length() != 0) {
            logger.info("Filtering books by author.");
            filterBook.addAll(getBooksByAutor(book.getAuthor()));
        }
        if (book.getTitle().length() != 0) {
            logger.info("filtering books by title");
            filterBook.addAll(getBooksByTitle(book.getTitle()));
        }
        if (book.getSize() != null) {
            logger.info("filtering books by size");
            filterBook.addAll(getBooksBySize(book.getSize()));
        }
        return filterBook;
    }

    // получаем список книг по автору
    public Set<Book> getBooksByAutor(String author) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);

        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE author = :author", parameterSource, (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new HashSet<Book>(books);
    }

    // получаем список книг по названию
    public Set<Book> getBooksByTitle(String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);

        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE title = :title", parameterSource, (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new HashSet<Book>(books);
    }

    // получаем список книг по количеству страниц
    public Set<Book> getBooksBySize(Integer size) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("size", size);

        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE size = :size", parameterSource, (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new HashSet<Book>(books);
    }

    //удаление всех книг автора
    public boolean removeByAuthor(String author) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);
        int result; // переменная будет хранить результат обращения к базе данных
        result = jdbcTemplate.update("DELETE FROM books WHERE author = :author", parameterSource);
        if (result == 1) {
            logger.info("removing a books by author " + author);
            return true;
        }
        return false;
    }

    //удаление всех книг по названию
    public boolean removeByTitle(String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        int result; // переменная будет хранить результат обращения к базе данных
        result = jdbcTemplate.update("DELETE FROM books WHERE title = :title", parameterSource);
        if (result == 1) {
            logger.info("removing a books by title " + title);
            return true;
        }
        return false;
    }

    //удаление всех книг по количеству страниц
    public boolean removeBySize(Integer size) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("size", size);
        int result; // переменная будет хранить результат обращения к базе данных
        result = jdbcTemplate.update("DELETE FROM books WHERE size = :size", parameterSource);
        if (result == 1) {
            logger.info("removing a books by size " + size);
            return true;
        }
        return false;
    }

    // пока отложим реализацию
    @Override
    public Book search(String name) {
        return null;
    }
}