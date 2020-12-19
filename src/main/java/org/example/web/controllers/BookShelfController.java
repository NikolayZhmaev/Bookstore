package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.app.services.FileService;
import org.example.web.dto.Book;
import org.example.web.dto.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;
    private FileService fileService; // new

    @Autowired
    public BookShelfController(BookService bookService, FileService fileService) {
        this.bookService = bookService;
        this.fileService = fileService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("filter", new Filter());
        model.addAttribute("bookList", bookService.getAllBooks());
        model.addAttribute("nameFiles", fileService.getNameFiles()); //new
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.info("error when saving a book:" + bookService.getAllBooks().size());
            model.addAttribute("book", book);
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("nameFiles", fileService.getNameFiles()); //new
            return "book_shelf";
        } else {
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
            model.addAttribute("nameFiles", fileService.getNameFiles());
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
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        fileService.uploadFile(file);

        return "redirect:/books/shelf";
    }

    @GetMapping("/downloadFile")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");

        Path file = Paths.get(String.valueOf(dir), fileName);

        if (Files.exists(file)) {
            response.setHeader("Content-disposition", "attachment; filename = " + fileName);
            response.setContentType("application/octet-stream");
            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}