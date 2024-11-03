package com.e_library.models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.e_library.models.instances.Book;
import com.google.gson.Gson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Books extends Objekts<Book>{
    private static ObservableList<Book> books_list = FXCollections.observableArrayList();
    public static String src_books = "src\\main\\resources\\com\\e_library\\Books.json";

    public Books() {
        super(Book.class);
    }    
    
    public ObservableList<Book> get_books_list() {
        books_list = FXCollections.observableArrayList(getObjekts(src_books));
        return books_list;
    }

    public void set_books_list(ObservableList<Book> books) {
        books_list = books;
    }

    public void add_book_to_list(Book book) {
        createNewObjekt(book, src_books);
    }

    public void remove_book(Book book) {
        removeObjekt(book, src_books);
        books_list.remove(book);
    }

    @Override
    public void createNewObjekt(Book book, String src) {
        Gson gson = new Gson();
        List<Book> objects = getObjekts(src);
        try (FileWriter fw = new FileWriter(src)) {
            objects.add(book);
            books_list.add(book);
            fw.write(gson.toJson(objects));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
