package com.e_library;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected Map<String, String> titles = new HashMap<>();
    public static List<String> windows = new ArrayList<>();

    @FXML
    private TextField login_field, login_password;

    @FXML
    private TextField reg_name, reg_surname, reg_login, reg_password;

    @FXML
    private DatePicker reg_datebirth;

    @FXML
    private Button reg_button, reg_success_button, login_fail_button, login_success_button, book_create_success_button, reg_fail_button, book_changed_success_button;

    public void login() {
        Users usr = new Users();
        List<User> users = usr.getUsers();
        System.out.println(users.toString());
        String login = login_field.getText();
        Stage stage = (Stage) login_field.getScene().getWindow();
        
        
        String password = login_password.getText();
        boolean found = false;
        for (User user: users) {
            if(user.getLogin().equals(login) && user.check_pass(password)) {
                Users.setCurrentUser(user);
                System.out.println(user.toString());
                windows.remove("Login");
                stage.close();
                start_window("login_success", "Успішно авторизовані");
                found = true;
                break;
            }
        }

        if(!found) start_window("login_fail", "Помилка в авторизації");

    }

    public void open_register_window() {
        start_window("Register", "Реєстрація");
    } 

    public void create_new_user() {
        Users users = new Users();
        Stage stage1 = (Stage) reg_button.getScene().getWindow();

        String name = reg_name.getText();
        String surname = reg_surname.getText();
        String login = reg_login.getText();
        String password = reg_password.getText();
        LocalDate date = reg_datebirth.getValue();
        
        if(!users.is_valid_info(name, surname, login, password, date)) {
            start_window("reg_fail", "Помилка в реєстрації");
        } else {
            users.addUser(name, surname, login, date, password);
            
            stage1.close();
            System.out.println("User created succesfully");
            start_window("reg_success", "Успішно зареєстровані");
        }
    }

    protected boolean start_window(String window, String title) {
        try {
            if (is_opened(window)) return false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window + ".fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle(title);
            windows.add(window);
            newStage.setOnCloseRequest(e -> windows.remove(window));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void reg_success_close() {
        windows.remove("reg_success");
        windows.remove("Register");
        Stage stage = (Stage) reg_success_button.getScene().getWindow();
        stage.close();
    }

    public void reg_fail_close() {
        windows.remove("reg_fail");
        Stage stage = (Stage) reg_fail_button.getScene().getWindow();
        stage.close();
    }

    public void login_fail_close() {
        windows.remove("login_fail");
        Stage stage = (Stage) login_fail_button.getScene().getWindow();
        stage.close();
    }

    public void login_success_close() {
        windows.remove("login_success");
        Stage stage = (Stage) login_success_button.getScene().getWindow();
        stage.close();
        System.out.println("start");
        
        start_window("Books", "Список книг");
        System.out.println("end");
    }

    public void open_book() {
        start_window("Book", "Книга");
    }
    
    public void book_create_success_close() {
        windows.remove("book_created_success");
        Stage stage = (Stage) book_create_success_button.getScene().getWindow();
        stage.close();
    }

    public void book_changed_success_close() {
        windows.remove("book_changed_success");
        Stage stage = (Stage) book_changed_success_button.getScene().getWindow();
        stage.close();
    }

    public boolean is_opened(String title) {
        System.out.println(); //TODO
        for(String window: windows) {
            System.out.println(window);
            if(window.equals(title)) return true;
        }
        System.out.println();

        return false;
    }
}
