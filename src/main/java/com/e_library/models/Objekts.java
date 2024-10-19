package com.e_library.models;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

abstract class Objekts<T> {
    private final Class<T> type;

    public Objekts(Class<T> type) {
        this.type = type;
    }

    public List<T> getObjekts(String src) {
        Gson gson = new Gson();
        String json = fromFileToString(src);
        System.out.println("Json: ");
        System.out.println(json);
        if (json.equals("")) {
            return new ArrayList<>();
        }
        Type type_of_objekt = TypeToken.getParameterized(List.class, type).getType();
        List<T> users = gson.fromJson(json, type_of_objekt);

        return users;
    }

    public void createNewObjekt(T object, String src) {
        Gson gson = new Gson();
        List<T> objects = getObjekts(src);
        try (FileWriter fw = new FileWriter(src)) {
            objects.add(object);
            System.out.println("Objekts:");
            System.out.println(objects.toString());
            fw.write(gson.toJson(objects));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String fromFileToString(String src) {
        String str = "";
        try {
            Path fileName = Path.of(src);
            str = Files.readString(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }
}
