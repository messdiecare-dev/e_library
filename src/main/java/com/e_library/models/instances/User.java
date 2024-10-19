package com.e_library.models.instances;
import java.util.Date;

import com.e_library.models.Users;

public class User{
    private String name;
    private String surname;
    private Date birth;
    private String password;
    private String login;
    private Boolean root = false;

    public User(String name, String surname, String login, Date birth, String password) {
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.login = login;
        this.password = password;
    }

    public String[] getNameSurname() {
        return new String[]{this.name, this.surname};
    }

    public String getLogin() {
        return this.login;
    }

    public Date getDateBirth() {
        return this.birth;
    }

    public Boolean is_root() {
        return this.root;
    }

    public Boolean check_pass(String password) {
        return new Users().getMD5(password).equals(this.password);
    }
}
