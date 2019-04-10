package seedu.address.model.util;

import javafx.beans.property.SimpleObjectProperty;

public class ForceNotifyObjectProperty<T> extends SimpleObjectProperty<T> {
    @Override
    public void set(T value) {
        super.set(value);
        fireValueChangedEvent();
    }
    @Override
    public void fireValueChangedEvent() {
        super.fireValueChangedEvent();
    }
}
