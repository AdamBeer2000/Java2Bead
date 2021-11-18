package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox defaultVBox;

    @FXML
    private Pane bannerPane;

    @FXML
    private ListView<String> mainList;

    @FXML
    private Button addTodo;

    @FXML
    private Pane addPane;

    @FXML
    private TextField taskName;

    @FXML
    private TextArea description;

    public void addPaneButtonEvent(MouseEvent mouseEvent) {
        defaultVBox.setDisable(true);
        addPane.setDisable(false);
        addPane.setVisible(true);
        bannerPane.setVisible(true);
        bannerPane.setDisable(false);
        
        taskName.setText("");
        description.setText("");
    }

    public void addTodoButtonEvent(MouseEvent mouseEvent) {

        if(!taskName.getText().isEmpty())
        {
            defaultVBox.setDisable(false);
            addPane.setDisable(true);
            addPane.setVisible(false);
            bannerPane.setVisible(false);
            bannerPane.setDisable(true);

            //TODO: add todo to database
        }
    }

    public void cancelAddTodoButtonEvent(MouseEvent mouseEvent) {
        defaultVBox.setDisable(false);
        addPane.setDisable(true);
        addPane.setVisible(false);
        bannerPane.setVisible(false);
        bannerPane.setDisable(true);

        taskName.setText("");
        description.setText("");
    }
}
