package org.example.app.services;

import org.example.web.dto.Book;
import org.example.web.dto.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retrieveAll();
    }

    public Set<Book> getFilterBook(Filter filter) {
        return bookRepo.filter(filter);
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean remove(Filter filter) {
        return bookRepo.remove(filter);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    // добавим метод проверки вводимых значений, запретив сохранять книгу со всеми пустыми полями
    public boolean validateBook(Book book) {
        if (book.getAuthor().length() != 0 || book.getTitle().length() != 0 || book.getSize() != null) {
            return true;
        } else {
            return false;
        }
    }
}