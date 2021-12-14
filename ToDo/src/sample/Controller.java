package sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import sample.database.SingletonDatabaseService;
import sample.groups.Group;
import sample.groups.Invite;
import sample.objects.ToDoObject;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.enums.Category;
import sample.enums.Importance;
import sample.objects.TodoBuilder;
import sample.users.SingletonLoggedUserManager;
import sample.users.User;

import java.time.LocalTime;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

public class Controller
{
    SingletonDatabaseService sds=SingletonDatabaseService.getInstance();
    SingletonLoggedUserManager slum=SingletonLoggedUserManager.getInstance();

    @FXML
    private VBox defaultVBox;

    @FXML
    private TextField loginUsername;
    @FXML
    private TextField loginPassword;
    @FXML
    private TextField signinUsername;
    @FXML
    private TextField signinPassword;
    @FXML
    private TextField signinEmail;
    @FXML
    private TextField taskName;
    @FXML
    private TextField searchUser;
    @FXML
    private TextField groupName;

    @FXML
    private ListView<String> grouplist;

    @FXML
    private TextArea description;
    @FXML
    private TextArea todoDesc;

    @FXML
    private Pane loginUsernamePN;
    @FXML
    private Pane loginPasswordPN;
    @FXML
    private Pane signinUsernamePN;
    @FXML
    private Pane signinPasswordPN;
    @FXML
    private Pane signinEmailPN;
    @FXML
    private Pane popupPane;
    @FXML
    private Pane innerBtnPane1;
    @FXML
    private Pane innerBtnPane2;
    @FXML
    private Pane innerBtnPane3;
    @FXML
    private Pane innerBtnPane4;
    @FXML
    private Pane innerBtnPane5;
    @FXML
    private Pane loginPane;
    @FXML
    private Pane bannerPane;
    @FXML
    private Pane addPane;
    @FXML
    private Pane invitePane;
    @FXML
    private Pane createGroupPane;
    @FXML
    private Pane acceptDeclinePane;

    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private DatePicker todoDayPicker;

    @FXML
    private Text clock;
    @FXML
    private Text popupMessage;

    @FXML
    private TableView<ToDoObject> mainTable;
    @FXML
    private TableColumn<ToDoObject, String> columnName;
    @FXML
    private TableColumn<ToDoObject, Date> columnStartDate;
    @FXML
    private TableColumn<ToDoObject, Date> columnDeadline;
    @FXML
    private TableColumn<ToDoObject, CheckBox> columnFinished;

    @FXML
    private TableView<Invite> InviteTable;
    @FXML
    private TableColumn<Invite, User> inviteFromUserColumn;
    @FXML
    private TableColumn<Invite, Group> inviteToGroupColumn;

    @FXML
    private TableView<Group> groupTable;
    @FXML
    private TableColumn<Group, String> groupTableColumn_Group;

    @FXML
    private ListView<String> userList;

    @FXML
    private Button acceptInviteButton;

    @FXML
    private Button declineInviteButton;

    private Category currentCategory = Category.NOLABEL;

    private Group groupToInvite;

    @FXML
    public void initialize()
    {
        setInnerButtonActivityColor();

        columnName.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        columnDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        columnFinished.setCellValueFactory(new PropertyValueFactory<>("is_finished_checked"));

        groupTableColumn_Group.setCellValueFactory(new PropertyValueFactory<>("name"));

        inviteFromUserColumn.setCellValueFactory(new PropertyValueFactory<>("InviterName"));
        inviteToGroupColumn.setCellValueFactory(new PropertyValueFactory<>("GroupName"));

        groupTable.setRowFactory(
            tableView -> {
                final TableRow<Group> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();
                MenuItem removeItem = new MenuItem("Leave");
                MenuItem inviteItem = new MenuItem("Invite");
                removeItem.setOnAction(event -> {
                    groupTable.getItems().remove(row.getItem());
                    try {
                        sds.SService().leaveGroup(row.getItem().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                inviteItem.setOnAction(event -> {
                    defaultVBox.setDisable(true);
                    invitePane.setDisable(false);
                    invitePane.setVisible(true);

                    groupToInvite = row.getItem();
                    try {
                        ObservableList<User> users = FXCollections.observableArrayList(sds.UService().getAllUsers());
                        for(int i = 0; i < users.size(); i++)
                        {
                            if(users.get(i).getUserName().equals(slum.getUserName()))
                            {
                                users.remove(i);
                            }
                        }

                        ObservableList<String> usernames = FXCollections.observableArrayList();
                        for(int i = 0; i < users.size(); i++)
                        {
                            usernames.add(users.get(i).getUserName());
                        }
                        userList.setItems(usernames);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                rowMenu.getItems().addAll(removeItem, inviteItem);

                // only display context menu for non-empty rows:
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(rowMenu));
                return row;
            }
        );

        Timeline t_line = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            clock.setText(currentTime.getHour() + ":" + currentTime.getMinute());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        t_line.setCycleCount(Animation.INDEFINITE);
        t_line.play();
    }

    public void TypeSearchUser(KeyEvent keyEvent)
    {
        try{
            ObservableList<User> users = FXCollections.observableArrayList(sds.UService().searchUsers(searchUser.getText()));
            for(int i = 0; i < users.size(); i++)
            {
                if(users.get(i).getUserName().equals(slum.getUserName()))
                {
                    users.remove(i);
                }
            }

            ObservableList<String> usernames = FXCollections.observableArrayList();
            for(int i = 0; i < users.size(); i++)
            {
                usernames.add(users.get(i).getUserName());
            }

            userList.getItems().clear();
            userList.setItems(usernames);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void inviteUserToGroup(MouseEvent mouseEvent)
    {
        String invitedusername = userList.getSelectionModel().getSelectedItem();
        if(userList.getSelectionModel().getSelectedItem() != null)
        {
            try
            {
                sds.SService().createInvite(slum.getUserid(), groupToInvite.getId(), sds.UService().getUser(invitedusername).getUserid());
            }
            catch (Exception e)
            {
                System.err.println("ERR");
                e.printStackTrace();
            }
        }
    }

    public void handleCategoryChange(Category category)
    {
        currentCategory = category;
        try {

            if(currentCategory == Category.TODAY)
            {
                dataToTable(FXCollections.observableArrayList(sds.TService().getAllTodoByUserIdToday(slum.getUserid())));
            }else if(currentCategory==Category.FINISHED)
            {
                dataToTable(FXCollections.observableArrayList(sds.TService().getAllTodoByUserIdAndFinished(slum.getUserid(),true)));
            }else if(currentCategory==Category.UNFINISHED)
            {
                dataToTable(FXCollections.observableArrayList(sds.TService().getAllTodoByUserIdAndFinished(slum.getUserid(),false)));
            }else if(currentCategory == Category.NOLABEL)
            {
                dataToTable(FXCollections.observableArrayList(sds.TService().getAllTodoByUserId(slum.getUserid())));
            }else
            {
                dataToTable(observableArrayList(sds.TService().getAllTodoByUserIdAndCategory(slum.getUserid(), currentCategory)));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void dataToTable(ObservableList<ToDoObject> tableDataset)
    {
        for(ToDoObject o : tableDataset)
        {
            o.is_finished_checked.selectedProperty().addListener((observable, oldValue, newValue) -> {
                o.is_finished = o.is_finished_checked.isSelected() ?  true : false;
                ToDoObject newTodo = o;
                SingletonDatabaseService sds = SingletonDatabaseService.getInstance();
                try {
                    sds.TService().modifyTodo(o.todoId, newTodo);
                    handleCategoryChange(currentCategory);
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            });
        }

        try {
            for(int i = 0; i < mainTable.getItems().size(); i++)
            {
                mainTable.getItems().remove(mainTable.getItems().get(i));
            }
            mainTable.setItems(tableDataset);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addPaneButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(true);
        addPane.setDisable(false);
        addPane.setVisible(true);
        bannerPane.setVisible(true);
        bannerPane.setDisable(false);
        
        taskName.setText("");
        description.setText("");
    }

    public void addTodoButtonEvent(MouseEvent mouseEvent)
    {
        Importance imp = Importance.NOLABEL;
        if(!taskName.getText().isEmpty())
        {

            try
            {
                TodoBuilder builder=new TodoBuilder(taskName.getText(),description.getText());
                if(deadlinePicker.getValue()!=null)
                {
                    builder.withDeadLine(deadlinePicker.getValue());
                }
                builder.withImportance(imp);
                builder.withCategory(currentCategory);
                sds.TService().addTodoToUser(slum.getUserid(),builder.Build());
                try
                {
                    handleCategoryChange(currentCategory);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                defaultVBox.setDisable(false);
                addPane.setDisable(true);
                addPane.setVisible(false);
                bannerPane.setVisible(false);
                bannerPane.setDisable(true);

            }catch (Exception e)
            {
                e.printStackTrace();
                //todo fail
            }
        }
    }

    public void clickTodoDayPicker(ActionEvent actionEvent)
    {
        if(todoDayPicker.getValue() != null)
        {
            Instant instant = Instant.from(todoDayPicker.getValue().atStartOfDay(ZoneId.systemDefault()));
            Date date= Date.from(instant);
            try {
                ObservableList todos = FXCollections.observableArrayList(sds.TService().getAllTodoByUserIdandDate(slum.getUserid(),date));
                dataToTable(todos);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelAddTodoButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        addPane.setDisable(true);
        addPane.setVisible(false);
        bannerPane.setVisible(false);
        bannerPane.setDisable(true);

        taskName.setText("");
        description.setText("");
    }

    public void loginButtonEvent(MouseEvent mouseEvent)
    {
        if(loginUsername.getText().isEmpty()||loginPassword.getText().isEmpty())
        {
            //todo kirni hogy üres x
            setPopup("-fx-background-color: #FF0000", "Missing Username or Password!");
        }
        else if(slum.loginUser(loginUsername.getText(),loginPassword.getText()))
        {
            loginPane.setVisible(false);
            loginPane.setDisable(true);
            defaultVBox.setDisable(false);
            defaultVBox.setVisible(true);

            try {
                dataToTable(observableArrayList(sds.TService().getAllTodoByUserId(slum.getUserid())));
                groupTable.setItems(observableArrayList(sds.SService().getAllGroupByUserId(slum.getUserid())));

                String msg = "You have " + sds.TService().getAllTodoByUserIdToday(slum.getUserid()).size() + " tasks today!";
                setPopup("-fx-background-color: #008000", msg);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            //todo sikertelen bejelentkezés
            setPopup("-fx-background-color: #FF0000", "Login failed!");
        }
    }

    @FXML
    public void ClickGroupTableCell(MouseEvent event)
    {
        if (event.getClickCount() == 2 && groupTable.getSelectionModel().getSelectedItem() != null)
        {
            dataToTable(observableArrayList(groupTable.getSelectionModel().getSelectedItem().associatedTodos));
        }
    }

    public void ClickMainTableCell(MouseEvent event)
    {
        if (event.getClickCount() == 1 && mainTable.getSelectionModel().getSelectedItem() != null)
        {
            todoDesc.setText(mainTable.getSelectionModel().getSelectedItem().description);
        }
    }

    public void ClickInviteTableCell(MouseEvent event)
    {
        if(InviteTable.getSelectionModel().getSelectedItem() != null)
        {
            acceptInviteButton.setDisable(false);
            declineInviteButton.setDisable(false);
        }
    }

    public void signinButtonEvent(MouseEvent mouseEvent)
    {
        try
        {
            sds.UService().addUser(signinUsername.getText(),signinPassword.getText(),signinEmail.getText());
            slum.loginUser(signinUsername.getText(),signinPassword.getText());
            setPopup("-fx-background-color: #008000", "Successful sign in!");
            loginPane.setVisible(false);
            loginPane.setDisable(true);
            defaultVBox.setDisable(false);
            defaultVBox.setVisible(true);
            try {
                dataToTable(observableArrayList(sds.TService().getAllTodoByUserId(slum.getUserid())));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }catch (Exception e)
        {
            //todo fail
            setPopup("-fx-background-color: #FF0000", "Sign in failed!");
        }

    }

    public void loginUsernameChangeEvent(KeyEvent keyEvent)
    {
        if(loginUsername.getText().isEmpty())
        {
            loginUsernamePN.setStyle("-fx-background-color: #FF0000");
        }
        else
        {
            loginUsernamePN.setStyle("-fx-background-color: #008000");
        }
    }

    public void loginPasswordChangeEvent(KeyEvent keyEvent)
    {
        if(loginPassword.getText().isEmpty())
        {
            loginPasswordPN.setStyle("-fx-background-color: #FF0000");
        }
        else
        {
            loginPasswordPN.setStyle("-fx-background-color: #008000");
        }
    }

    public void signinEmailReleaseEvent(KeyEvent keyEvent)
    {
        if(signinEmail.getText().contains("@") && signinEmail.getText().contains(".") && signinEmail.getText().length() >= 5)
        {
            signinEmailPN.setStyle("-fx-background-color: #008000");
        }
        else
        {
            signinEmailPN.setStyle("-fx-background-color: #FF0000");
        }
    }

    public void signinUsernameChangeEvent(KeyEvent keyEvent)
    {
        if(signinUsername.getText().isEmpty())
        {
            signinUsernamePN.setStyle("-fx-background-color: #FF0000");
        }
        else
        {
            signinUsernamePN.setStyle("-fx-background-color: #008000");
        }
    }

    public void signinPasswordChangeEvent(KeyEvent keyEvent)
    {
        if(signinPassword.getText().length() < 8)
        {
            signinPasswordPN.setStyle("-fx-background-color: #FF0000");
        }
        else
        {
            signinPasswordPN.setStyle("-fx-background-color: #008000");
        }
    }

    //POPUP
    public void setPopup(String color, String msg)
    {
        popupPane.setStyle(color + "; -fx-opacity: 0.5");
        popupMessage.setText(msg);

        Thread wait_thread = new Thread(() -> {
            popupPane.setVisible(true);
            popupPane.setDisable(false);

            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                System.err.print("[THREAD ERROR]:");
                e.printStackTrace();
            }

            popupPane.setVisible(false);
            popupPane.setDisable(true);
        });

        wait_thread.start();
    }

    public void closePopupButtonEvent(MouseEvent mouseEvent) {
        popupPane.setVisible(false);
        popupPane.setDisable(true);
    }

    public void todayTasks(MouseEvent mouseEvent)
    {
        innerBtnPane2.setStyle("-fx-background-color: #202225");
        innerBtnPane1.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.TODAY);
        todoDayPicker.setValue(null);
    }

    public void plannedTasks(MouseEvent mouseEvent)
    {
        innerBtnPane3.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane1.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.PLANNED);
        todoDayPicker.setValue(null);
    }

    public void unfinishedTasks(MouseEvent mouseEvent)
    {
        innerBtnPane4.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane1.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.UNFINISHED);
        todoDayPicker.setValue(null);
    }

    public void finishedtasks(MouseEvent mouseEvent)
    {
        innerBtnPane5.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane1.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.FINISHED);
        todoDayPicker.setValue(null);
    }

    public void allTasks(MouseEvent mouseEvent)
    {
        innerBtnPane1.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.NOLABEL);
        todoDayPicker.setValue(null);
    }

    public void setInnerButtonActivityColor()
    {
        innerBtnPane1.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
    }

    public void cancelInviteButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        invitePane.setDisable(true);
        invitePane.setVisible(false);
    }

    public void invitePplButtonEvent(MouseEvent mouseEvent)
    {
        ArrayList<Group> myGroups;

        try
        {

        }
        catch (Exception e)
        {
            System.err.println("ERR");
        }
    }

    public void toAddGroupButton(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(true);
        createGroupPane.setDisable(false);
        createGroupPane.setVisible(true);
    }

    public void cancelCreateGroupButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        createGroupPane.setDisable(true);
        createGroupPane.setVisible(false);
    }

    public void createGroupsInPaneButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        createGroupPane.setDisable(true);
        createGroupPane.setVisible(false);

        if(!groupName.getText().isEmpty())
        {
            try {
                sds.SService().createGroup(slum.getUserid(), groupName.getText());
            }
            catch (Exception e)
            {
                System.err.println("ERR");
            }
        }
    }

    public void acceptInviteButtonEvent(MouseEvent mouseEvent)
    {
        ArrayList<Invite> invites;
        int selected = -1;
        try
        {
            selected = InviteTable.getSelectionModel().getSelectedIndex();
            if(selected != -1)
            {
                invites = sds.SService().getInvitesOffLoggedUser();
                sds.SService().acceptInvite(invites.get(selected).getInviteId());
                dataToInviteTable(observableArrayList(sds.SService().getInvitesOffLoggedUser()));
                groupTable.setItems(observableArrayList(sds.SService().getAllGroupByUserId(slum.getUserid())));
            }
        }
        catch (Exception e)
        {
            System.err.println("ERR");
            e.printStackTrace();
        }
    }

    public void backFromInviteButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        acceptDeclinePane.setDisable(true);
        acceptDeclinePane.setVisible(false);
    }

    public void declineInviteButtonEvent(MouseEvent mouseEvent)
    {
        ArrayList<Invite> invites;
        int selected = -1;
        try
        {
            selected = InviteTable.getSelectionModel().getSelectedIndex();
            invites = sds.SService().getInvitesOffLoggedUser();
            sds.SService().declineInvite(invites.get(selected).getInviteId());
            dataToInviteTable(observableArrayList(sds.SService().getInvitesOffLoggedUser()));
        }
        catch (Exception e)
        {
            System.err.println("ERR");
            e.printStackTrace();
        }
    }

    public void dataToInviteTable(ObservableList<Invite> tableDataset)
    {
        try
        {
            for(int i = 0; i < InviteTable.getItems().size(); i++)
            {
                InviteTable.getItems().remove(InviteTable.getItems().get(i));
            }
            InviteTable.setItems(tableDataset);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void letMeSeemyInvitesButtonEvent(MouseEvent mouseEvent)
    {
        try
        {
            dataToInviteTable(observableArrayList(sds.SService().getInvitesOffLoggedUser()));
        }
        catch (Exception e)
        {
            System.err.println("ERR");
            e.printStackTrace();
        }

        defaultVBox.setDisable(true);
        acceptDeclinePane.setDisable(false);
        acceptDeclinePane.setVisible(true);
    }

    public void logoutEvent(ActionEvent actionEvent)
    {
        loginPane.setVisible(true);
        loginPane.setDisable(false);
        defaultVBox.setDisable(true);
        defaultVBox.setVisible(false);
    }
}
