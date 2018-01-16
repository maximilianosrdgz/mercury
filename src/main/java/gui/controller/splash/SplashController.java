package gui.controller.splash;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SplashController implements Initializable {

    @FXML
    private ImageView splashImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("/mercury-background.jpg");
        splashImage.setImage(image);
    }
}
