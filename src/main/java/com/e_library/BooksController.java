package com.e_library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.e_library.models.Books;
import com.e_library.models.Users;
import com.e_library.models.instances.Book;
import com.e_library.models.instances.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

public class BooksController extends MainController {
    protected Book selectedBook;

    @FXML
    private TableColumn<Book, String> book_name_col, book_author_col, book_genre_col, book_date_col;

    @FXML
    private TableView<Book> books_table;

    @FXML
    private TextField books_search_field;

    @FXML
    private ImageView books_user_img;

    @FXML
    private Button book_new_book_button;

    
    private boolean start_book_window(String window, Book book) {
        try {
            String title = "Книга";
            if(is_opened("Book")) return false;

            if(book == null) {
                book = new Book(" ", " ", " ", new Date(), new Date(), " ");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window + ".fxml"));
            Parent root = loader.load();

            BookController controller = loader.getController();
            
            Stage newStage = new Stage();
            Stage current_stage = (Stage) books_table.getScene().getWindow();
            newStage.setScene(new Scene(root));
            newStage.setTitle(title);
            newStage.setResizable(false);
            controller.setData(book, newStage, current_stage);
            windows.add("Book");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void open_book() {
        start_book_window("Book", null);
    }

    private boolean start_change_user_info_window() {
        String title = "Інформація про користувача";
        if(is_opened("Change_user_info")) return false;
        Users users = new Users();
        User user = users.getCurrentUser();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Change_user_info.fxml"));
            Parent root = loader.load();

            ChangeInfoController controller = loader.getController();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle(title);
            controller.setData(user, newStage);
            newStage.setResizable(false);
            windows.add("Change_user_info");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @FXML
    public void initialize() {
        ObservableList<Book> books_raw = new Books().get_books_list();
        FilteredList<Book> books = new FilteredList<>(books_raw, p -> true);

        if(!Users.root()) {
            book_new_book_button.setVisible(false);
        }

        books_search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            books.setPredicate(book -> {
                if(newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter = newValue.toLowerCase();
                return book.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        Users users = new Users();
        User current_user = users.getCurrentUser();
        
        try {
            InputStream icon = new FileInputStream(current_user.getIcon());
            books_user_img.setImage(new Image(icon));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        // books_user_img.setOnMouseClicked(e -> start_change_user_info_window()); //TODO
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Змінити");
        item1.setOnAction((e) -> {
            start_change_user_info_window();
        });
        MenuItem item2 = new MenuItem("Вийти");
        item2.setOnAction((e) -> {
            try {
                close_all_windows();
                Users.logout();
                Thread.sleep(100); // Задержка для закрытия окон
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            start_window("Login", "Вхід");
        });
        contextMenu.getItems().addAll(item1, item2);

        books_user_img.setOnMouseClicked(e -> {
            contextMenu.show(books_user_img, e.getScreenX(), e.getScreenY());
        });
        books_user_img.setOnMouseEntered((e) -> {
            books_user_img.setCursor(Cursor.HAND);
        });


        book_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        book_author_col.setCellValueFactory(new PropertyValueFactory<>("author"));
        book_genre_col.setCellValueFactory(new PropertyValueFactory<>("genre"));
        book_date_col.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return new SimpleStringProperty(book.getDateAdded());
        });

        
        books_table.setItems(books);
        books_table.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && !books_table.getSelectionModel().isEmpty()) {
                selectedBook = books_table.getSelectionModel().getSelectedItem();
                start_book_window("Book", selectedBook);
            }
        });
    }

    private void close_all_windows() {
        List<Window> winds = new ArrayList<>(Window.getWindows());
        windows.clear();
        
        for(Window window: winds) {
            if(window instanceof Stage) {
                Stage stage = (Stage) window;
                if(stage.isShowing()) stage.close();
            }
        }

        System.out.println();
    }
}
