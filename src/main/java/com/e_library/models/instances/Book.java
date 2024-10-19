package com.e_library.models.instances;

// import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.*;

public class Book {
    private String name, author, annotation;
    private String image = "C:\\Users\\destr\\Documents\\javafx-proj\\javafx\\src\\book.png";
    private Date dateAdded, dateCreated;
    private List<BookInstance> instances = new ArrayList<BookInstance>();
    public Book(String name, String author, Date dateAdded, Date dateCreated, String annotation, String image) {
        this.name = name;
        this.author = author;
        this.annotation = annotation;
        this.image = image;
        this.dateAdded = dateAdded;
        this.dateCreated = dateCreated;
    }

    public Book(String name, String author, Date dateAdded, Date dateCreated, String annotation) {
        this.name = name;
        this.author = author;
        this.annotation = annotation;
        this.dateAdded = dateAdded;
        this.dateCreated = dateCreated;
    }

    public String[] getNameAuthorAnnotation() {
        return new String[]{this.name, this.author, this.annotation};
    }

    public Date getDateAdded() {
        return this.dateAdded;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public String getImage() {
        // FileOutputStream img = new FileOutputStream(new File(this.image));
        return this.image;
    }

    public void addInstance(ArrayList<String> instance) {
        BookInstance book_instance = new BookInstance();
        instances.add(book_instance);
    }

    public List<BookInstance> getInstancesList() {
        return this.instances;
    }
}
