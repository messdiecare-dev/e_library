package com.e_library.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.e_library.models.instances.User;

public class Users extends Objekts<User>{
    private static String src_user = "src\\main\\resources\\com\\e_library\\Users.json";
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

    private User getUserById(String id) {
        List<User> users = getUsers();
        for (User user: users) {
            if(user.getId().equals(id)) {
                return user;
            } 
        }

        return null;
    }

    public void saveUser(User user) {
        User old_user = getUserById(user.getId());
        removeObjekt(old_user, src_user);
        createNewObjekt(user, src_user);
    }
}
