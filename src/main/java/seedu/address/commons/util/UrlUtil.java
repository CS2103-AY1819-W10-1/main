package seedu.address.commons.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Contains URL utility methods.
 */
public class UrlUtil {

    /**
     * Attempts to convert a string to a URL
     * @param urlString string to convert to URL
     * @return Optional of URL if conversion successful, else empty Optional
     */
    public static Optional<URL> fromString(String urlString) {
        return Optional
                .ofNullable(urlString)
                .map(string -> {
                    try {
                        return new URL(string);
                    } catch (MalformedURLException mue) {
                        return null;
                    }
                });
    }

}
