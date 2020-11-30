package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);
    private final ProjectRepository<User> userRepo;

    @Autowired
    public LoginService(ProjectRepository<User> userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.retreiveAll();
    }

    // метод по добавлению нового пользователя в базу
    public void saveUser(User user) {
        userRepo.store(user);
    }

    /* добавим метод проверки вводимых значений, запретив регистрацию пользователя c незаполненными полями, и добавление
       одинаковых логинов
    */
    public boolean checkSaveUser(User user) {
        if (user.getName().length() != 0 && user.getSurnames().length() != 0 && user.getAge() != null && user.getLogin().length() != 0 && user.getPassword().length() != 0) {
            if (userRepo.search(user.getLogin()) == null) {
                return true;
            }
        }
        return false;
    }

    public boolean authenticate(LoginForm loginFrom) {
        logger.info("try auth with user-form: " + loginFrom);
        User user = userRepo.search(loginFrom.getUsername());
        if (user != null && user.getPassword().equals(loginFrom.getPassword())) {
            return true;
        }
        return false;
    }

    public User findByLogin (String login) {
        logger.info("try find user by login: " + login);
        User user = userRepo.search(login);
        return user;
    }
}