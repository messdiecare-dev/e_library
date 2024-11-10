package com.e_library;

import com.e_library.models.Users;
import com.e_library.models.instances.Book;
import com.e_library.models.instances.BookInstance;
import com.e_library.models.instances.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookInstanceController extends MainController {
    private static Book current_book;
    private static BookInstance current_instance;
    private static Stage current_stage;
    private static Stage book_stage;

    @FXML
    private ChoiceBox<String> status_choicebox;

    @FXML
    private Label owner_label;

    @FXML
    private TextField number, owner_name;

    @FXML
    private Button instance_create_success_button, instance_delete_button;

    public void setData(BookInstance instance, Book book, Stage stage, Stage stage_book) {
        current_book = book;
        current_stage = stage;
        book_stage = stage_book;

        if (instance == null) {
            current_instance = new BookInstance(current_book.newNumber(), "Доступний", "");
        } else {
            current_instance = instance;
        }

        number.setText(current_instance.getNumber());

        ObservableList<String> options = FXCollections.observableArrayList("Доступний", "Втрачений", "Виданий читачу");
        status_choicebox.setItems(options);
        status_choicebox.setValue(current_instance.getStatus());
        if(status_choicebox.getValue().equals("Виданий читачу") && !current_instance.getOwner().isBlank()) {
            owner_label.setVisible(true);
            owner_name.setVisible(true);
            owner_name.setDisable(true);

            User user = new Users().getUserById(current_instance.getOwner());
            owner_name.setText(user.getName() + " " + user.getSurname());
        }

        stage.setOnCloseRequest((e) -> {
            current_book = null;
            current_instance = null;
            book_stage = null;
            windows.remove("BookInstance");
        });
    }

    public void save_instance() {
        ObservableList<BookInstance> instances = BookController.instances;
        instances.remove(current_instance);
        String status = status_choicebox.getValue();
        current_instance.setStatus(status);
        if(!status.equals("Виданий читачу")) current_instance.setOwner("");
        instances.add(current_instance);
        TableView<BookInstance> table = (TableView<BookInstance>) book_stage.getScene().lookup("#book_instances");
        table.refresh();

        // System.out.println();
        // for(BookInstance instance: instances) {
        //     System.out.print("Number: "+ instance.getNumber());
        //     switch(instance.getStatus()) {
        //         case "Доступний":
        //             System.out.print("; Status: available");
        //             break;
        //         case "Втрачений":
        //             System.out.print("; Status: lost");
        //             break;
        //         case "Виданий читачу":
        //             System.out.print("; Status: given");
        //             break;
        //         default:
        //             System.out.println("; Status: undefined");
        //     }
        //     System.out.print('\n');
        // }
        // System.out.println();

        current_stage.close();
        windows.remove("BookInstance");
        start_window("instance_created_success", "Успішно додали примірник");
    }

    public void delete_instance() {
        ObservableList<BookInstance> instances = BookController.instances;
        current_stage.close();
        windows.remove("BookInstance");
        instances.remove(current_instance);

        TableView<BookInstance> table = (TableView<BookInstance>) book_stage.getScene().lookup("#book_instances");
        table.refresh();

        start_window("instance_deleted", "Видалено примірник");
    }

    public void instance_create_success_close() {
        Stage stage = (Stage) instance_create_success_button.getScene().getWindow();
        windows.remove("instance_created_success");
        stage.close();
    }

    public void instance_deleted() {
        Stage stage = (Stage) instance_delete_button.getScene().getWindow();
        windows.remove("instance_deleted");
        stage.close();
    }
}
