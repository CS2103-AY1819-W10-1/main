package seedu.address.ui;

import org.apache.commons.text.WordUtils;

/**
 * Represents an immutable view mode that comprises a view type and reader view style.
 */
public class ViewMode {

    private ViewType viewType;
    private ReaderViewStyle readerViewStyle;

    public ViewMode(ViewType viewType, ReaderViewStyle readerViewStyle) {
        this.viewType = viewType;
        this.readerViewStyle = readerViewStyle;
    }

    public ViewMode(ViewType viewType) {
        this(viewType, ReaderViewStyle.DEFAULT);
    }

    public ViewMode() {
        this(ViewType.BROWSER);
    }

    public ViewType getViewType() {
        return viewType;
    }

    public ReaderViewStyle getReaderViewStyle() {
        return readerViewStyle;
    }

    @Override
    public String toString() {
        if (viewType.equals(ViewType.READER)) {
            return String.format("%s view in %s style",
                    WordUtils.capitalize(this.viewType.toString()),
                    WordUtils.capitalize(this.readerViewStyle.toString()));
        }
        return String.format("%s view", WordUtils.capitalize(this.viewType.toString()));
    }

    @Override
    public boolean equals(Object obj) {

        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ViewMode)) {
            return false;
        }

        // state check
        ViewMode other = (ViewMode) obj;
        return this.viewType.equals(other.viewType) && this.readerViewStyle.equals(other.readerViewStyle);

    }

    public boolean hasBrowserViewType() {
        return this.viewType.equals(ViewType.BROWSER);
    }

    public boolean hasReaderViewType() {
        return this.viewType.equals(ViewType.READER);
    }
}
