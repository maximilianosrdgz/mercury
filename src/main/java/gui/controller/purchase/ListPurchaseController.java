package gui.controller.purchase;

import dao.ClientDAO;
import dao.PurchaseDAO;
import domain.Client;
import domain.Purchase;
import gui.controller.MenuController;
import gui.form.SpringFxmlLoader;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class ListPurchaseController implements Initializable {

    @FXML
    private TableColumn colTotal;
    @FXML
    private Button btnFilterByDate;
    @FXML
    private DatePicker dtpFrom;
    @FXML
    private DatePicker dtpTo;
    @FXML
    private TextField txtClientName;
    @FXML
    private CheckBox chkCanceled;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnResetFilter;
    @FXML
    private Button btnFilterByCanceled;
    @FXML
    private TableView<Purchase> tblPurchases;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDate;
    @FXML
    private TableColumn colClientName;
    @FXML
    private TableColumn colCanceled;
    @FXML
    private Button btnViewDetail;
    @FXML
    private VBox purchaseListForm;

    private MenuController menuController;
    private ClientDAO clientDAO;
    private PurchaseDAO purchaseDAO;

    @Autowired
    public ListPurchaseController(MenuController menuController, ClientDAO clientDAO,
                                  PurchaseDAO purchaseDAO) {

        this.menuController = menuController;
        this.clientDAO = clientDAO;
        this.purchaseDAO = purchaseDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dtpFrom.setValue(LocalDate.now());
        dtpTo.setValue(LocalDate.now());
        dtpFrom.setEditable(false);
        dtpTo.setEditable(false);
        initPurchaseTable(clientDAO.findAll());
    }

    private void initPurchaseTable(List<Client> clientList) {
        colId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Purchase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Purchase, String> data) {

                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getId()));
            }
        });
        colDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Purchase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Purchase, String> data) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = formatter.format(data.getValue().getDate());
                return new ReadOnlyStringWrapper(formattedDate);
            }
        });
        colCanceled.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Purchase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Purchase, String> data) {

                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().isCanceled()));
            }
        });
        colClientName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Purchase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Purchase, String> data) {
                Client client = clientList.stream()
                        .filter(c -> c.getPurchases().contains(data.getValue()))
                        .collect(Collectors.toList())
                        .get(0);
                return new ReadOnlyStringWrapper(client.getName());
            }
        });
        colTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Purchase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Purchase, String> data) {
                double total = data.getValue().getPurchaseDetails().stream()
                        .mapToDouble(d -> d.getQuantity() * d.getUnitPrice())
                        .sum();
                return new ReadOnlyStringWrapper(String.valueOf(total));
            }
        });
        ObservableList<Purchase> purchases = FXCollections.observableArrayList();
        clientList.forEach(c -> c.getPurchases().forEach(p -> purchases.add(p)));
        tblPurchases.setItems(purchases);
        tblPurchases.getSelectionModel().selectFirst();
    }

    public void filterWithEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterByClient(null);
        }
    }

    public void filterByClient(ActionEvent actionEvent) {
        List<Client> clientList = clientDAO.findAll();
        ObservableList<Purchase> purchases = FXCollections.observableArrayList();
        for(Client c : clientList) {
            if(StringUtils.containsIgnoreCase(
                    Normalizer.normalize(c.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                    , Normalizer.normalize(txtClientName.getText(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", ""))) {

                purchases.addAll(c.getPurchases());
            }
        }
        tblPurchases.setItems(purchases);
        tblPurchases.getSelectionModel().selectFirst();
    }

    public void filterTableByCanceled(ActionEvent actionEvent) {
        tblPurchases.getItems().removeAll(tblPurchases.getItems().stream()
                .filter(p -> p.getBooleanCanceled() != chkCanceled.isSelected())
                .collect(Collectors.toList()));
    }

    public void filterByDate(ActionEvent actionEvent) {
        List<Client> clientList = clientDAO.findAll();
        ObservableList<Purchase> purchases = FXCollections.observableArrayList();
        Date dateFrom = java.sql.Date.valueOf(dtpFrom.getValue());
        Date dateTo = java.sql.Date.valueOf(dtpTo.getValue());
        for(Client c : clientList) {
            purchases.addAll(c.getPurchases().stream()
                    .filter(p -> p.getDate().compareTo(dateFrom) >= 0 && p.getDate().compareTo(dateTo) <= 0)
                    .collect(Collectors.toList()));
        }
        tblPurchases.setItems(purchases);
        tblPurchases.getSelectionModel().selectFirst();
    }

    public void openFormViewDetail(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/purchases/view-purchase-details.fxml"), 900, 350);
        Stage stage = new Stage();
        stage.setTitle("Detalles de la Venta");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(purchaseListForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void reloadForm(ActionEvent actionEvent) throws IOException {
        menuController.loadListPurchasePane(actionEvent);
    }

    public Purchase getPurchaseSelected() {
        return tblPurchases.getSelectionModel().getSelectedItem();
    }
}
