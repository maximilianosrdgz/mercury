package gui.util;

import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class TextFieldUtils {

    public static boolean fieldsFilled(TextField... fields) {
        return Arrays.stream(fields)
                .filter(field -> field.getText().equals(""))
                .collect(Collectors.toList()).size() == 0;
    }

    public static void activated(boolean activated, TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> field.setEditable(activated));
    }

    public static void clear(TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> field.setText(""));
    }
}