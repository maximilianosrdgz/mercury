package gui.controller;

import dao.ClientDAO;
import domain.Client;
import gui.form.SpringFxmlLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by MaxPower on 17/09/2017.
 */
@NoArgsConstructor
@Controller
public class ClientListController implements Initializable {


    private ClientDAO clientDAO;

    @FXML
    private VBox clientListForm;
    @FXML
    private TableView<Client> tblClients;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colProvince;
    @FXML
    private TableColumn colAge;
    @FXML
    private TableColumn colBuyer;
    @FXML
    private TableColumn colConsultant;
    @FXML
    private TableColumn colBlacklist;

    @FXML
    private Button btnUpdateClient;

    @Autowired
    public ClientListController(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientsTable();
    }

    public void initClientsTable() {
        colId.setCellValueFactory(new PropertyValueFactory<Client, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
        colProvince.setCellValueFactory(new PropertyValueFactory<Client, String>("province"));
        colAge.setCellValueFactory(new PropertyValueFactory<Client, String>("age"));
        colBuyer.setCellValueFactory(new PropertyValueFactory<Client, String>("buyer"));
        colConsultant.setCellValueFactory(new PropertyValueFactory<Client, String>("consultant"));
        colBlacklist.setCellValueFactory(new PropertyValueFactory<Client, String>("blacklist"));
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientDAO.findAll());
        tblClients.setItems(clients);
        tblClients.getSelectionModel().selectFirst();
    }

    public void updateClient(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent)SpringFxmlLoader.load("/update-client.fxml"), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Modificar Cliente");
        stage.setScene(scene);
        stage.show();
    }

    public Client getSelectedClient(){
        return tblClients.getSelectionModel().getSelectedItem();
    }
}
