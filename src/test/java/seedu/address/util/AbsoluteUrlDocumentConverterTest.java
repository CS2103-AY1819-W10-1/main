package seedu.address.util;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.junit.Test;

public class AbsoluteUrlDocumentConverterTest {

    private URL baseUrl = new URL("https://en.wikipedia.org/wiki/Therapsid");

    public AbsoluteUrlDocumentConverterTest() throws MalformedURLException {
    }

    @Test
    public void convert_linksProperlyConverted() {
        assertLinkConversion(
                "<!doctype html><body><a href=\"%s\"></a></body>");
        assertLinkConversion(
                "<!doctype html><body><a junk=\"foo\" href=\"%s\"></a></body>");
        assertLinkConversion(
                "<!doctype html><body><a href=\"%s\" junk=\"foo\"></a></body>");
    }

    @Test
    public void convert_styleSheetLinkProperlyConverted() {
        assertLinkConversion(
                "<!doctype html><head><link rel=\"stylesheet\" href=\"%s\"></head><body></body>");
        assertLinkConversion(
                "<!doctype html><head><link rel=\"stylesheet\" junk=\"foo\" href=\"%s\"></head><body></body>");
        assertLinkConversion(
                "<!doctype html><head><link rel=\"stylesheet\" href=\"%s\" junk=\"foo\"></head><body></body>");
    }

    @Test
    public void convert_imgLinkProperlyConverted() {
        assertLinkConversion(
                "<!doctype html><head></head><body><img src=\"%s\"></body>");
        assertLinkConversion(
                "<!doctype html><head></head><body><img junk=\"foo\" src=\"%s\"></body>");
        assertLinkConversion(
                "<!doctype html><head></head><body><img src=\"%s\" junk=\"foo\"></body>");
    }

    @Test
    public void convert_scriptLinkProperlyConverted() {
        assertLinkConversion(
                "<!doctype html><head></head><body><script src=\"%s\"></body>");
        assertLinkConversion(
                "<!doctype html><head></head><body><script junk=\"foo\" src=\"%s\"></body>");
        assertLinkConversion(
                "<!doctype html><head></head><body><script src=\"%s\" junk=\"foo\"></body>");
    }

    @Test
    public void convert_styleUrlsProperlyConverted() {
        // Buggy rules are left untouched
        String buggyDoc =
            "<!doctype html><body><div style=\"background-image: url('; background-image: url(\"??')\"></div></body>";
        assertSuccessfulConversion(buggyDoc, baseUrl, buggyDoc);

        // Converts url(...) in CSS properly
        assertLinkConversion(
            "<!doctype html><head></head><body><div style=\"background-image: url(%s)\"></div></body>");
        assertLinkConversion(
            "<!doctype html><head></head><body><div style=\"color: red; background-image: url(%s)\"></div></body>");
        assertLinkConversion(
            "<!doctype html><head></head><body><div style=\"background-image: url(%s); color: red\"></div></body>");

        // Cases like url('...') and url("...") are transparently handled by CSS parser,
        // No need to test.
    }

    /**
     * Asserts that using the given format string to substitute in various links,
     * that the document will undergo the conversion to absolute links correctly.
     */
    private void assertLinkConversion(String formatString) {

        // Absolute links are preserved
        assertSuccessfulConversion(
                String.format(formatString, "http://absolute.link/and/path"),
                baseUrl,
                String.format(formatString, "http://absolute.link/and/path")
        );

        // Protocol relative links are converted
        assertSuccessfulConversion(
                String.format(formatString, "https://absolute.link/and/path"),
                baseUrl,
                String.format(formatString, "//absolute.link/and/path")
        );

        // Domain relative links are converted
        assertSuccessfulConversion(
                String.format(formatString, "https://en.wikipedia.org/and/path"),
                baseUrl,
                String.format(formatString, "/and/path")
        );

        // Relative links are converted
        assertSuccessfulConversion(
                String.format(formatString, "https://en.wikipedia.org/wiki/and/path"),
                baseUrl,
                String.format(formatString, "and/path")
        );

    }

    /**
     * Asserts that the conversion of inputDocument was successful and its result matches the expected document.
     */
    private void assertSuccessfulConversion(String expected, URL baseUrl, String inputDocument) {
        assertEquals(
                new String(normalizeHtml(expected.getBytes())),
                new String(normalizeHtml(AbsoluteUrlDocumentConverter.convert(baseUrl, inputDocument.getBytes()))));
    }

    /**
     * Formats the html to some clean standardised output format
     */
    private byte[] normalizeHtml(byte[] html) {
        // We parse and output until it stops differing.
        // Sometimes after 1 or 2 passes there can still be whitespace differences.
        // I didn't want to hardcode 3 passes because I'm no longer sure 3 passes would be enough.
        String cur = new String(html);
        String prev = null;
        while (!cur.equals(prev)) {
            prev = cur;
            cur = Jsoup.parse(cur).toString();
        }
        return cur.getBytes();
    }
}
