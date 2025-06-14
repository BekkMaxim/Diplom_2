package ru.praktikum.yandex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private String email;
    private String password;

    public User (String email, String password){
        this.email = email;
        this.password = password;
    }
}
