package gui.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    public static void editable(boolean editable, TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> field.setEditable(editable));
    }

    public static void clear(TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> field.setText(""));
    }

    public static void activated(boolean activated, TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> field.setDisable(!activated));
    }

    public static void setDecimalOnly(TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> field.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d{0,7}([\\.]\\d{0,2})?")) {
                            field.setText(oldValue);
                        }
                    }
                }));
    }

    public static void setNumericOnly(TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> field.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.matches("\\d{0,5}")) {
                            field.setText(oldValue);
                        }
                    }
                }));
    }

    public static void setZeroIfPoint(TextField... fields) {
        Arrays.stream(fields)
                .forEach(field -> {
                    if(field.getText().equals(".")) {
                        field.setText("0");
                    }
                });
    }
}
