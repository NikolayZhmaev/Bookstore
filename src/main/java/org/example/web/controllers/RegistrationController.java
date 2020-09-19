package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.LoginService;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final Logger logger = Logger.getLogger(RegistrationController.class);
    private final LoginService loginService;

    @Autowired
    public RegistrationController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String registration(Model model) {
        logger.info("GET /registration returns registration_form.html");
        model.addAttribute("user", new User());
        return "registration_form";
    }

    @PostMapping("/save")
    public String saveUser(User user) {
        if (loginService.checkSaveUser(user)) {
            loginService.saveUser(user);
            logger.info("current repository Users size:" + loginService.getAllUsers().size());
            return "redirect:/login";
        }
        logger.info("entered an invalid value, user " + user.toString() + ". Number of Users:" + loginService.getAllUsers().size());
        return "redirect:/registration";
    }
}