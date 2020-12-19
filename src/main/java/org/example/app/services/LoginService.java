package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);
    private final ProjectRepository<User> userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(ProjectRepository<User> userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;

        //добавим в БД администратора со следующими параметрами
        User userAdmin = new User();
        userAdmin.setName("Admin");
        userAdmin.setSurnames("Admin");
        userAdmin.setAge(35);
        userAdmin.setEmail("admin@mail.ru");
        userAdmin.setLogin("admin");
        userAdmin.setPassword("admin");

        saveUser(userAdmin);
    }

    public List<User> getAllUsers() {
        return userRepo.retrieveAll();
    }

    // method for adding a new user to the database
    public void saveUser(User user) {
        //перед сохранением выполним хэширование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public User findByLogin(String login) {
        logger.info("try find user by login: " + login);
        User user = userRepo.search(login);
        return user;
    }
}