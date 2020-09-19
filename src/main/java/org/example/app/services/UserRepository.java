package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepository implements ProjectRepository<User> {

    private Logger logger = Logger.getLogger(UserRepository.class);
    private List<User> userRepo = new ArrayList<>();

    @Override
    public List<User> retreiveAll() {
        return new ArrayList<>(userRepo);
    }

    @Override
    public void store(User user) {
        user.setId(user.hashCode());
        logger.info("store new user" + user);
        userRepo.add(user);
    }

    // метод поиска пользователя по лигину
    @Override
    public User search(String login) {
        for (User user : userRepo) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    // в задании не указано реализовать удаление пользователя, пока отложим этот метод
    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        return false;
    }

    // оставим реализацию этого метода на потом
    @Override
    public boolean remove(User user) {
        return false;
    }

    // реализация метода не требуется
    @Override
    public Set<User> filter(User user) {
        return null;
    }
}