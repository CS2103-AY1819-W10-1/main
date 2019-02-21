package seedu.address.network;

/**
 * Manager of Network component
 */
public abstract class Network {

    public static String fetch(String targetUrl) {
        return lookupUrl(targetUrl);
    }

    private static String lookupUrl(String targetUrl) {
        return "Hello World";
    }
}
