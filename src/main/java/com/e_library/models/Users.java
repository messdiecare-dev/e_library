package com.e_library.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.e_library.models.instances.User;

public class Users extends Objekts<User>{
    private static User current_user;
    private static Boolean root;
    public Users() {
        super(User.class);
    }
    public static void setCurrentUser(User user) {
        current_user = user;
        root = current_user.is_root();
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
}
