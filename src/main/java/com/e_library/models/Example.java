package com.e_library.models;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e_library.models.instances.Book;
import com.e_library.models.instances.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Example {
    public static String src = "src\\main\\resources\\com\\e_library\\Users.json";
    public static void main(String[] args) {
        Date date = new Date();
        User user1 = new User("Name1", "Surname1", "login1", date, "12345");
        User user2 = new User("Name2", "Surname2", "login2", date, "12345");

        Book book1 = new Book("Name", "Author", date, date, "annotation", "image");
        Book book2 = new Book("Name", "Author", date, date, "annotation", "image");

        Gson gson = new Gson();
        try (FileWriter fw = new FileWriter(src)) {
            List<User> users = new ArrayList<>();
            users.add(user1);
            fw.write(gson.toJson(users));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = fromFileToString(src);
        Type type = new TypeToken<List<User>>(){}.getType();
        System.out.println(type);
        List<User> users = gson.fromJson(json, type);
        users.add(user2);
        System.out.println(users.toString());

        try (FileWriter fo = new FileWriter(src)){
            fo.write(gson.toJson(users));
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String fromFileToString(String src) {
        StringBuilder str = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(src))) {
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();
    }
}
