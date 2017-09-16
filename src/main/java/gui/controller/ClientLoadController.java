package gui.controller;

import controller.EntityManagerUtils;
import dao.CategoryDAO;
import dao.ClientDAO;
import dao.ProvinceDAO;
import domain.Client;
import domain.Province;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
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
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by MaxPower on 10/09/2017.
 */
@NoArgsConstructor
@Controller
public class ClientLoadController {

    private EntityManagerUtils emu;
    private ClientDAO clientDAO;

    @Autowired
    public ClientLoadController(EntityManagerUtils emu, ClientDAO clientDAO) {
        this.emu = emu;
        this.clientDAO = clientDAO;
    }

    public void saveClient(ActionEvent actionEvent) {
        clientDAO.create(emu.buildClient());
        buildAlert("Client Saved", clientDAO.find(1).getName()).showAndWait();
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
}
