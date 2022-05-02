package ControllersGUI;

import Domain.*;
import Service.ITheatreObserver;
import Service.ITheatreServices;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController implements ITheatreObserver {

    public AnchorPane showTableViewList;
    public TableColumn<Show,Long> showIdTableColumn;
    public TableColumn<Show,String> nameTableColumn;
    public TableColumn<Show,String> descriptionTableColumn;
    public TableColumn<Show,String> typeTableColumn;
    public TableColumn<Show,String> nrOfSeatsTableColumn;
    public TextField nameShowTextBox;
    public TextField descriptionShowTextBox;
    public TextField typeShowTextBox;
    public TextField nrofseatsTextBox;
    public Button addShowButton;
    public Button deleteShowButton;
    public Button modifyShowButton;
    public Button createAccountClientButton;
    public Button buyticketButton;
    public Button refreshButton;
    public Button logoutButton;
    public TableView<Show> ShowTableView;
    private ITheatreServices theatreServices;
    public ObservableList<Show> showObservableList;

    Stage stage;

    private User user;

    public void setup(String username, String password, ITheatreServices theatreServices) throws TheatreException{
        user =new User(username,password);
        this.theatreServices = theatreServices;
        showObservableList = FXCollections.observableArrayList();
        loadData();
    }
    public void setup1(String first_name,String username,String password,ITheatreServices theatreServices) throws TheatreException{
        user = new User(first_name,username,password);
        this.theatreServices = theatreServices;
        showObservableList = FXCollections.observableArrayList();
        loadData();
    }

    public void loadData() throws TheatreException {
        List<Show> showList = new ArrayList<>(theatreServices.getAllShows());
        showObservableList.setAll(showList);
        ShowTableView.setItems(showObservableList);
        showIdTableColumn.setCellValueFactory(data->{
            return new ReadOnlyObjectWrapper<>(data.getValue().getShowID());
        });
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nrOfSeatsTableColumn.setCellValueFactory(data->{
            if (data.getValue().getNrofseats()<=0)
            {
                return new ReadOnlyObjectWrapper<>("SOLD OUT");
            }
            else
            {
                return new ReadOnlyObjectWrapper<>(String.valueOf(data.getValue().getNrofseats()));
            }
        });
        nrOfSeatsTableColumn.setCellFactory(column-> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item,empty);
                        if (item != null) {
                            if (item.equals("SOLD OUT")) {
                                this.setTextFill(Color.RED);
                            } else {
                                this.setTextFill(Color.BLACK);
                            }
                            this.setText(item);
                        }
                    }
                }
        );
    }

    @Override
    public void ticketSold(Ticket ticket) throws TheatreException {

    }

    @Override
    public void addedClient(Client client) throws TheatreException {

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

    public void AddShowOnClick(MouseEvent mouseEvent) {
    }

    public void DeleteShowOnClick(MouseEvent mouseEvent) {
    }

    public void ModifyShowOnClick(MouseEvent mouseEvent) {
    }

    public void createAccountClientOnClick(MouseEvent mouseEvent) {
        try
        {
            Stage newstage= new Stage();
            FXMLLoader loader = new FXMLLoader(JavaFxMain.class.getResource("ClientController.fxml"));
            //loader.setLocation(Ja.class.getResource());


                Scene scene = new Scene(loader.load());
                newstage.setScene(scene);
                newstage.show();
                ClientController mainViewController =(ClientController) (loader.getController());
                theatreServices.registerObserver(mainViewController);
                //theatreServices.loginUser2(user,mainViewController);
                mainViewController.setup(user.getUsername(),user.getPassword(),theatreServices);
        } catch (IOException | TheatreException ex)
        {
            System.out.println(ex);
        }
    }

    public void BuyTicketButtonOnClick(MouseEvent mouseEvent) {
    }

    public void RefreshShowsOnClick(MouseEvent mouseEvent) throws TheatreException {
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
            stage = (Stage) showTableViewList.getScene().getWindow();
            stage.close();
        }
    }
}
