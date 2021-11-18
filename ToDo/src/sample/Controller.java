package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private ListView<String> mainList;

    @FXML
    private TextField mainInput;

    @FXML
    private MenuItem mainContextDelete;

    @FXML
    private Button mainAddButton;

    @FXML
    private Button addTodo;

    @FXML
    void mainAddButton_Click(ActionEvent event) {
        if(!mainInput.getText().isEmpty())
        {
            String todo = mainInput.getText();
            mainList.getItems().add(todo);
        }
    }

    @FXML
    void mainContextDelete_DeleteTodo(ActionEvent event) {
        int selectedItem = mainList.getSelectionModel().getSelectedIndex();
        System.out.println(selectedItem);
        mainList.getItems().remove(selectedItem);
    }
}
