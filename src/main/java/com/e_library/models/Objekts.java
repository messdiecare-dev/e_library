package com.e_library.models;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
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
        URL fileUrl = Objekts.class.getResource(src);

        if(fileUrl != null) {
            try(OutputStream outputStream = new FileOutputStream(Paths.get(fileUrl.toURI()).toFile())) {
                objekts.add(objekt);
                outputStream.write(gson.toJson(objekts).getBytes());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeObjekt(T objekt, String src) {
        Gson gson = new Gson();
        List<T> objekts = getObjekts(src);
        objekts.remove(objekt);
        
        URL fileUrl = Objekts.class.getResource(src);
        if(fileUrl != null) {
            try(OutputStream outputStream = new FileOutputStream(Paths.get(fileUrl.toURI()).toFile())) {
                outputStream.write(gson.toJson(objekts).getBytes());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    protected static String fromFileToString(String src) {
        try(InputStream inputStream = Objekts.class.getResourceAsStream(src)) {
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder content = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                content.append(line);
            }

            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
