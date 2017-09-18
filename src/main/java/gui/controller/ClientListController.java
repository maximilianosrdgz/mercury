package gui.controller;

import controller.EntityManagerUtils;
import dao.ClientDAO;
import domain.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
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

    @Autowired
    public ClientListController(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientsTable();
    }

    private void initClientsTable() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientDAO.findAll());
        tblClients.setItems(clients);
    }
}
