package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        resultDisplay.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = resultDisplay.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    public void setFeedbackErrorToUser(String feedbackToUser) {
        setStyleToIndicateCommandFailure();
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }

    public void setFeedbackSuccessToUser(String feedbackToUser) {
        setStyleToDefault();
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }

}
