package com.e_library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;

import com.e_library.models.Users;
import com.e_library.models.instances.User;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChangeInfoController extends MainController {
    private static User current_user;
    private static Stage current_stage;

    @FXML
    private ImageView user_icon;

    @FXML
    private TextField user_name, user_surname;
    private PasswordField user_current_password, user_new_password;

    @FXML
    private DatePicker user_datebirth;

    @FXML
    public void setData(User current_user, Stage stage) {
        Users users = new Users();
        ChangeInfoController.current_user = current_user;
        user_name.setText(current_user.getName());
        user_surname.setText(current_user.getSurname());
        user_datebirth.setValue(LocalDate.parse(current_user.getDateBirth()));

        try {
            InputStream image = new FileInputStream(current_user.getIcon());
            user_icon.setImage(new Image(image));
        } catch (FileNotFoundException e) {
        }

        user_icon.setOnMouseClicked((e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select png file");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                System.out.println("File selected. You selected: " + file.getAbsolutePath());
                try {
                    current_user.setIcon(file.getAbsolutePath());
                    InputStream image = new FileInputStream(file.getAbsoluteFile());
                    user_icon.setImage(new Image(image));
                    current_user.setIcon(file.getAbsolutePath());
                    users.saveUser(current_user);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("File selection. No file is selected");
            }
        });

        current_stage = stage;
        stage.setOnCloseRequest((e) -> {
            ChangeInfoController.current_user = null;
            ChangeInfoController.current_stage = null;
        });
    }

    public void save_changes() {
        Users users = new Users();
        String current_password = user_current_password.getText();
        if (!current_password.equals("")) {
            String new_password = user_new_password.getText();
            if (current_user.check_pass(current_password)) {
                current_user.setNewPassword(new_password);
                users.saveUser(current_user);
                current_stage.close();
                start_window("change_info_user_success");
            } else {
                start_window("change_info_user_fail");
            }
        }

    }
}
