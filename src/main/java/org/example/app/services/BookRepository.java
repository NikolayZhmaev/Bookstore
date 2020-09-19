package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();


    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        book.setId(book.hashCode());
        logger.info("store new book: " + book);
        repo.add(book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("removing a book by id " + book.getId());
                return repo.remove(book);
            }
        }
        return false;
    }

    //сделаем метод удаления кних по нескольким параметрам
    @Override
    public boolean remove(Book book) {
        int sizeRepo = repo.size();
        if (book.getId() != null) {
            removeItemById(book.getId());
            logger.info("current repository Books size: " + repo.size());
        }
        if (book.getAuthor().length() != 0) {
            removeByAuthor(book.getAuthor());
            logger.info("current repository Books size: " + repo.size());
        }
        if (book.getTitle().length() != 0) {
            removeByTitle(book.getTitle());
            logger.info("current repository Books size: " + repo.size());
        }
        if (book.getSize() != null) {
            removeBySize(book.getSize());
            logger.info("current repository Books size: " + repo.size());
        }
        if (sizeRepo != repo.size()) {
            return true;
        }
        return false;
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
        Set<Book> filterBookByAutor = new HashSet<>();
        for (Book book : repo) {
            if (book.getAuthor().equals(author)) {
                filterBookByAutor.add(book);
            }
        }
        return filterBookByAutor;
    }

    // получаем список книг по названию
    public Set<Book> getBooksByTitle(String title) {
        Set<Book> filterBookByTitle = new HashSet<>();
        for (Book book : repo) {
            if (book.getTitle().equals(title)) {
                filterBookByTitle.add(book);
            }
        }
        return filterBookByTitle;
    }

    // получаем список книг по количеству страниц
    public Set<Book> getBooksBySize(Integer size) {
        Set<Book> filterBookBySize = new HashSet<>();
        for (Book book : repo) {
            if (book.getSize().intValue() == size.intValue()) {
                filterBookBySize.add(book);
            }
        }
        return filterBookBySize;
    }

    //удаление всех книг автора
    public void removeByAuthor(String author) {
        logger.info("removing a books by author " + author);
        Iterator<Book> iter = repo.iterator();
        while (iter.hasNext()) {
            if (iter.next().getAuthor().equals(author)) {
                iter.remove();
            }
        }
    }

    //удаление всех книг по названию
    public void removeByTitle(String title) {
        logger.info("removing a books by title " + title);
        Iterator<Book> iter = repo.iterator();
        while (iter.hasNext()) {
            if (iter.next().getTitle().equals(title)) {
                iter.remove();
            }
        }
    }

    //удаление всех книг по количеству страниц
    public void removeBySize(Integer size) {
        logger.info("removing a books by size " + size);
        Iterator<Book> iter = repo.iterator();
        while (iter.hasNext()) {
            if (iter.next().getSize().intValue() == size.intValue()) {
                iter.remove();
            }
        }
    }

    // пока отложим реализацию
    @Override
    public Book search(String name) {
        return null;
    }
}