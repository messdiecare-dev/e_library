package com.e_library.models.instances;

public class BookInstance {
    private String number, status;

    public BookInstance(String number, String status) {
        this.number = number;
        this.status = status;
    }

    public String getNumber() {
        return this.number;
    }

    public String getStatus() {
        return this.status;
    }

    public void setNumber(String number) {
        try {
            Integer.valueOf(number);
            this.number = number;
        } catch (NumberFormatException e) {
            System.out.println("Not correct number input for book_instance");
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
