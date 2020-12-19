package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepository implements ProjectRepository<User> {

    private Logger logger = Logger.getLogger(UserRepository.class);
    //    private List<User> userRepo = new ArrayList<>();
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> retrieveAll() {
        List<User> books = jdbcTemplate.query("SELECT * FROM users", (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurnames(rs.getString("surname"));
            user.setAge(rs.getInt("age"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            return user;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(User user) {

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", user.getName());
        parameterSource.addValue("surname", user.getSurnames());
        parameterSource.addValue("age", user.getAge());
        parameterSource.addValue("email", user.getEmail());
        parameterSource.addValue("login", user.getLogin());
        parameterSource.addValue("password", user.getPassword());
        jdbcTemplate.update("INSERT INTO users (name, surname, age, email, login, password) VALUES (:name, " +
                ":surname, :age, :email, :login, :password)", parameterSource);

        logger.info("store new user" + user);
    }

    // method of searching for a user by login
    @Override
    public User search(String login) {
        User user = null;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("login", login);
        try {
            user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE login = :login", parameterSource, (ResultSet rs, int row) -> {
                User foundUser = new User();
                foundUser.setId(rs.getInt("id"));
                foundUser.setName(rs.getString("name"));
                foundUser.setSurnames(rs.getString("surname"));
                foundUser.setAge(rs.getInt("age"));
                foundUser.setEmail(rs.getString("email"));
                foundUser.setLogin(rs.getString("login"));
                foundUser.setPassword(rs.getString("password"));
                return foundUser;
            });
        } catch (DataAccessException ex) {
            logger.info("Couldn't find login");
        }
        return user;
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