package gui.util;

import dao.CategoryDAO;
import dao.MaterialDAO;
import dao.ProductDAO;
import domain.Category;
import domain.Material;
import domain.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class ComboBoxLoader {

    private CategoryDAO categoryDAO;
    private MaterialDAO materialDAO;
    private ProductDAO productDAO;

    @Autowired
    public ComboBoxLoader (CategoryDAO categoryDAO, MaterialDAO materialDAO, ProductDAO productDAO) {
        this.categoryDAO = categoryDAO;
        this.materialDAO = materialDAO;
        this.productDAO = productDAO;
    }

    public static void initBirthYearsCombo(ComboBox<Integer> comboBox) {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1940; i--) {
            years.add(i);
        }
        comboBox.setItems(years);
        comboBox.getSelectionModel().selectFirst();
    }

    public void initCategoriesCombo(ComboBox<Category> comboBox, int selectedIndex) {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryDAO.findAll());
        comboBox.setItems(categories);
        comboBox.getSelectionModel().select(selectedIndex);
    }

    public void initMaterialsCombo(ComboBox<Material> comboBox, int selectedIndex) {
        ObservableList<Material> materials = FXCollections.observableArrayList();
        materials.addAll(materialDAO.findAll());
        comboBox.setItems(materials);
        comboBox.getSelectionModel().select(selectedIndex);
    }

    public void initProductsCombo(ComboBox<Product> comboBox, int selectedIndex) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        products.addAll(productDAO.findAll());
        comboBox.setItems(products);
        comboBox.getSelectionModel().select(selectedIndex);
    }
}
