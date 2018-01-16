package gui.controller;

import gui.form.SpringFxmlLoader;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class MenuController implements Initializable {

    @FXML
    private Button btnOpenFormCategoryReport;
    @FXML
    private Button btnOpenFormProductReport;
    @FXML
    private Button btnOpenFormClientReport;
    @FXML
    private Button btnOpenFormSendEmail;
    @FXML
    private Button btnOpenFormListSupplier;
    @FXML
    private Button btnOpenFormNewSales;
    @FXML
    private Button btnOpenFormRegisteredSales;
    @FXML
    private Button btnClose;
    @FXML
    private Button openFrm3;
    @FXML
    private VBox dataPane;
    @FXML
    private Button btnOpenFormClientLoad;
    @FXML
    private Button btnClientList;
    @FXML
    private Button btnOpenFormNewProduct;
    @FXML
    private Button btnOpenFormNewMaterial;
    @FXML
    private Button btnOpenFormNewCategory;

    public void setDataPane(Node node) {
        // update VBox with new form(FXML) depends on which button is clicked
        dataPane.getChildren().setAll(node);
    }

    public VBox fadeAnimate(String url) throws IOException {
        VBox v = (VBox) SpringFxmlLoader.load(url);
        FadeTransition ft = new FadeTransition(Duration.millis(500));
        ft.setNode(v);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        return v;
    }

    public void closeForm(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Seguro que desea salir de Mercury?");
        alert.setContentText("Si confirma, la aplicación se cerrará");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        }
    }

    public void loadPane(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/forms/clients/new-client.fxml"));
    }

    public void loadNewClientPane(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/forms/clients/new-client.fxml"));
    }

    public void loadListClientPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/clients/list-client.fxml"));
    }

    public void loadNewProductPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/products/list-product.fxml"));
    }

    public void loadNewMaterialPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/products/list-material.fxml"));
    }

    public void loadNewCategoryPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/products/update-category.fxml"));
    }

    public void loadNewPurchasePane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/purchases/new-purchase.fxml"));
    }

    public void loadListPurchasePane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/purchases/list-purchase.fxml"));
    }

    public void loadListSupplierPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/suppliers/list-supplier.fxml"));
    }

    public void loadSendEmailPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/email/send-email.fxml"));
    }

    public void loadClientReportPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/reports/client-report.fxml"));
    }

    public void loadProductReportPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/reports/product-report.fxml"));
    }

    public void loadCategoryReportPane(ActionEvent actionEvent) throws IOException {
        setDataPane(fadeAnimate("/forms/reports/category-report.fxml"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setDataPane(fadeAnimate("/forms/menu/splash.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
