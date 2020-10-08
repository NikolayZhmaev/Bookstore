package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
        model.addAttribute("filter", new Filter());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info("error when saving a book:" + bookService.getAllBooks().size());
            model.addAttribute("book", book);
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        }else {
            bookService.saveBook(book);
            logger.info("current repository Books size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping(value = "/action")
    public String action(Filter filter, Model model, @ModelAttribute(name = "action") String name) {
        if (name.equals("remove")) {
            return removeBook(filter);
        }
        return filterBooks(model, filter);
    }

    //    @GetMapping("/filter")
    public String filterBooks(Model model, Filter filter) {
        if (bookService.validateBook(filter)) {
            logger.info("got filter books");
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getFilterBook(filter));
            return "book_shelf";
        }
        return "redirect:/books/shelf";
    }

    // перепишим метод удаления книг
    //   @PostMapping("/remove")
    public String removeBook(Filter filter) {
        if (bookService.remove(filter)) {
            return "redirect:/books/shelf";
        } else {
            logger.info("entered an invalid value.");
            return "redirect:/books/shelf"; // баг был в этом месте, будем обновлять страницу без удаления данных
        }
    }
}