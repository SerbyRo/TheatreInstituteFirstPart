import ControllersGUI.JavaFxMain;
import ControllersGUI.LoginController;
import RpcProtocol.TheatreClientRpcWorker;
import RpcProtocol.TheatreServicesRpcProxy;
import Service.ITheatreObserver;
import Service.ITheatreServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class StartRpcClient extends Application {
    private static int defaultTheatrePort = 55555;
    private static String defaultServer = "localhost";
    public static void main(String[] args)
    {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProperties = new Properties();
        Socket connection = null;
        try{
            clientProperties.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProperties.list(System.out);
        }catch (IOException ex)
        {
            System.err.println("Can't find client.properties");
            return;
        }
        String serverIP = clientProperties.getProperty("Java.server.host",defaultServer);
        int serverPort = defaultTheatrePort;
        try{
            serverPort = Integer.parseInt(clientProperties.getProperty("Java.server.port"));
        }catch (NumberFormatException ex)
        {
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultTheatrePort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);


        ITheatreServices server = new TheatreServicesRpcProxy(serverIP,serverPort);
        ITheatreObserver observer = new TheatreClientRpcWorker(server);

        FXMLLoader fxmlLoader = new FXMLLoader(JavaFxMain.class.getResource("LoginController.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.show();
        LoginController loginController=(LoginController) fxmlLoader.getController();
        loginController.setup(server,observer);
    }
}
