package gui.util;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Component;

@Component
public class AlertBuilder {

    private Alert alert;

    public AlertBuilder builder() {
        return this;
    }

    public AlertBuilder type(Alert.AlertType alertType) {
        alert = new Alert(alertType);
        return this;
    }

    public AlertBuilder title(String title) {
        alert.setTitle(title);
        return this;
    }

    public AlertBuilder headerText(String header) {
        alert.setHeaderText(header);
        return this;
    }

    public AlertBuilder contentText(String content) {
        alert.setContentText(content);
        return this;
    }

    public Alert build() {
        return alert;
    }

    public Alert buildAlert(Alert.AlertType type, String title, String header, String content) {
        return builder()
                .type(type)
                .title(title)
                .headerText(header)
                .contentText(content)
                .build();
    }
}
