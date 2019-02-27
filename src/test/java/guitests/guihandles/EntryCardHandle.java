package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMultiset;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.entry.Entry;

/**
 * Provides a handle to a entry card in the entry list panel.
 */
public class EntryCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String TITLE_FIELD_ID = "#title";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String COMMENT_FIELD_ID = "#comment";
    private static final String LINK_FIELD_ID = "#link";
    private static final String TAGS_FIELD_ID = "#tags";

    private final Label idLabel;
    private final Label titleLabel;
    private final Label addressLabel;
    private final Label commentLabel;
    private final Label linkLabel;
    private final List<Label> tagLabels;

    public EntryCardHandle(Node cardNode) {
        super(cardNode);

        idLabel = getChildNode(ID_FIELD_ID);
        titleLabel = getChildNode(TITLE_FIELD_ID);
        addressLabel = getChildNode(ADDRESS_FIELD_ID);
        commentLabel = getChildNode(COMMENT_FIELD_ID);
        linkLabel = getChildNode(LINK_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getComment() {
        return commentLabel.getText();
    }

    public String getLink() {
        return linkLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    public List<String> getTagStyleClasses(String tag) {
        return tagLabels
                .stream()
                .filter(label -> label.getText().equals(tag))
                .map(Label::getStyleClass)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such tag."));
    }

    /**
     * Returns true if this handle contains {@code entry}.
     */
    public boolean equals(Entry entry) {
        return getTitle().equals(entry.getTitle().fullTitle)
                && getAddress().equals(entry.getAddress().value)
                && getComment().equals(entry.getComment().value)
                && getLink().equals(entry.getLink().value)
                && ImmutableMultiset.copyOf(getTags()).equals(ImmutableMultiset.copyOf(entry.getTags().stream()
                        .map(tag -> tag.tagName)
                        .collect(Collectors.toList())));
    }
}
