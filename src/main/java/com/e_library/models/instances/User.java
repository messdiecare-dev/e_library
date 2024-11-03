package com.e_library.models.instances;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.e_library.models.Users;

public class User {
    private String name;
    private String surname;
    private Date birth;
    private String password;
    private String login;
    private Boolean root = true;
    private String id;
    private String user_icon = "src\\main\\resources\\com\\e_library\\user.png";

    public User(String name, String surname, String login, Date birth, String password) {
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.login = login;
        this.password = password;
        this.id = newId();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }
    public String getLogin() {
        return this.login;
    }

    public String getDateBirth() {
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        // System.out.println(df.format(this.dateAdded));
        return df.format(this.birth);
    }

    public Boolean is_root() {
        return this.root;
    }

    public Boolean check_pass(String password) {
        return new Users().getMD5(password).equals(this.password);
    }

    public void setNewPassword(String password) {
        String newPassword = new Users().getMD5(password);
        this.password = newPassword;
    }

    public String getIcon() {
        return this.user_icon;
    }

    public void setIcon(String newIcon) {
        this.user_icon = newIcon;
    }

    public String newId() {
        Users usrs = new Users();
        List<User> users = usrs.getUsers();
        Random rand = new Random();
        String number;

        while (true) {
            number = String.valueOf(rand.nextInt(9_000_000) + 1_000_000);
            if (users.isEmpty()) {
                return number;
            }
            boolean unique = true;

            for (User user : users) {
                if (user.getId().equals(number)) {
                    unique = false;
                    break;
                }
            }

            if (unique) {
                System.out.println("number created");
                return number;
            }
        }
    }
}
