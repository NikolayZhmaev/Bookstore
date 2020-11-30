package org.example.app.config;

import org.apache.log4j.Logger;
import org.example.app.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = Logger.getLogger(AppSecurityConfig.class);


    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("popular in memory auth user");

        auth.userDetailsService(customUserDetailsService);


//        auth
//                .inMemoryAuthentication()
//                .withUser("root")
//                .password(passwordEncoder().encode("123"))
//                .roles("USER");

    }

    //метод шифрования пароля
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // способ шифрования
    }

    // метод передающий нам объект HttpSecurity
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        logger.info("config http security");
//        http
//                .csrf()
//                .disable()
//                .authorizeRequests()
////                //доступ только для не зарегистрированных пользователей
////                .antMatchers("/registration").not().fullyAuthenticated()
//                //доступ только для пользователей с ролью "USER"
//                .antMatchers("/books/shelf").hasAnyRole("USER")
//                //доступ разрешен всем пользователям
//                .antMatchers("/login/**", "/registration/**").permitAll()
//                //все остальные страницы требуют аутентификации
//                .anyRequest().authenticated()
//                .and()
//                //настройка для входа в систему
//                .formLogin()
//                .loginPage("/login")
//                //перенаправление на страницу /books/shelf после успешного входа
//                .defaultSuccessUrl("/books/shelf", true)
//                .failureUrl("/login");


        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/login/**", "/registration/**").permitAll()
                .antMatchers("/books/shelf").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/auth")
                .defaultSuccessUrl("/books/shelf", true)
                .failureUrl("/login");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        logger.info("config web security");

        web
                .ignoring()
                .antMatchers("/images/**");
    }
}
