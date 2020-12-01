package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//создадим наш пользовательский UserDetailsService

@Service
public class CustomUserDetailsService implements UserDetailsService {

    Logger logger = Logger.getLogger(CustomUserDetailsService.class);

    @Autowired
    private LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User loginUser = loginService.findByLogin(userName);

        if (loginUser == null) {
            logger.info("User with login " + userName + " not found");
            throw new UsernameNotFoundException("Unknown user: " + userName); // если пользователь не найден пробросим ошибку
        }
        logger.info("User with login " + userName + " found");
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(loginUser.getLogin())
                .password(loginUser.getPassword())
                .roles("USER")
                .build();
        logger.info("return user: " + user);
        return user;
    }
}