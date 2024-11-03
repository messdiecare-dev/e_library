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
        if (json.equals("")) {
            return new ArrayList<>();
        }
        Type type_of_objekt = TypeToken.getParameterized(List.class, type).getType();
        List<T> users = gson.fromJson(json, type_of_objekt);

        return users;
    }

    public void createNewObjekt(T objekt, String src) {
        Gson gson = new Gson();
        List<T> objekts = getObjekts(src);
        try (FileWriter fw = new FileWriter(src)) {
            objekts.add(objekt);
            fw.write(gson.toJson(objekts));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeObjekt(T objekt, String src) {
        Gson gson = new Gson();
        List<T> objekts = getObjekts(src);
        objekts.remove(objekt);
        
        try (FileWriter fw = new FileWriter(src)) {
            fw.write(gson.toJson(objekts));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String fromFileToString(String src) {
        try {
            Path fileName = Path.of(src);
            return Files.readString(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
