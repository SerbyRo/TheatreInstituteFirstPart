package ControllersGUI;

import Domain.*;
import Service.ITheatreObserver;
import Service.ITheatreServices;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ClientController implements ITheatreObserver {
    public TableColumn<Client,Long> clientIdTableColumn;
    public TableColumn<Client,String> firstNameTableColumn;
    public TableColumn<Client,String> usernameClientTableColumn;
    public TableColumn<Client,String> passwordClientTableColumn;
    public TableColumn<Client,Integer> ageClientTableColumn;
    public TableColumn<Client,String> genderClientTableColumn;
    public TextField firstNameClientTextBox;
    public TextField passwordClientTextBox;
    public TextField genderClientTextBox;
    public TextField usernameClientTextBox;
    public TextField ageClientTextBox;
    public Button addClientButton;
    public TableView<Client> ClientsTableList;
    private ITheatreServices theatreServices;
    public ObservableList<Client> clientsObservableList;
    private User user;

    Stage stage;

    public void setup(String username,String password,ITheatreServices theatreServices) throws TheatreException {
        this.theatreServices=theatreServices;
        user =new User(username,password);
        clientsObservableList= FXCollections.observableArrayList();
        loadData();
    }
    public void loadData() throws TheatreException {
        List<Client> clientList = new ArrayList<>(theatreServices.getAllClients());
        clientsObservableList.setAll(clientList);
        ClientsTableList.setItems(clientsObservableList);
        clientIdTableColumn.setCellValueFactory(data->{
           return new ReadOnlyObjectWrapper<>(data.getValue().getClientID());
        });
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        usernameClientTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordClientTableColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        ageClientTableColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderClientTableColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }
    @Override
    public void ticketSold(Ticket ticket) throws TheatreException {

    }

    @Override
    public void addedClient(Client client) throws TheatreException {
        Platform.runLater(()->{
            try {
                loadData();
            } catch (TheatreException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void addedShow(Show show) throws TheatreException {

    }

    @Override
    public void deletedShow(Show show) throws TheatreException {

    }

    @Override
    public void updatedShow(Show show) throws TheatreException {

    }

    public void addClientOnClick(MouseEvent mouseEvent) throws TheatreException {
        String first_name = firstNameClientTextBox.getText();
        String username = usernameClientTextBox.getText();
        String password = passwordClientTextBox.getText();
        String age = ageClientTextBox.getText();
        int age1 = Integer.parseInt(age);
        String gender = genderClientTextBox.getText();
        Client client = new Client(first_name,username,password,age1,gender);
        Client client1 = theatreServices.addClient(client);
        loadData();
    }

    public void LogoutOnClick(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you want to save progress ?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try{
                theatreServices.logoutUser(user,this);
            } catch (TheatreException e) {
                e.printStackTrace();
            }
            stage = (Stage) ClientsTableList.getScene().getWindow();
            stage.close();
        }
    }
}
