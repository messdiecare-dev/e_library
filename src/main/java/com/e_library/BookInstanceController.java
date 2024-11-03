package com.e_library;

import com.e_library.models.instances.Book;
import com.e_library.models.instances.BookInstance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookInstanceController extends MainController {
    private static Book current_book;
    private static BookInstance current_instance;
    private static Stage current_stage;

    @FXML
    private ChoiceBox<String> status_choicebox;

    @FXML
    private TextField number;

    public void setData(BookInstance instance, Book book, Stage stage) {
        current_book = book;
        current_stage = stage;

        if (instance == null) {
            current_instance = new BookInstance(current_book.newNumber(), "Доступний");
        } else {
            current_instance = instance;
        }

        number.setText(current_instance.getNumber());

        ObservableList<String> options = FXCollections.observableArrayList("Доступний", "Втрачений", "Виданий читачу");
        status_choicebox.setItems(options);
        status_choicebox.setValue(current_instance.getStatus());

        stage.setOnCloseRequest((e) -> {
            current_book = null;
            current_instance = null;
        });
    }

    public void save_instance() {
        current_instance.setStatus(status_choicebox.getValue());
        ObservableList<BookInstance> instances = new BookController().instances;
        instances.remove(current_instance);
        instances.add(current_instance);
        current_stage.close();
        start_window("instance_created_success");
    }
}
