package com.e_library.models;
import java.util.concurrent.TimeUnit;

import com.e_library.models.instances.Book;
import com.github.javafaker.Faker;

public class Generator {
    public static String src_books = "/com/e_library/Books.json";

    public static void main(String[] args) {
        Books bks = new Books();
        Faker faker = new Faker();

        for(int i = 1; i <= 100; i++) {
            bks.createNewObjekt(new Book(
                faker.book().title(),
                faker.book().author(),
                faker.book().genre(),
                faker.date().past(365*100, TimeUnit.DAYS),
                faker.date().past(365*100, TimeUnit.DAYS),
                faker.lorem().paragraph()
            ), src_books);
            System.out.println("Book #" + i + " created");
        }
    }
}
