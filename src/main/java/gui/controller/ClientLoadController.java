package gui.controller;

import controller.EntityManagerUtils;
import dao.ClientDAO;
import dao.ProvinceDAO;
import domain.Client;
import domain.Province;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by MaxPower on 10/09/2017.
 */
@NoArgsConstructor
@Controller
public class ClientLoadController implements Initializable {

    private EntityManagerUtils emu;
    private ClientDAO clientDAO;
    private ProvinceDAO provinceDAO;

    @FXML
    private ComboBox<Province> cmbProvinces;
    @FXML
    private ComboBox cmbBirthYears;
    @FXML
    private Button btnSaveClient;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private CheckBox chkBlacklisted;
    @FXML
    private CheckBox chkBuyer;
    @FXML
    private CheckBox chkConsultant;

    @Autowired
    public ClientLoadController(EntityManagerUtils emu, ClientDAO clientDAO, ProvinceDAO provinceDAO) {
        this.emu = emu;
        this.clientDAO = clientDAO;
        this.provinceDAO = provinceDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProvinceCombo();
        initBirthYearsCombo();
    }

    public void saveClient(ActionEvent actionEvent) {
        Client client = buildClient();
        clientDAO.create(client);
        buildAlert("Cliente Guardado", "Nuevo Cliente: " + clientDAO.find(client.getId()).getName()).showAndWait();
    }

    private Alert buildAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(header);
        alert.setContentText(content);

        return alert;
    }

    public void sendEmail() {
        final String USERNAME = "mizu.store.cba@gmail.com";
        final String PASSWORD = "PaRaDiSeKiSs1987";
        final String HTML_FILE_PATH = "D:/Projects/mercury/src/main/resources/utils/hyperlink-image.html";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            Client client = Client.builder()
                    .name("Maxi")
                    .email("maxi.mxpw@gmail.com")
                    .build();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(client.getEmail()));
            message.setSubject("Hola, " + client.getName() + "!");
            message.setContent(readHTML(HTML_FILE_PATH), "text/html; charset=utf-8");

            Transport.send(message);

            buildAlert("E-Mail Enviado", "E-Mail enviado exitosamente!").showAndWait();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readHTML(String path) throws IOException {
        String line;
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder content = new StringBuilder(1024);

        while((line = br.readLine()) != null)
        {
            content.append(line);
        }

        return content.toString();
    }

    private Client buildClient() {
        return Client.builder()
                .name(txtName.getText())
                .email(txtEmail.getText())
                .province(cmbProvinces.getSelectionModel().getSelectedItem())
                .birthYear((Integer) cmbBirthYears.getSelectionModel().getSelectedItem())
                .buyer(chkBuyer.isSelected())
                .consultant(chkConsultant.isSelected())
                .blackListed(chkBlacklisted.isSelected())
                .build();
    }

    private void initProvinceCombo() {
        ObservableList<Province> provinceList = FXCollections.observableArrayList();
        provinceList.addAll(provinceDAO.findAll());
        cmbProvinces.setItems(provinceList);
        cmbProvinces.getSelectionModel().selectFirst();
    }

    private void initBirthYearsCombo() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= 1940; i--) {
            years.add(i);
        }
        cmbBirthYears.setItems(years);
        cmbBirthYears.getSelectionModel().selectFirst();
    }
}
