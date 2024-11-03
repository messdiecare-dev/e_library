package com.e_library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.e_library.models.Books;
import com.e_library.models.instances.Book;
import com.e_library.models.instances.BookInstance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BookController extends MainController {
    private static String books_src = "src\\main\\resources\\com\\e_library\\Books.json";
    private static Book current_book;
    private static boolean change_image = false;
    public static ObservableList<BookInstance> instances;

    @FXML
    private TextField book_name, book_author, book_genre;

    @FXML
    private TextArea book_annotation;

    @FXML
    private DatePicker book_dateAdding, book_dateCreated;

    @FXML
    private Button book_create_button, book_save_button;

    @FXML
    private ImageView book_image;

    @FXML
    private TableView<BookInstance> book_instances;

    @FXML
    private TableColumn<BookInstance, String> instance_col_num, instance_col_status;

    public void book_change_visibility() {
        book_name.setEditable(!book_name.isEditable());
        book_author.setEditable(!book_author.isEditable());
        book_genre.setEditable(!book_genre.isEditable());
        book_dateAdding.setEditable(!book_dateAdding.isEditable());
        book_dateAdding.setDisable(!book_dateAdding.isDisabled());
        book_dateCreated.setEditable(!book_dateCreated.isEditable());
        book_dateCreated.setDisable(!book_dateCreated.isDisabled());
        book_annotation.setEditable(!book_annotation.isEditable());
        book_create_button.setDisable(!book_create_button.isDisabled());
        book_save_button.setDisable(!book_save_button.isDisabled());
        change_image = !change_image;
    }

    public void create_new_book() {
        Books bks = new Books();
        Book book = new Book(
                book_name.getText(),
                book_author.getText(),
                book_genre.getText(),
                Date.from(book_dateAdding.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(book_dateCreated.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                book_annotation.getText());

        bks.createNewObjekt(book, books_src);
        start_window("book_created_success");
    }

    public void save_book() {
        Books books = new Books();
        books.remove_book(current_book);
        current_book.setName(book_name.getText());
        current_book.setAuthor(book_author.getText());
        current_book.setGenre(book_genre.getText());
        current_book.setDateAdded(book_dateAdding.getValue());
        current_book.setDateCreated(book_dateCreated.getValue());
        current_book.setAnnotation(book_annotation.getText());
        // current_book.setInstances(book_instances.getItems());
        books.add_book_to_list(current_book);

        start_window("book_changed_success");
        System.out.println("Book saved succesfully!");
    }

    public void setData(Book book, Stage stage) {
        current_book = book;
        instances = FXCollections.observableArrayList(current_book.getInstancesList());
        book_name.setText(book.getName());
        book_author.setText(book.getAuthor());
        book_genre.setText(book.getGenre());
        book_dateAdding.setValue(LocalDate.parse(book.getDateAdded()));
        book_dateCreated.setValue(LocalDate.parse(book.getDateCreated()));
        book_annotation.setText(book.getAnnotation());
        stage.setOnCloseRequest((event) -> {
            Books books = new Books();
            books.remove_book(current_book);
            current_book.setInstances(instances);
            books.add_book_to_list(current_book);
            current_book = null;
            instances = null;
            change_image = false;
        });

        Tooltip tooltip = new Tooltip("Натисніть, аби змінити картинку)");
        tooltip.setShowDelay(Duration.ONE);
        book_image.setOnMouseClicked((e) -> {
            if(change_image) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select png file");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(null);
                if(file != null) {
                    System.out.println("File selected. You selected: " + file.getAbsolutePath());
                    try {
                        current_book.setImage(file.getAbsolutePath());
                        InputStream image = new FileInputStream(file.getAbsoluteFile());
                        book_image.setImage(new Image(image));

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("File selection. No file is selected");
                }
            }
        });

        book_image.setOnMouseEntered((e) -> {
            if(change_image) {
                book_image.setCursor(Cursor.HAND);
                tooltip.show(book_image, e.getScreenX(), e.getScreenY());
            } else {
                tooltip.hide();
            }
        });

        book_image.setOnMouseExited((e) -> {
            tooltip.hide();
            book_image.setCursor(Cursor.DEFAULT);
        });

        instance_col_num.setCellValueFactory(new PropertyValueFactory<>("number"));
        instance_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        book_instances.setItems(instances);
        book_instances.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 || book_instances.getSelectionModel().isEmpty()) {
                BookInstance currentInstance = book_instances.getSelectionModel().getSelectedItem();
                start_book_instance_window(current_book, currentInstance);
            }
        });
        try {
            InputStream image = new FileInputStream(book.getImage());
            book_image.setImage(new Image(image));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void create_new_instance() {
        start_book_instance_window(current_book, null);
    }

    public void start_book_instance_window(Book book, BookInstance book_instance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookInstance.fxml"));
            Parent root = loader.load();

            BookInstanceController controller = loader.getController();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            controller.setData(book_instance, book, newStage);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
