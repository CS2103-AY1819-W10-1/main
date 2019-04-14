package seedu.address.util;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import com.steadystate.css.util.ThrowCssExceptionErrorHandler;

/**
 * Converts all the urls in a document to its absolute equivalent,
 * so it can be saved on disk and used again later.
 */
public abstract class AbsoluteUrlDocumentConverter {

    /**
     * Converts all the urls in a document to its absolute equivalent,
     * so it can be saved on disk and used again later.
     */
    public static byte[] convert(URL baseUrl, byte[] articleContent) {
        Document document = Jsoup.parse(new String(articleContent));
        return convert(baseUrl, document).normalise().toString().getBytes();
    }

    /**
     * Converts all the urls in a document to its absolute equivalent,
     * so it can be saved on disk and used again later.
     */
    private static Document convert(URL baseUrl, Document document) {
        // Convert all href and src attributes
        for (Element e : document.getAllElements()) {

            if (e.hasAttr("href")) {
                String link = e.attr("href");
                e.attr("href", convertUrl(baseUrl, link));
            }

            if (e.hasAttr("src")) {
                String link = e.attr("src");
                e.attr("src", convertUrl(baseUrl, link));
            }

            if (e.hasAttr("style")) {
                String cssAttr = e.attr("style");
                e.attr("style", convertCssUrls(baseUrl, cssAttr));
            }

        }

        return document;
    }

    /**
     * Converts urls in a CSS style declaration to absolute form.
     */
    private static String convertCssUrls(URL baseUrl, String cssAttribute) {
        CSSOMParser cssParser = new CSSOMParser(new SACParserCSS3());
        cssParser.setErrorHandler(new ThrowCssExceptionErrorHandler());
        Pattern urlPattern = Pattern.compile("url\\((.*)\\)");
        try {
            // Parse style
            CSSStyleDeclaration cssStyleDecl = cssParser.parseStyleDeclaration(
                new InputSource(new StringReader(cssAttribute)));

            // Loop through each attribute in decl
            for (int i = 0; i < cssStyleDecl.getLength(); i++) {

                // Check if it matches url(...) pattern
                String property = cssStyleDecl.item(i);
                CSSValue propertyValue = cssStyleDecl.getPropertyCSSValue(property);
                String propertyPriority = cssStyleDecl.getPropertyPriority(property);
                Matcher urlPropMatch = urlPattern.matcher(propertyValue.toString());
                if (!urlPropMatch.matches()) {
                    continue;
                }

                // If it does, convert link
                String url = urlPropMatch.toMatchResult().group(1);
                String convertedUrl = convertUrl(baseUrl, url);
                String newPropertyValue = "url(" + convertedUrl + ")";

                // And set it back in the style decl
                cssStyleDecl.setProperty(property, newPropertyValue, propertyPriority);
            }

            return cssStyleDecl.getCssText();
        } catch (IOException | CSSException ioe) {
            // IOException will never happen since we use InputSource StringReader combo
            // If CSS parsing had error, preserve style completely.
            return cssAttribute;
        }

    }

    /**
     * Converts a URL to absolute form.
     */
    private static String convertUrl(URL baseUrl, String url) {
        try {
            return new URL(baseUrl, url).toExternalForm();
        } catch (MalformedURLException mue) {
            // Do nothing if URL is malformed, basically don't convert
            return url;
        }
    }

}
