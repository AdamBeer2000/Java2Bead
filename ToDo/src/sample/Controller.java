package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.database.SingletonDatabaseService;
import sample.objects.ToDoObject;

import java.sql.SQLException;
import java.util.Date;

public class Controller {
    private static SingletonDatabaseService sds=SingletonDatabaseService.getInstance();

    @FXML
    private MenuItem mainMenuDelete;

    @FXML
    private TableView<ToDoObject> mainTable;

    @FXML
    private TableColumn<ToDoObject, String> tableTitle;

    @FXML
    private TableColumn<ToDoObject, Date> tableStartDate;

    @FXML
    private TableColumn<ToDoObject, Date> tableDeadline;

    @FXML
    private TableColumn<ToDoObject, String> tableDescription;

    @FXML
    private TableColumn<ToDoObject, CheckBox> tableFinished;

    @FXML
    private Button addTodo;

    @FXML
    public void initialize(){
        tableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        tableDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        tableDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableFinished.setCellValueFactory(new PropertyValueFactory<>("is_finished_checkbox"));

        try {
            final ObservableList<ToDoObject> tableData = FXCollections.observableArrayList(sds.getAllTodoByUserId(0));
            mainTable.setItems(tableData);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }




    /*@FXML
    void mainMenuDelete_OnClick(ActionEvent event) {
        int selectedItem = mainList.getSelectionModel().getSelectedIndex();
        mainList.getItems().remove(selectedItem);
    }*/
}
