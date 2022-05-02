package ControllersGUI;

import Domain.TheatreException;
import Domain.User;
import Service.ITheatreObserver;
import Service.ITheatreServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public Label First_name;
    public TextField firstnameTextBox;
    public TextField usernameTextBox;
    public TextField passwordTextBox;
    public Button loginClientButton;
    public Button loginAdminButton;
    ITheatreObserver theatreObserver;
    ITheatreServices theatreServices;

    private Stage loginstage;

    public void setLoginStage(Stage loginStage) {
        this.loginstage = loginStage;
    }



    public void setup(ITheatreServices theatreServices, ITheatreObserver theatreObserver)
    {
        this.theatreServices = theatreServices;
        this.theatreObserver = theatreObserver;
    }

    public void loginAdmin(MouseEvent mouseEvent) {
        String username= usernameTextBox.getText();
        String password=passwordTextBox.getText();
        if (username.matches("[ ]*")|| password.matches("[ ]*")){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please introduce the credentials!");
            alert.show();
            return;
        }
        else
        {
            User user = new User(username,password);
            try
            {
                Stage newstage= new Stage();
                FXMLLoader loader = new FXMLLoader(JavaFxMain.class.getResource("MainController.fxml"));
                //loader.setLocation(Ja.class.getResource());
                try{

                    Scene scene = new Scene(loader.load());
                    newstage.setScene(scene);
                    newstage.show();
                    MainController mainViewController =(MainController) (loader.getController());
                    theatreServices.loginUser(user,mainViewController);
                    mainViewController.setup(username,password,theatreServices);
                }catch (IOException ex)
                {
                    System.out.println(ex);
                }
            }
            catch (TheatreException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR,"User doesn't exist!");
                alert.show();
                return;
            }
        }
    }

    public void loginClient(MouseEvent mouseEvent) {
        String first_name = firstnameTextBox.getText();
        String username= usernameTextBox.getText();
        String password=passwordTextBox.getText();
        if (first_name.matches("[ ]*") || username.matches("[ ]*")|| password.matches("[ ]*")){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please introduce the credentials!");
            alert.show();
            return;
        }
        else
        {
            User user = new User(first_name,username,password);
            try
            {
                Stage newstage= new Stage();
                FXMLLoader loader = new FXMLLoader(JavaFxMain.class.getResource("MainController.fxml"));
                //loader.setLocation(Ja.class.getResource());
                try{

                    Scene scene = new Scene(loader.load());
                    newstage.setScene(scene);
                    newstage.show();
                    MainController mainViewController =(MainController) (loader.getController());
                    theatreServices.loginUser2(user,mainViewController);
                    mainViewController.setup1(first_name,username,password,theatreServices);
                }catch (IOException ex)
                {
                    System.out.println(ex);
                }
            }
            catch (TheatreException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR,"User doesn't exist!");
                alert.show();
                return;
            }
        }
    }
}
