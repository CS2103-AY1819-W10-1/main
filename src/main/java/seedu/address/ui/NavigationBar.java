package seedu.address.ui;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import seedu.address.model.ModelContext;

/**
 * The UI component that is responsible for app navigation.
 */
public class NavigationBar extends UiPart<Region> {

    public static final String SELECTED_STYLE_CLASS = "selected";
    private static final String FXML = "NavigationBar.fxml";

    @FXML
    private Button listButton;

    @FXML
    private Button archivesButton;

    @FXML
    private Button discoverButton;

    @FXML
    private Button feedsButton;

    public NavigationBar(ContextSwitchExecutor contextSwitchExecutor, ObservableValue<ModelContext> contextProperty) {
        super(FXML);

        contextProperty.addListener((observable, oldContext, newContext) -> {
            processContextSwitch(newContext);
        });
        processContextSwitch(contextProperty.getValue());

        discoverButton.setOnAction(event -> contextSwitchExecutor.executeContextSwitch(ModelContext.CONTEXT_SEARCH));
        listButton.setOnAction(event -> contextSwitchExecutor.executeContextSwitch(ModelContext.CONTEXT_LIST));
        archivesButton.setOnAction(event -> contextSwitchExecutor.executeContextSwitch(ModelContext.CONTEXT_ARCHIVES));
        feedsButton.setOnAction(event -> contextSwitchExecutor.executeContextSwitch(ModelContext.CONTEXT_FEEDS));
    }

    /**
     * Processes a context switch.
     */
    private void processContextSwitch(ModelContext context) {
        setAllButtonsToDefaultStyle();
        switch (context) {
        case CONTEXT_LIST:
            setButtonToSelectedStyle(listButton);
            break;
        case CONTEXT_ARCHIVES:
            setButtonToSelectedStyle(archivesButton);
            break;
        case CONTEXT_SEARCH:
            setButtonToSelectedStyle(discoverButton);
            break;
        case CONTEXT_FEEDS:
            setButtonToSelectedStyle(feedsButton);
            break;
        default:
            break;
        }
    }

    private void setAllButtonsToDefaultStyle() {
        setButtonToDefaultStyle(listButton);
        setButtonToDefaultStyle(archivesButton);
        setButtonToDefaultStyle(discoverButton);
        setButtonToDefaultStyle(feedsButton);
    }

    private void setButtonToDefaultStyle(Button button) {
        button.getStyleClass().remove(SELECTED_STYLE_CLASS);
    }

    private void setButtonToSelectedStyle(Button button) {
        ObservableList<String> styleClass = button.getStyleClass();
        if (styleClass.contains(SELECTED_STYLE_CLASS)) {
            return;
        }
        styleClass.add(SELECTED_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface ContextSwitchExecutor {
        /**
         * Executes the context switch.
         *
         * @see seedu.address.logic.Logic#executeContextSwitch(ModelContext)
         */
        void executeContextSwitch(ModelContext context);
    }

}
