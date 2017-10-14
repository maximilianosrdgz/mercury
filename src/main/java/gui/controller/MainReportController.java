package gui.controller;

import domain.Client;
import gui.util.ComboBoxLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@NoArgsConstructor
@Controller
public class MainReportController implements Initializable {

    @FXML
    private ComboBox<Integer> cmbClientYears;
    @FXML
    private ComboBox<String> cmbClientMonths;
    @FXML
    private ComboBox<Client> cmbClientClients;

    private ComboBoxLoader comboBoxLoader;

    @Autowired
    public MainReportController(ComboBoxLoader comboBoxLoader) {
        this.comboBoxLoader = comboBoxLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ComboBoxLoader.initYearsCombo(cmbClientYears);
        ComboBoxLoader.initMonthCombo(cmbClientMonths, 0);
    }
}
