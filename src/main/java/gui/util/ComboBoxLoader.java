package gui.util;

import dao.CategoryDAO;
import domain.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class ComboBoxLoader {

    private CategoryDAO categoryDAO;

    @Autowired
    public ComboBoxLoader (CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
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
}
