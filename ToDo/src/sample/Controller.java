package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.database.SingletonDatabaseService;
import sample.groups.Group;
import sample.groups.Invite;
import sample.objects.ToDoObject;
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
    private Text clock;
    @FXML
    private Text popupMessage;

    @FXML
    private RadioButton radioMostImp;
    @FXML
    private RadioButton radioImp;
    @FXML
    private RadioButton radioLessImp;
    @FXML
    private RadioButton radioAvg;
    @FXML
    private RadioButton radioNotImp;

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

    private Category currentCategory = Category.NOLABEL;

    @FXML
    public void initialize()
    {
        setInnerButtonActivityColor();

        columnName.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        columnDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        columnFinished.setCellValueFactory(new PropertyValueFactory<>("is_finished_checked"));

        groupTableColumn_Group.setCellValueFactory(new PropertyValueFactory<>("name"));


        Timeline t_line = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            clock.setText(currentTime.getHour() + ":" + currentTime.getMinute());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        t_line.setCycleCount(Animation.INDEFINITE);
        t_line.play();
    }

    public void handleCategoryChange(Category category)
    {
        currentCategory = category;
        try {
            if(currentCategory == Category.NOLABEL)
            {
                dataToTable(FXCollections.observableArrayList(sds.getAllTodoByUserId(slum.getUserid())));
            }
            if(currentCategory == Category.TODAY)
            {
                dataToTable(FXCollections.observableArrayList(sds.getAllTodoByUserIdandDate(slum.getUserid(),new Date())));
            }
            else
            {
                dataToTable(FXCollections.observableArrayList(sds.getAllTodoByUserIdAndCategory(slum.getUserid(), currentCategory)));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void dataToTable(ObservableList<ToDoObject> tableDataset)
    {
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

    public void mostImpRadio(ActionEvent actionEvent)
    {
        radioImp.setSelected(false);
        radioLessImp.setSelected(false);
        radioNotImp.setSelected(false);
        radioAvg.setSelected(false);
    }

    public void impRadio(ActionEvent actionEvent)
    {
        radioMostImp.setSelected(false);
        radioLessImp.setSelected(false);
        radioNotImp.setSelected(false);
        radioAvg.setSelected(false);
    }

    public void notImpRadio(ActionEvent actionEvent)
    {
        radioImp.setSelected(false);
        radioLessImp.setSelected(false);
        radioMostImp.setSelected(false);
        radioAvg.setSelected(false);
    }

    public void avgRadio(ActionEvent actionEvent)
    {
        radioImp.setSelected(false);
        radioLessImp.setSelected(false);
        radioNotImp.setSelected(false);
        radioMostImp.setSelected(false);
    }

    public void lessImpRadio(ActionEvent actionEvent)
    {
        radioImp.setSelected(false);
        radioMostImp.setSelected(false);
        radioNotImp.setSelected(false);
        radioAvg.setSelected(false);
    }

    public void addTodoButtonEvent(MouseEvent mouseEvent)
    {
        Importance imp = Importance.NOLABEL;
        if(!taskName.getText().isEmpty())
        {
            if(radioMostImp.isSelected()) imp = Importance.MOST_IMPORTANT;
            if(radioImp.isSelected()) imp = Importance.IMPORTANT;
            if(radioLessImp.isSelected()) imp = Importance.LESS_IMPORTANT;
            if(radioAvg.isSelected()) imp = Importance.AVARAGE_IMPORTANT;
            if(radioNotImp.isSelected()) imp = Importance.NOT_IMPORTANT;

            try
            {
                TodoBuilder builder=new TodoBuilder(taskName.getText(),description.getText()); //TODO: importance hozzáadása, amennyiben nem null, a checker fv még nincs kész
                if(deadlinePicker.getValue()!=null)
                {
                    builder.withDeadLine(deadlinePicker.getValue());
                }
                builder.withImportance(imp);
                builder.withCategory(currentCategory);
                sds.addTodoToUser(slum.getUserid(),builder.Build());
                try {
                    if(currentCategory != Category.NOLABEL)
                    {
                        dataToTable(FXCollections.observableArrayList(sds.getAllTodoByUserIdAndCategory(slum.getUserid(), currentCategory)));
                    }else
                    {
                        dataToTable(FXCollections.observableArrayList(sds.getAllTodoByUserId(slum.getUserid())));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                defaultVBox.setDisable(false);
                addPane.setDisable(true);
                addPane.setVisible(false);
                bannerPane.setVisible(false);
                bannerPane.setDisable(true);

                radioImp.setSelected(false);
                radioLessImp.setSelected(false);
                radioNotImp.setSelected(false);
                radioMostImp.setSelected(false);
                radioAvg.setSelected(false);

            }catch (Exception e)
            {
                e.printStackTrace();
                //todo fail
            }
        }
    }

    public void cancelAddTodoButtonEvent(MouseEvent mouseEvent)
    {
        radioImp.setSelected(false);
        radioLessImp.setSelected(false);
        radioNotImp.setSelected(false);
        radioMostImp.setSelected(false);
        radioAvg.setSelected(false);

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
            setPopup("-fx-background-color: #008000", "Successful login!");
            loginPane.setVisible(false);
            loginPane.setDisable(true);
            defaultVBox.setDisable(false);
            defaultVBox.setVisible(true);

            try {
                dataToTable(FXCollections.observableArrayList(sds.getAllTodoByUserId(slum.getUserid())));
                groupTable.setItems(FXCollections.observableArrayList(sds.getAllGroupByUserId(slum.getUserid())));
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
        if (event.getClickCount() == 2)
        {
            dataToTable(FXCollections.observableArrayList(groupTable.getSelectionModel().getSelectedItem().associatedTodos));
        }
    }

    public void ClickMainTableCell(MouseEvent event)
    {
        if (event.getClickCount() == 1)
        {
            todoDesc.setText(mainTable.getSelectionModel().getSelectedItem().description);
        }
    }

    public void signinButtonEvent(MouseEvent mouseEvent)
    {
        try
        {
            sds.addUser(signinUsername.getText(),signinPassword.getText(),signinEmail.getText());
            slum.loginUser(signinUsername.getText(),signinPassword.getText());
            setPopup("-fx-background-color: #008000", "Successful sign in!");
            loginPane.setVisible(false);
            loginPane.setDisable(true);
            defaultVBox.setDisable(false);
            defaultVBox.setVisible(true);
            try {
                dataToTable(FXCollections.observableArrayList(sds.getAllTodoByUserId(slum.getUserid())));
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
    }

    public void plannedTasks(MouseEvent mouseEvent)
    {
        innerBtnPane3.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane1.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.PLANNED);
    }

    public void unfinishedTasks(MouseEvent mouseEvent)
    {
        innerBtnPane4.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane1.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.UNFINISHED);
    }

    public void finishedtasks(MouseEvent mouseEvent)
    {
        innerBtnPane5.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane1.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.FINISHED);
    }

    public void allTasks(MouseEvent mouseEvent)
    {
        innerBtnPane1.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
        handleCategoryChange(Category.NOLABEL);
    }

    public void setInnerButtonActivityColor()
    {
        innerBtnPane1.setStyle("-fx-background-color: #202225");
        innerBtnPane2.setStyle("-fx-background-color: #2F3136");
        innerBtnPane3.setStyle("-fx-background-color: #2F3136");
        innerBtnPane4.setStyle("-fx-background-color: #2F3136");
        innerBtnPane5.setStyle("-fx-background-color: #2F3136");
    }

    public void inviteButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        invitePane.setDisable(true);
        invitePane.setVisible(false);
        //TODO invite
        //System.out.println(grouplist.getSelectionModel().getSelectedIndex());
        ArrayList<Group> myGroups;
        int selected = -1;
        String invitedusername = "";
        try
        {
            myGroups = sds.getAllGroupByUserId(slum.getUserid());
            selected = grouplist.getSelectionModel().getSelectedIndex();
            invitedusername = searchUser.getText();
            sds.createInvite(slum.getUserid(), myGroups.get(selected).getId(), sds.getUser(invitedusername).getUserid());
        }
        catch (Exception e)
        {
            System.err.println("ERR");
        }
    }

    public void cancelInviteButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        invitePane.setDisable(true);
        invitePane.setVisible(false);
    }

    public void invitePplButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(true);
        invitePane.setDisable(false);
        invitePane.setVisible(true);

        ArrayList<Group> myGroups;

        try
        {
             myGroups = sds.getAllGroupByUserId(slum.getUserid());
             ObservableList<String> oblst =  FXCollections.observableArrayList();;
             for(int i = 0; i < myGroups.size(); i++)
             {
                 oblst.add(myGroups.get(i).getName());
             }
             grouplist.setItems(oblst);
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
                sds.createGroup(slum.getUserid(), groupName.getText());
            }
            catch (Exception e)
            {
                System.err.println("ERR");
            }
        }
    }

    public void acceptInviteButtonEvent(MouseEvent mouseEvent)
    {
        //todo accept
    }

    public void backFromInviteButtonEvent(MouseEvent mouseEvent)
    {
        defaultVBox.setDisable(false);
        acceptDeclinePane.setDisable(true);
        acceptDeclinePane.setVisible(false);
    }

    public void declineInviteButtonEvent(MouseEvent mouseEvent)
    {
        //todo decline
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
            dataToInviteTable(FXCollections.observableArrayList(sds.getInvitesOffLoggedUser()));
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
}
