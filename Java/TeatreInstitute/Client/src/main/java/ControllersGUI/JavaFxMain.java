package ControllersGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFxMain extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Welcome to mpp!");
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root,300,250));
        FXMLLoader fxmlLoader= new FXMLLoader(JavaFxMain.class.getResource("LoginController.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),500,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
