package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.entry.Entry;

/**
 * An UI component that displays information of a {@code Entry}.
 */
public class EntryCard extends UiPart<Region> {

    private static final String FXML = "EntryListCard.fxml";

    private static final String[] TAG_COLOR_STYLES = { "red", "pink", "purple", "deepPurple", "indigo", "blue",
                                                       "lightBlue", "cyan", "teal", "green", "lightGreen", "lime",
                                                       "yellow", "amber", "orange", "deepOrange", "brown", "gray",
                                                       "blueGray" };

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on EntryBook level 4</a>
     */

    public final Entry entry;

    @FXML
    private HBox cardPane;
    @FXML
    private Label title;
    @FXML
    private Label id;
    @FXML
    private Label comment;
    @FXML
    private Label address;
    @FXML
    private Label link;
    @FXML
    private FlowPane tags;

    public EntryCard(Entry entry, int displayedIndex) {
        super(FXML);
        this.entry = entry;
        id.setText(displayedIndex + ". ");
        title.setText(entry.getTitle().fullTitle);
        comment.setText(entry.getComment().value);
        address.setText(entry.getAddress().value);
        address.setManaged(false); // Makes address label invisible, graphic pipeline will not manage it.
        link.setText(entry.getLink().value);
        entry.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyleFor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EntryCard)) {
            return false;
        }

        // state check
        EntryCard card = (EntryCard) other;
        return id.getText().equals(card.id.getText())
                && entry.equals(card.entry);
    }

    /**
     * Returns the color style for {@code tagName}'s label.
     */
    private String getTagColorStyleFor(String tagName) {
        /* Use hash code of tag title to generate random color, so color remains consistent between different runs.
         * Java only guarantees the hashCode method must consistently return the same integer whenever it is invoked on
         * the same object more than once during an execution of a Java application, but for practical purposes it
         * remains consistent from one execution of an application to another execution of the same application.
         */
        return TAG_COLOR_STYLES[Math.abs(tagName.hashCode()) % TAG_COLOR_STYLES.length];
    }

}
