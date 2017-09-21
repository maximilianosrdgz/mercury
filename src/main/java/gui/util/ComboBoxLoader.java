package gui.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class ComboBoxLoader {

    public static void initBirthYearsCombo(ComboBox comboBox) {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1940; i--) {
            years.add(i);
        }
        comboBox.setItems(years);
        comboBox.getSelectionModel().selectFirst();
    }
}
