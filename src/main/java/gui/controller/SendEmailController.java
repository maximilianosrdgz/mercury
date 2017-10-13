package gui.controller;

import dao.ClientDAO;
import dao.StoreDAO;
import domain.Category;
import domain.Client;
import domain.Product;
import domain.Province;
import domain.Purchase;
import domain.Store;
import factory.emailprops.EmailPropertiesFactory;
import gui.form.SpringFxmlLoader;
import gui.util.AlertBuilder;
import gui.util.ComboBoxLoader;
import gui.util.TextFieldUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Controller
public class SendEmailController implements Initializable {

    @FXML
    private Button btnSelfSendTest;
    @FXML
    private Button btnConfigAccount;
    @FXML
    private TextField txtSubject;
    @FXML
    private Label lblClientCount;
    @FXML
    private Label lblProductCount;
    @FXML
    private Label lblCategoryCount;
    @FXML
    private Label lblProvinceCount;
    @FXML
    private VBox sendEmailForm;
    @FXML
    private Button btnSearchFile;
    @FXML
    private TextField txtFilePath;
    @FXML
    private ComboBox<String> cmbBuyer;
    @FXML
    private ComboBox<String> cmbConsultant;
    @FXML
    private Button btnOpenFormPickProducts;
    @FXML
    private Button btnOpenFormPickCategories;
    @FXML
    private Button btnOpenFormPickProvinces;
    @FXML
    private Button btnResetFilter;
    @FXML
    private Button btnApplyFilter;
    @FXML
    private TableView<Client> tblClients;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colEmail;
    @FXML
    private Button btnRemoveClient;
    @FXML
    private Button btnAddClient;
    @FXML
    private Button btnSendEmail;

    private final FileChooser fileChooser = new FileChooser();

    private ComboBoxLoader comboBoxLoader;
    private ClientDAO clientDAO;
    private MenuController menuController;
    private AlertBuilder alertBuilder;
    private StoreDAO storeDAO;
    private EmailPropertiesFactory propertiesFactory;
    private List<Product> selectedProducts;
    private List<Category> selectedCategories;
    private List<Province> selectedProvinces;

    private Store store;
    private File htmlFile;
    private Session session;

    @Autowired
    public SendEmailController(ComboBoxLoader comboBoxLoader, ClientDAO clientDAO,
                               MenuController menuController, AlertBuilder alertBuilder,
                               StoreDAO storeDAO, EmailPropertiesFactory propertiesFactory) {

        this.comboBoxLoader = comboBoxLoader;
        this.clientDAO = clientDAO;
        this.menuController = menuController;
        this.alertBuilder = alertBuilder;
        this.storeDAO = storeDAO;
        this.propertiesFactory = propertiesFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxLoader.initBooleanStringCombo(cmbBuyer, 0);
        comboBoxLoader.initBooleanStringCombo(cmbConsultant, 0);
        initClientsTable(clientDAO.findAll());
        selectedProducts = new ArrayList<>();
        selectedCategories = new ArrayList<>();
        selectedProvinces = new ArrayList<>();
        updateClientCount();
        TextFieldUtils.editable(false, txtFilePath);
        configureFileChooser();
        store = storeDAO.find(1);
        openSession();
    }

    private void initClientsTable(List<Client> clientList) {
        colName.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
        ObservableList<Client> clients = FXCollections.observableArrayList();
        clients.addAll(clientList.stream()
                .filter(Client::getBooleanReceiver)
                .collect(Collectors.toList()));
        tblClients.setItems(clients);
        tblClients.getSelectionModel().selectFirst();
    }

    public void openPickProductsForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/pick-products.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Productos");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void openPickCategoriesForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/products/pick-categories.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Categorías");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void openPickProvincesForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-provinces.fxml"), 600, 500);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Provincias");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void openFormConfigAccount(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/email/config-store.fxml"), 500, 200);
        Stage stage = new Stage();
        stage.setTitle("Configurar Negocio");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void resetFilter(ActionEvent actionEvent) throws IOException {
        Alert alert = alertBuilder.builder()
                .type(Alert.AlertType.CONFIRMATION)
                .title("Enviar Mail")
                .headerText("Reestablecer selección de clientes")
                .contentText("¿Desea cancelar la operación?")
                .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reloadForm();
        }
    }

    public void applyFilter(ActionEvent actionEvent) {
        boolean buyer = false;
        boolean consultant = false;
        boolean isBuyerSelected = true;
        boolean isConsultantSelected = true;

        if(cmbBuyer.getSelectionModel().getSelectedIndex() == 1) {
            buyer = true;
        }
        else {
            if(cmbBuyer.getSelectionModel().getSelectedIndex() == 2) {
                buyer = false;
            }
            else {
                isBuyerSelected = false;
            }
        }

        if(cmbConsultant.getSelectionModel().getSelectedIndex() == 1) {
            consultant = true;
        }
        else {
            if(cmbConsultant.getSelectionModel().getSelectedIndex() == 2) {
                consultant = false;
            }
            else {
                isConsultantSelected = false;
            }
        }

        List<Client> results = doFilterClients(isBuyerSelected, isConsultantSelected, buyer, consultant,
                selectedProducts, selectedCategories, selectedProvinces);
        initClientsTable(results);
        updateClientCount();
    }

    private List<Client> doFilterClients(boolean isBuyerSelected, boolean isConsultantSelected,
                                 boolean isBuyer, boolean isConsultant,
                                 List<Product> products, List<Category> categories, List<Province> provinces) {

        ObservableList<Client> clients = tblClients.getItems();
        Set<Client> results = new HashSet<>();

        if(isBuyerSelected) {
            results.addAll(clients.stream()
                    .filter(client -> client.getBooleanBuyer() == isBuyer)
                    .collect(Collectors.toList()));
        }

        if(isConsultantSelected) {
            results.addAll(clients.stream()
                    .filter(client -> client.getBooleanConsultant() == isConsultant)
                    .collect(Collectors.toList()));
        }

        if(!products.isEmpty()) {
            results.addAll(clients.stream()
                    .filter(client -> !Collections.disjoint(getProductsPurchased(client), products))
                    .collect(Collectors.toList()));
        }

        if(!categories.isEmpty()) {
            results.addAll(clients.stream()
                    .filter(client -> !Collections.disjoint(getCategoriesPurchased(client), categories))
                    .collect(Collectors.toList()));
        }

        if(!provinces.isEmpty()) {
            results.addAll(clients.stream()
                    .filter(client -> provinces.contains(client.getProvince()))
                    .collect(Collectors.toList()));
        }

        if(!isBuyerSelected && !isConsultantSelected && products.isEmpty() && categories.isEmpty() && provinces.isEmpty()) {
            return clients;
        }
        else {
            return new ArrayList<>(results);
        }
    }

    private List<Product> getProductsPurchased(Client client) {
        List<Product> results = new ArrayList<>();
        for(Purchase p : client.getPurchases()) {
            p.getPurchaseDetails().forEach(d -> results.add(d.getProduct()));
        }
        return results;
    }

    private List<Category> getCategoriesPurchased(Client client) {
        List<Category> results = new ArrayList<>();
        for(Purchase p : client.getPurchases()) {
            p.getPurchaseDetails().forEach(d -> results.addAll(d.getProduct().getCategories()));
        }
        return results;
    }

    public void removeClient(ActionEvent actionEvent) {
        tblClients.getItems().remove(tblClients.getSelectionModel().getSelectedItem());
        updateClientCount();
    }

    public void openPickClientForm(ActionEvent actionEvent) {
        Scene scene = new Scene((Parent) SpringFxmlLoader.load("/forms/clients/pick-client-email.fxml"), 1130, 600);
        Stage stage = new Stage();
        stage.setTitle("Seleccionar Cliente");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sendEmailForm.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void selfSendTest(ActionEvent actionEvent) {
        if(TextFieldUtils.fieldsFilled(txtFilePath)) {
            Alert alert;
            if (!TextFieldUtils.fieldsFilled(txtSubject)) {
                alert = alertBuilder.buildAlert(Alert.AlertType.CONFIRMATION, "Enviar e-mail",
                        "E-mail sin asunto.", "¿Desea enviar e-mail sin asunto?");
            } else {
                alert = alertBuilder.buildAlert(Alert.AlertType.CONFIRMATION, "Enviar e-mail",
                        "Prueba de envío", "¿Proceder con envío de prueba?");
            }
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    String content = readHTML(htmlFile) + "</br></br>Clientes: </br>" +
                            getClientNames() + "</br></br>Productos: </br>" +
                            getProductListString() + "</br></br>Categorías: </br>" +
                            getCategoryListString()  + "</br></br>Provincias: </br>" +
                            getProvinceListString();
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(store.getEmail()));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(store.getEmail()));
                    message.setSubject(String.format("%s, %s", store.getName(), txtSubject.getText()));
                    message.setContent(content, "text/html; charset=utf-8");
                    Transport.send(message);
                    alertBuilder.buildAlert(Alert.AlertType.INFORMATION, "Enviar e-mail",
                            "Prueba de envío", "Mail de prueba enviado exitosamente a " + store.getEmail())
                            .showAndWait();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (AddressException ae) {
                    ae.printStackTrace();
                } catch (AuthenticationFailedException afe) {
                    alertBuilder.buildAlert(Alert.AlertType.ERROR, "Enviar e-mail",
                            "Error en el envío",
                            "Fallo de autenticación. Corrobore usuario y contraseña de la cuenta " + store.getEmail())
                            .showAndWait();
                } catch (MessagingException me) {
                    me.printStackTrace();
                }
            }
        }
        else {
            alertBuilder.buildAlert(Alert.AlertType.INFORMATION, "Enviar e-mail",
                    "Archivo no seleccionado", "Por favor, seleccione el archivo a enviar.")
                    .showAndWait();
        }
    }

    public void sendEmail(ActionEvent actionEvent) {
        if(TextFieldUtils.fieldsFilled(txtFilePath)) {
            Alert alert;
            if(!TextFieldUtils.fieldsFilled(txtSubject)) {
                alert = alertBuilder.buildAlert(Alert.AlertType.CONFIRMATION, "Enviar e-mail",
                        "E-mail sin asunto.", "¿Desea enviar e-mail sin asunto?");
            }
            else {
                alert = alertBuilder.buildAlert(Alert.AlertType.CONFIRMATION, "Enviar e-mail",
                        "Enviando e-mail a clientes seleccionados", "¿Proceder con el envío?");
            }
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(store.getEmail()));

                    tblClients.getItems().forEach(client -> {
                        try {
                            message.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse(client.getEmail()));
                            message.setSubject(String.format("%s, %s", client.getName(), txtSubject.getText()));
                            message.setContent(readHTML(htmlFile), "text/html; charset=utf-8");
                            Transport.send(message);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                alertBuilder.buildAlert(Alert.AlertType.INFORMATION, "Enviar e-mail",
                        "Envío finalizado", "Proceso finalizado con éxito!")
                        .showAndWait();
            }
        }
        else {
            alertBuilder.buildAlert(Alert.AlertType.INFORMATION, "Enviar e-mail",
                    "Archivo no seleccionado", "Por favor, seleccione el archivo a enviar.")
                    .showAndWait();
        }
    }

    public void openSession() {
        Properties props = propertiesFactory.getProperties(getStoreEmailDomain()).createProperties();

        try {
            session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(store.getEmail(), store.getPassword());
                        }
                    });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String getStoreEmailDomain() {
        return store.getEmail().substring(store.getEmail().indexOf("@") + 1);
    }

    private String readHTML(File htmlFile) throws IOException {
        String line;
        FileReader fileReader = new FileReader(htmlFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder content = new StringBuilder(2048);

        while((line = bufferedReader.readLine()) != null) {
            content.append(line);
        }
        return content.toString();
    }

    public void loadSelectedClient(Client client) {
        if(!tblClients.getItems().contains(client)) {
            tblClients.getItems().add(client);
            tblClients.getSelectionModel().select(client);
        }
    }

    public void loadSelectedProducts(List<Product> productList) {
        selectedProducts.clear();
        selectedProducts.addAll(productList);
        lblProductCount.setText(String.format("(%d)", selectedProducts.size()));
    }

    public void loadSelectedCategories(List<Category> categoryList) {
        selectedCategories.clear();
        selectedCategories.addAll(categoryList);
        lblCategoryCount.setText(String.format("(%d)", selectedCategories.size()));
    }

    public void loadSelectedProvinces(List<Province> provinceList) {
        selectedProvinces.clear();
        selectedProvinces.addAll(provinceList);
        lblProvinceCount.setText(String.format("(%d)", selectedProvinces.size()));
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public List<Province> getSelectedProvinces() {
        return selectedProvinces;
    }

    public void reloadForm() throws IOException {
        menuController.loadSendEmailPane(null);
    }

    public void updateClientCount() {
        lblClientCount.setText("Clientes: " + tblClients.getItems().size());
    }

    public void openFileChooser(ActionEvent actionEvent) {
        htmlFile = fileChooser.showOpenDialog(sendEmailForm.getScene().getWindow());
        txtFilePath.setText(htmlFile.getAbsolutePath());
    }

    private void configureFileChooser() {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("HTML", "*.html")
        );
        fileChooser.setTitle("Enviar e-mail");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\utils"));
    }

    private String getClientNames() {
        return tblClients.getItems().stream()
                .map(Client::getName)
                .collect(Collectors.joining ("</br>"));
    }

    private String getProductListString() {
        return selectedProducts.stream()
                .map(Product::getDescription)
                .collect(Collectors.joining("</br>"));
    }

    private String getCategoryListString() {
        return selectedCategories.stream()
                .map(Category::getDescription)
                .collect(Collectors.joining("</br>"));
    }

    private String getProvinceListString() {
        return selectedProvinces.stream()
                .map(Province::getName)
                .collect(Collectors.joining("</br>"));
    }
}
