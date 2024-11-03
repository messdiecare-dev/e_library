package com.e_library;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.e_library.models.Users;
import com.e_library.models.instances.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {
    private final String users_src = "src\\main\\resources\\com\\e_library\\Users.json";
    private final String books_src = "src\\main\\resources\\com\\e_library\\Books.json";

    @FXML
    private TextField login_field, login_password;

    @FXML
    private TextField reg_name, reg_surname, reg_login, reg_password;

    @FXML
    private DatePicker reg_datebirth;

    @FXML
    private Button reg_button, reg_success_button, login_fail_button, login_success_button, book_create_success_button;

    public void login() {
        Users usr = new Users();
        List<User> users = usr.getObjekts(users_src);
        String login = login_field.getText();
        Stage stage = (Stage) login_field.getScene().getWindow();
        
        String password = login_password.getText();
        for (User user: users) {
            if(user.getLogin().equals(login) && user.check_pass(password)) {
                Users.setCurrentUser(user);
                stage.close();
                start_window("login_success");

            } else {
                start_window("login_fail");
            }
        }

    }

    public void open_register_window() {
        start_window("Register");
    }

    public void create_new_user() {
        Users users = new Users();
        Stage stage1 = (Stage) reg_button.getScene().getWindow();
        
        User user = new User(
            reg_name.getText(),
            reg_surname.getText(),
            reg_login.getText(),
            Date.from(reg_datebirth.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            users.getMD5(reg_password.getText())
        );
        users.createNewObjekt(user, users_src);
        stage1.close();
        System.out.println("User created succesfully");
        start_window("reg_success");

        
    }

    protected void start_window(String window) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window + ".fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reg_success_close() {
        Stage stage = (Stage) reg_success_button.getScene().getWindow();
        stage.close();
    }

    public void login_fail_close() {
        Stage stage = (Stage) login_fail_button.getScene().getWindow();
        stage.close();
    }

    public void login_success_close() {
        Stage stage = (Stage) login_success_button.getScene().getWindow();
        start_window("Books");
        stage.close();
    }

    public void open_book() {
        start_window("Book");
    }

    // public void book_change_visibility() {
    //     book_name.setEditable(!book_name.isEditable());
    //     book_author.setEditable(!book_author.isEditable());
    //     book_dateAdding.setEditable(!book_dateAdding.isEditable());
    //     book_dateCreated.setEditable(!book_dateCreated.isEditable());
    //     book_annotation.setEditable(!book_annotation.isEditable());
    //     book_create_button.setDisable(!book_create_button.isDisabled());
    // }

    // public void create_new_book() {
    //     Books bks = new Books();
    //     Book book = new Book(
    //         book_name.getText(),
    //         book_author.getText(),
    //         book_genre.getText(),
    //         Date.from(book_dateAdding.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
    //         Date.from(book_dateCreated.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
    //         book_annotation.getText()
    //     );

    //     bks.createNewObjekt(book, books_src);
    //     start_window("book_created_success");
    // }

    public void book_create_success_close() {
        Stage stage = (Stage) book_create_success_button.getScene().getWindow();
        stage.close();
    }
}
