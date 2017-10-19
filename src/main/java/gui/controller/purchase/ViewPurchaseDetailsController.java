package gui.controller.purchase;

import dao.ClientDAO;
import dao.ProductStockDAO;
import dao.PurchaseDAO;
import domain.Client;
import domain.ProductStock;
import domain.Purchase;
import domain.PurchaseDetail;
import gui.util.AlertBuilder;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class ViewPurchaseDetailsController implements Initializable {

    @FXML
    private Label lblPurchaseId;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblClientName;
    @FXML
    private TableView<PurchaseDetail> tblDetails;
    @FXML
    private TableColumn colQuantity;
    @FXML
    private TableColumn colDescription;
    @FXML
    private TableColumn colUnitPrice;
    @FXML
    private TableColumn colTotalPrice;
    @FXML
    private Button btnCancelPurchase;
    @FXML
    private Button btnReturn;
    @FXML
    private Label lblTotal;
    @FXML
    private VBox purchaseDetailsForm;

    private Purchase selectedPurchase;
    private AlertBuilder alertBuilder;
    private ListPurchaseController listPurchaseController;
    private ClientDAO clientDAO;
    private PurchaseDAO purchaseDAO;
    private ProductStockDAO productStockDAO;

    @Autowired
    public ViewPurchaseDetailsController(AlertBuilder alertBuilder, ListPurchaseController listPurchaseController,
                                         ClientDAO clientDAO, PurchaseDAO purchaseDAO,
                                         ProductStockDAO productStockDAO) {

        this.alertBuilder = alertBuilder;
        this.listPurchaseController = listPurchaseController;
        this.clientDAO = clientDAO;
        this.purchaseDAO = purchaseDAO;
        this.productStockDAO = productStockDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedPurchase = listPurchaseController.getPurchaseSelected();
        loadSelectedPurchase(selectedPurchase);
        initDetailsTable(selectedPurchase.getPurchaseDetails());
    }

    private void initDetailsTable(List<PurchaseDetail> purchaseDetailList) {
        colQuantity.setCellValueFactory(new PropertyValueFactory<PurchaseDetail, String>("quantity"));
        colDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PurchaseDetail,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PurchaseDetail, String> data) {
                return new ReadOnlyStringWrapper(String.valueOf(data.getValue().getProduct().getDescription()));
            }
        });
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<PurchaseDetail, String>("unitPrice"));
        colTotalPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PurchaseDetail,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PurchaseDetail, String> data) {
                double total = Double.valueOf(data.getValue().getQuantity()) * Double.valueOf(data.getValue().getUnitPrice());
                return new ReadOnlyStringWrapper(String.valueOf(total));
            }
        });
        ObservableList<PurchaseDetail> details = FXCollections.observableArrayList();
        details.addAll(purchaseDetailList);
        tblDetails.setItems(details);
        tblDetails.getSelectionModel().selectFirst();
    }

    private void loadSelectedPurchase(Purchase purchase) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = formatter.format(purchase.getDate());
        double total = purchase.getPurchaseDetails().stream()
                .mapToDouble(d -> d.getQuantity() * d.getUnitPrice())
                .sum();
        List<Client> clientList = clientDAO.findAll();
        Client client = clientList.stream()
                .filter(c -> c.getPurchases().contains(purchase))
                .collect(Collectors.toList())
                .get(0);
        lblDate.setText(formattedDate);
        lblPurchaseId.setText(String.valueOf(purchase.getId()));
        lblTotal.setText(String.valueOf(total));
        lblClientName.setText(client.getName());
    }

    public void cancelPurchase(ActionEvent actionEvent) throws IOException {
        if(!selectedPurchase.getBooleanCanceled()) {
            Alert alert = alertBuilder.builder()
                    .type(Alert.AlertType.CONFIRMATION)
                    .title("Detalle de Venta")
                    .headerText("Está por ANULAR la venta número: " + selectedPurchase.getId() +
                            "\nDel cliente: " + lblClientName.getText())
                    .contentText("¿Confirmar anulación?")
                    .build();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                for(PurchaseDetail detail : selectedPurchase.getPurchaseDetails()) {
                    ProductStock stock = productStockDAO.findByProductId(detail.getProduct().getId());
                    stock.setQuantity(stock.getQuantity() + detail.getQuantity());
                    productStockDAO.update(stock);
                }
                selectedPurchase.setCanceled(true);
                purchaseDAO.update(selectedPurchase);
                alertBuilder.builder()
                        .type(Alert.AlertType.INFORMATION)
                        .title("Detalle de Venta")
                        .headerText("Venta anulada exitosamente")
                        .contentText("Se ha anulado la venta: " + selectedPurchase.getId())
                        .build()
                        .showAndWait();
                listPurchaseController.reloadForm(actionEvent);
                returnToPurchases(actionEvent);
            }
        }
        else {
            alertBuilder.builder()
                    .type(Alert.AlertType.INFORMATION)
                    .title("Detalle de Venta")
                    .headerText("Venta anulada")
                    .contentText("La venta seleccionada ya se encuentra anulada")
                    .build()
                    .showAndWait();
        }
    }

    public void returnToPurchases(ActionEvent actionEvent) {
        Stage stage = (Stage) btnReturn.getScene().getWindow();
        stage.close();
    }
}
