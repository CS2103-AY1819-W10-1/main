package seedu.address.ui;

/**
 * Represents internal interpretation of URLs into URL types.
 */
public enum UrlType {
    INTERNAL("internal page"),
    ONLINE("online page"),
    OFFLINE("offline page"),
    CONTENT("HTML content");

    private String friendlyName;

    UrlType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
