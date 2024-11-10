package com.e_library.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.e_library.models.instances.User;

public class Users extends Objekts<User>{
    // private static String src_user = "src\\main\\resources\\com\\e_library\\Users.json";
    private static String src_user = "/com/e_library/Users.json";
    private static User current_user;
    private static String current_user_id;
    private static Boolean root;

    public Users() {
        super(User.class);
    }

    public List<User> getUsers() {
        return getObjekts(src_user);
    }

    public User getCurrentUser() {
        return current_user;
    }

    public boolean is_valid_info(String name, String surname, String login, String password, LocalDate date) {
        if(date == null) return false;
        System.out.println();
        System.out.println("Date is valid");
        
        if(name.isBlank() || surname.isBlank() || login.isBlank() || password.isBlank()) return false;
        System.out.println("name, surname, login,m password are not blank");
        
        String regex = "[\\p{IsCyrillic}&#+*^%@`~/.<>/|\\\\-]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        
        if(matcher.find()) return false;
        System.out.println("password is valid");
        
        List<User> users = getObjekts(src_user);
        for(User user: users) {
            if(user.getLogin().equals(login)) return false;
        }
        System.out.println("login is not used");

        return true;
    }

    public static void setCurrentUser(User user) {
        current_user = user;
        root = current_user.is_root();
        current_user_id = user.getId();
        System.out.println("Current user setted succesfully!");
        System.out.println(user);
    }

    public static void logout() {
        current_user = null;
        root = false;
    }

    public String getMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for(byte b: messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean root() {
        return root;
    }

    public User getUserById(String id) {
        List<User> users = getUsers();
        for (User user: users) {
            if(user.getId().equals(id)) {
                return user;
            } 
        }

        return null;
    }

    public void addUser(String name, String surname, String login, LocalDate date, String password) {
        User user = new User(
                name,
                surname,
                login,
                Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                getMD5(password)
        );
        createNewObjekt(user, src_user);
    }

    public void saveUser(User user) {
        User old_user = getUserById(user.getId());
        removeObjekt(old_user, src_user);
        createNewObjekt(user, src_user);
    }
}
