package com.e_library;

import java.util.List;
import java.util.Random;

import com.e_library.models.Books;
import com.e_library.models.instances.Book;
import com.e_library.models.instances.BookInstance;

public class Code {
    public static List<BookInstance> instances;
    public static String src_books = "src\\main\\resources\\com\\e_library\\Books.json";

    public static void main(String[] args) {
        Books books = new Books();
        Book book = books.getObjekts(src_books).get(0);
        instances = book.getInstancesList();
        instances.add(new BookInstance("12345", "available", ""));
        instances.add(new BookInstance("1234sdfsdf5", "avaidflable", ""));
        System.out.println(newNumber());
    }

    public static String newNumber() {
        Random rand = new Random();
        String number = " ";
        if (instances.isEmpty()) {
            return number;
        }
        
        while (true) {
            number = String.valueOf(rand.nextInt( 9_000_000) + 1_000_000);
            boolean unique = true;

            for(BookInstance instance: instances) {
                if(instance.getNumber().equals(number)) {
                    unique = false;
                    break;
                }
            }

            if (unique) return number;
        }
    }
}
