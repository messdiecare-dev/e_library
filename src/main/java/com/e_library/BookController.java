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
import com.e_library.models.Users;
import com.e_library.models.instances.Book;
import com.e_library.models.instances.BookInstance;
import com.e_library.models.instances.User;

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
    private static Book current_book;
    public static Stage current_book_stage;
    private static boolean change_image = false;
    private static boolean book_instances_change = false;
    private static Stage window_books;
    public static ObservableList<BookInstance> instances;

    @FXML
    private TextField book_name, book_author, book_genre;

    @FXML
    private TextArea book_annotation;

    @FXML
    private DatePicker book_dateAdding, book_dateCreated;

    @FXML
    private Button book_create_button, book_save_button, book_delete_button, book_delete_success_button, new_instance_button, book_editable_change, reserved_success_button, reserved_fail_button;

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
        book_dateAdding.setDisable(!book_dateAdding.isDisabled());
        book_dateCreated.setDisable(!book_dateCreated.isDisabled());
        book_annotation.setEditable(!book_annotation.isEditable());
        book_create_button.setDisable(!book_create_button.isDisabled());
        book_save_button.setDisable(!book_save_button.isDisabled());
        book_delete_button.setDisable(!book_delete_button.isDisabled());
        new_instance_button.setDisable(!new_instance_button.isDisabled());
        book_instances_change = !book_instances_change;
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
                book_annotation.getText()
        );

        bks.add_book_to_list(book);

        TableView<Book> table = (TableView<Book>) window_books.getScene().lookup("#books_table");
        table.refresh();
        start_window("book_created_success", "Успішно додали книгу");
    }

    public void save_book() {
        Books books = new Books();
        books.remove_book(current_book);
        String name = book_name.getText();
        String surname = book_author.getText();
        String genre = book_genre.getText();
        LocalDate dateAdded = book_dateAdding.getValue();
        LocalDate dateCreated = book_dateCreated.getValue();
        String annotation = book_annotation.getText();

        if(books.is_valid_info(name, surname, genre, dateAdded, dateCreated)) {
            current_book.setName(name);
            current_book.setAuthor(surname);
            current_book.setGenre(genre);
            current_book.setDateAdded(dateAdded);
            current_book.setDateCreated(dateCreated);
            current_book.setAnnotation(annotation);
            books.add_book_to_list(current_book);
            TableView<Book> table = (TableView<Book>) window_books.getScene().lookup("#books_table");
            table.refresh();
    
            start_window("book_changed_success", "Успішно змінили книгу");
            System.out.println("Book saved succesfully!");
        } else {
            start_window("book_changed_fail", "Помилка під час збереження");
        }

    }

    public void setData(Book book, Stage stage, Stage books_window) {
        current_book = book;
        current_book_stage = stage;
        window_books = books_window;
        boolean root = Users.root();
        instances = FXCollections.observableArrayList(current_book.getInstancesList());
        book_name.setText(book.getName());
        book_author.setText(book.getAuthor());
        book_genre.setText(book.getGenre());
        book_dateAdding.setValue(LocalDate.parse(book.getDateAdded()));
        book_dateCreated.setValue(LocalDate.parse(book.getDateCreated()));
        book_annotation.setText(book.getAnnotation());

        if(!root) {
            book_create_button.setVisible(false);
            book_save_button.setVisible(false);
            book_delete_button.setVisible(false);
            new_instance_button.setVisible(false);
            book_editable_change.setVisible(false);
        }

        stage.setOnCloseRequest((event) -> {
            Books books = new Books();
            books.remove_book(current_book);
            current_book.setInstances(instances);
            books.add_book_to_list(current_book);
            current_book = null;
            instances = null;
            change_image = false;
            book_instances_change = false;
            current_book_stage = null;
            window_books = null;
            windows.remove("Book");
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
        if(root) {
            book_instances.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !book_instances.getItems().isEmpty() && book_instances_change) {
                    BookInstance currentInstance = book_instances.getSelectionModel().getSelectedItem();
                    start_book_instance_window(current_book, currentInstance);
                }
            });
        } 
        else {
            book_instances.setOnMouseClicked(event -> {
                BookInstance currentInstance = book_instances.getSelectionModel().getSelectedItem();
                if(event.getClickCount() == 2) {
                    if(currentInstance.getStatus().equals("Доступний")) {
                        User current_user = new Users().getCurrentUser();
                        currentInstance.setStatus("Виданий читачу");
                        currentInstance.setOwner(current_user.getId());
                        TableView<BookInstance> table = (TableView<BookInstance>) book_name.getScene().lookup("#book_instances");
                        table.refresh();
                        start_window("book_reserved_success", "Книга видана");
                    } else {
                        start_window("book_reserved_fail", "Помилка при видачі книги");
                    }
                }
            });
        }
        try {
            InputStream image = new FileInputStream(book.getImage());
            book_image.setImage(new Image(image));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void reserved_success_close() {
        windows.remove("book_reserved_success");
        Stage stage = (Stage) reserved_success_button.getScene().getWindow();
        stage.close();
    }

    public void reserved_fail_close() {
        windows.remove("book_reserved_fail");
        Stage stage = (Stage) reserved_fail_button.getScene().getWindow();
        stage.close();
    }

    public void create_new_instance() {
        start_book_instance_window(current_book, null);
    }

    public boolean start_book_instance_window(Book book, BookInstance book_instance) {
        try {
            String title = "Примірник";
            if(is_opened("BookInstance")) return false;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookInstance.fxml"));
            Parent root = loader.load();

            BookInstanceController controller = loader.getController();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle(title);
            newStage.setResizable(false);
            controller.setData(book_instance, book, newStage, current_book_stage);
            newStage.show();
            windows.add("BookInstance");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void book_delete() {
        Books books = new Books();
        books.remove_book(current_book);
        Stage stage = (Stage) book_delete_button.getScene().getWindow();
        stage.close();
        current_book = null;
        instances = null;
        change_image = false;
        book_instances_change = false;

        TableView<Book> table = (TableView<Book>) window_books.getScene().lookup("#books_table");
        table.refresh();
        
        start_window("book_deleted_success", "Видалення книги");
    }

    public void book_delete_success_close() {
        windows.remove("book_deleted_success");
        Stage stage = (Stage) book_delete_success_button.getScene().getWindow();
        stage.close();
    }

    

}
