package gui.util;

import javafx.scene.control.Button;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ButtonUtils {

    public static void activated(boolean activated, Button... buttons) {
        Arrays.stream(buttons)
                .forEach(button -> button.setDisable(!activated));
    }
}
