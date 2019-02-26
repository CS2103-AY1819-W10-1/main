package seedu.address.ui;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.entry.Entry;

/**
 * Panel containing the list of persons.
 */
public class EntryListPanel extends UiPart<Region> {
    private static final String FXML = "EntryListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EntryListPanel.class);

    @FXML
    private ListView<Entry> entryListView;

    public EntryListPanel(ObservableList<Entry> entryList, ObservableValue<Entry> selectedPerson,
                          Consumer<Entry> onSelectedPersonChange) {
        super(FXML);
        entryListView.setItems(entryList);
        entryListView.setCellFactory(listView -> new PersonListViewCell());
        entryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.fine("Selection in entry list panel changed to : '" + newValue + "'");
            onSelectedPersonChange.accept(newValue);
        });
        selectedPerson.addListener((observable, oldValue, newValue) -> {
            logger.fine("Selected entry changed to: " + newValue);

            // Don't modify selection if we are already selecting the selected entry,
            // otherwise we would have an infinite loop.
            if (Objects.equals(entryListView.getSelectionModel().getSelectedItem(), newValue)) {
                return;
            }

            if (newValue == null) {
                entryListView.getSelectionModel().clearSelection();
            } else {
                int index = entryListView.getItems().indexOf(newValue);
                entryListView.scrollTo(index);
                entryListView.getSelectionModel().clearAndSelect(index);
            }
        });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Entry} using a {@code EntryCard}.
     */
    class PersonListViewCell extends ListCell<Entry> {
        @Override
        protected void updateItem(Entry entry, boolean empty) {
            super.updateItem(entry, empty);

            if (empty || entry == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new EntryCard(entry, getIndex() + 1).getRoot());
            }
        }
    }

}
