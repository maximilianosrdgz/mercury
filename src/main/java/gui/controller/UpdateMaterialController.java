package gui.controller;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.MaterialStockDAO;
import domain.Category;
import domain.Material;
import domain.MaterialStock;
import gui.util.AlertBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Controller
public class UpdateMaterialController {

    @FXML
    private Button btnSaveMaterial;

    private MaterialDAO materialDAO;
    private MaterialStockDAO materialStockDAO;
    private CategoryDAO categoryDAO;
    private AlertBuilder alertBuilder;

    @Autowired
    public UpdateMaterialController(MaterialDAO materialDAO, MaterialStockDAO materialStockDAO,
                                    CategoryDAO categoryDAO, AlertBuilder alertBuilder) {

        this.materialDAO = materialDAO;
        this.materialStockDAO = materialStockDAO;
        this.categoryDAO = categoryDAO;
        this.alertBuilder = alertBuilder;
    }

    public void saveMaterial(ActionEvent actionEvent) {
        Set<Category> categories = new HashSet<>();
        categories.add(categoryDAO.find(1));
        Material material = Material.builder()
                .description("Material")
                .cost(10)
                .categories(categories)
                .build();
        materialDAO.create(material);
    }

    public void findMaterial(ActionEvent actionEvent) {
        alertBuilder.builder()
                .type(Alert.AlertType.INFORMATION)
                .contentText(materialDAO.find(1).toString())
                .build()
                .showAndWait();
    }

    public void saveStock(ActionEvent actionEvent) {
        MaterialStock stock = MaterialStock.builder()
                .material(materialDAO.find(1))
                .quantity(100)
                .storeType("Unidad")
                .build();
        //materialStockDAO.create(stock);
        alertBuilder.builder()
                .type(Alert.AlertType.INFORMATION)
                .contentText(materialStockDAO.find(1).toString())
                .build()
                .showAndWait();

    }

    public void filterWithEnter(KeyEvent keyEvent) {
    }

    public void reloadForm(ActionEvent actionEvent) {
    }
}
