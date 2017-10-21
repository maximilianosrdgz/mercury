package gui.util;

import javafx.scene.control.DatePicker;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DatePickerUtils {

    public static void initPickers(LocalDate date, DatePicker... pickers) {
        Arrays.stream(pickers)
                .forEach(picker -> picker.setValue(date));
    }

    public static void editable(boolean editable, DatePicker... pickers) {
        Arrays.stream(pickers)
                .forEach(picker -> picker.setEditable(editable));
    }
}
