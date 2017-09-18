package gui.controller;

import controller.EntityManagerUtils;
import dao.ClientDAO;
import domain.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @Autowired
    public ClientListController(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientsTable();
    }

    private void initClientsTable() {
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
    }
}
