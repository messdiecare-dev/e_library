package com.e_library.models.instances;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Book {
    public String name, author, genre, annotation;
    private String image = "src\\main\\resources\\com\\e_library\\book.png";
    private Date dateAdded, dateCreated;
    private List<BookInstance> instances = new ArrayList<>();

    public Book(String name, String author, String genre, Date dateAdded, Date dateCreated, String annotation,
            String image) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.annotation = annotation;
        this.image = image;
        this.dateAdded = dateAdded;
        this.dateCreated = dateCreated;
    }

    public Book(String name, String author, String genre, Date dateAdded, Date dateCreated, String annotation) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.annotation = annotation;
        this.dateAdded = dateAdded;
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getDateAdded() {
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        // System.out.println(df.format(this.dateAdded));
        return df.format(this.dateAdded);
    }

    public void setDateAdded(LocalDate date) {
        this.dateAdded = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public String getDateCreated() {
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        // System.out.println(df.format(this.dateAdded));
        return df.format(this.dateCreated);
    }

    public void setDateCreated(LocalDate date) {
        this.dateCreated = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public String getImage() {
        // FileOutputStream img = new FileOutputStream(new File(this.image));
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setInstances(List<BookInstance> instances) {
        this.instances = instances;
    }

    public void addInstance(BookInstance instance) {
        // BookInstance book_instance = new BookInstance("123", "status");
        instances.add(instance);
    }

    public List<BookInstance> getInstancesList() {
        // this.book_instances = FXCollections.observableArrayList(book_instances);
        // return this.instances;
        return this.instances;
    }

    public boolean removeInstance(String number) {
        for (BookInstance instance : this.instances) {
            if (instance.getNumber().equals(number)) {
                this.instances.remove(instance);
                return true;
            }
        }

        return false;
    }

    public String newNumber() {
        Random rand = new Random();
        String number;

        while (true) {
            number = String.valueOf(rand.nextInt(9_000_000) + 1_000_000);
            if (instances.isEmpty()) {
                return number;
            }
            boolean unique = true;

            for (BookInstance instance : instances) {
                if (instance.getNumber().equals(number)) {
                    unique = false;
                    break;
                }
            }

            if (unique) {
                System.out.println("number created");
                return number;
            }
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", annotation='" + annotation + '\'' +
                ", dateAdded=" + dateAdded +
                ", dateCreated=" + dateCreated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return name.equals(book.name) && author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, genre, annotation, dateAdded, dateCreated);
    }

}
