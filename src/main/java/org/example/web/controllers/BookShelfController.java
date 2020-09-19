package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        if (bookService.validateBook(book)) {
            bookService.saveBook(book);
            logger.info("current repository Books size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        } else {
            logger.info("entered an invalid value, number of books:" + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/action")
    public String action(Model model, Book book, @ModelAttribute(name = "action") String name) {
        if (name.equals("remove")) {
           return removeBook(book);
        }
        return filterBooks(model, book);
    }

//    @GetMapping("/filter")
    public String filterBooks(Model model, Book book) {
        if (bookService.validateBook(book)) {
            logger.info("got filter books");
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getFilterBook(book));
            return "book_shelf";
        }
        return "redirect:/books/shelf";
    }

    // перепишим метод удаления книг
    //   @PostMapping("/remove")
    public String removeBook(Book book) {
        if (bookService.remove(book)) {
            return "redirect:/books/shelf";
        } else {
            logger.info("entered an invalid value.");
            return "redirect:/books/shelf"; // баг был в этом месте, будем обновлять страницу без удаления данных
        }
    }
}