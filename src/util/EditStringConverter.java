package util;

import javafx.util.StringConverter;

public class EditStringConverter extends StringConverter<Integer>{
    @Override
    public String toString(Integer object) {
        if (object == null) {
            return "";
        } return object.toString();
    }

    @Override
    public Integer fromString(String string) {
        if (string.isEmpty()) {
            return 0;
        } return Integer.parseInt(string);
    }
}
