package com.e_library;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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

    
    private void start_book_window(String window, Book book) {
        try {
            if(book == null) {
                book = new Book(" ", " ", " ", null, null, " ");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window + ".fxml"));
            Parent root = loader.load();

            BookController controller = loader.getController();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            controller.setData(book, newStage);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start_change_user_info_window() {
        Users users = new Users();
        User user = users.getCurrentUser();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Change_user_info.fxml"));
            Parent root = loader.load();

            ChangeInfoController controller = loader.getController();
            
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            controller.setData(user, newStage);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        ObservableList<Book> books_raw = new Books().get_books_list();
        FilteredList<Book> books = new FilteredList<>(books_raw, p -> true);

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

        books_user_img.setOnMouseClicked(e -> start_change_user_info_window());
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
            if(event.getClickCount() == 2 || books_table.getSelectionModel().isEmpty()) {
                selectedBook = books_table.getSelectionModel().getSelectedItem();
                start_book_window("Book", selectedBook);
            }
        });
    }
}
