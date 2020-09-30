package org.example.web.dto;

import javax.validation.constraints.*;

public class User {

    private Integer id;

    /* Нам необходимо чтобы при регистрации пользователь указывал реальные имя и фамилию, а значит надо проверить чтобы:
        - строка не была пустой,не менее 2-х символов и не более 12
        - начиналась с заглавной буквы
        - остальные буквы были маленькие
        - небыло символов и цифр
        - буквы русского алфавита
    */
    @Pattern(regexp = "[А-Я][а-я]{1,12}", message = "Invalid name")
    private String name;

    @Pattern(regexp = "[А-Я][а-я]{1,12}", message = "Invalid surnames")
    private String surnames;

    /* Поле возраста должно соответствовать следующим критериям:
      - не должно быть null
      - минимальный возраст 10 лет
    */
    @NotNull
    @Min(value = 10, message = "Invalid age")
    private Integer age;

    @Email
    private String email;

    /* Логин должен соответствовать следующим критериям:
     - только латинские буквы любого регистра, не менее 2 и не более 12 символов
     - любые цифры и дефис
     - ни каких пробелов
   */
    @Pattern(regexp = "[a-zA-Z0-9\\-*]{2,12}", message = "Invalid login")
    private String login;

    /* Пароль должен соответствовать следующим критериям:
         - только латинские буквы любого регистра, не менее 6 и не более 10 символов
         - любые цифры
         - ни каких пробелов
   */
    @Pattern(regexp = "[a-zA-Z0-9]{6,10}", message = "Invalid password")
    private String password;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surnames='" + surnames + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}