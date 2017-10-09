package gui.controller;

import gui.form.SpringFxmlLoader;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class MenuController {

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
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
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
}
