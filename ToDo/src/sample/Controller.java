package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    private VBox defaultVBox;

    @FXML
    private Pane loginPane;

    @FXML
    private Pane bannerPane;

    @FXML
    private Pane addPane;

    @FXML
    private ListView<String> mainList;

    @FXML
    private Button addTodo;

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
    private TextArea description;

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
    private DatePicker deadlinePicker;

    @FXML
    private Text clock;

    /*@FXML
    public void update()
    {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            //clock.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }*/

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
        loginPane.setVisible(false);
        loginPane.setDisable(true);
        defaultVBox.setDisable(false);
        defaultVBox.setVisible(true);
    }

    public void signinButtonEvent(MouseEvent mouseEvent)
    {
        loginPane.setVisible(false);
        loginPane.setDisable(true);
        defaultVBox.setDisable(false);
        defaultVBox.setVisible(true);
    }

    public void loginUsernameChangeEvent(KeyEvent keyEvent)
    {
        if(loginUsername.getText().isEmpty())
        {
            loginUsernamePN.setStyle("-fx-background-color: RED");
        }
        else
        {
            loginUsernamePN.setStyle("-fx-background-color: GREEN");
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

    public void signinEmailChangeEvent(KeyEvent keyEvent)
    {
        // A regex nem működik
        //^[A-Z0-9+_.-]+@[A-Z0-9.-]+$
        String emailRegex = "^[A-Z0-9+_.-]+@[A-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(signinEmail.getText());

        if(!(matcher.matches()))
        {
            signinEmailPN.setStyle("-fx-background-color: #FF0000");
        }
        else
        {
            signinEmailPN.setStyle("-fx-background-color: #008000");
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
}
