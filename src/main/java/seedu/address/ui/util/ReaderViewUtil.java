package seedu.address.ui.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import net.dankito.readability4j.extended.Readability4JExtended;

import seedu.address.commons.util.DateUtil;
import seedu.address.commons.util.OptionalCandidate;
import seedu.address.commons.util.StringUtil;

/**
 * Utility functions specific to Reader View
 */
public class ReaderViewUtil {

    // Average human reading speed
    private static final int AVERAGE_WORDS_PER_MINUTE = 250;

    // Bootstrap style classes
    private static final String CONTAINER_STYLE_CLASS = "container py-5";
    private static final String IMG_RESPONSIVE_STYLE_CLASS = "img-fluid py-3";
    private static final String METADATA_WRAPPER_STYLE_CLASS = "d-flex flex-wrap pb-4";
    private static final String METADATA_STYLE_CLASS = "pr-3";
    private static final String TITLE_STYLE_CLASS = "pb-3";
    private static final String LEAD_STYLE_CLASS = "lead";

    // HTML tags
    private static final String HEADER_TAG = "h1";
    private static final String PARAGRAPH_TAG = "p";
    private static final String SMALL_TAG = "small";
    private static final String DIV_TAG = "div";
    private static final String IMG_TAG = "img";
    private static final String ANCHOR_TAG = "a";

    /**
     * Gets a document representing the reader view of the given document.
     * @param rawDocument Jsoup document from raw HTML
     * @return document representing the reader view
     */
    public static Document generateReaderViewFrom(Document rawDocument) throws IllegalArgumentException {

        // extract article using Readability4J
        Readability4J readability4J = new Readability4JExtended(rawDocument.baseUri(), rawDocument);
        Article article = readability4J.parse();

        // create empty container
        Document document = createEmptyDocument(rawDocument.baseUri());
        Element container = createContainerElement();
        document.body().appendChild(container);

        // add main article content
        createArticleElement(article)
                .map(container::prependChild)
                .orElseThrow(() -> new IllegalArgumentException("Null article content"));

        // add useful extra information at the top if available
        createArticleMetadataElement(rawDocument, article).ifPresent(container::prependChild);
        createTitleElement(article).ifPresent(container::prependChild);
        createSiteNameElement(rawDocument).ifPresent(container::prependChild);

        // prettify
        makeImagesResponsive(document);

        return document;

    }

    /**
     * Gets HTML representing the reader view of the given HTML.
     * @param rawHtml raw HTML string to process
     * @param baseUrl base URL used to resolve relative URLs to absolute URLs
     * @return HTML string representing the reader view of rawHtml string
     */
    public static String generateReaderViewStringFrom(String rawHtml, String baseUrl) throws IllegalArgumentException {
        return generateReaderViewFrom(Jsoup.parse(rawHtml, baseUrl)).outerHtml();
    }

    /**
     * Creates empty Jsoup document with given base URL
     * @param baseUrl base URL
     */
    private static Document createEmptyDocument(String baseUrl) {
        Document document = Document.createShell(baseUrl);
        document.updateMetaCharsetElement(true);
        document.charset(StandardCharsets.UTF_8);
        return document;
    }

    /**
     * Creates container element
     * @return container element
     */
    private static Element createContainerElement() {
        return new Element(Tag.valueOf(DIV_TAG), "").addClass(CONTAINER_STYLE_CLASS);
    }

    /**
     * Makes all images responsive.
     * @param document Jsoup document parsed from raw HTML
     */
    private static Document makeImagesResponsive(Document document) {
        document.select(IMG_TAG).forEach(image -> image.addClass(IMG_RESPONSIVE_STYLE_CLASS));
        return document;
    }

    /**
     * Attempts to create an article element.
     * @param article Readability4J article
     * @return optional article element
     */
    private static Optional<Element> createArticleElement(Article article) {
        return Optional.ofNullable(article.getArticleContent());
    }

    /**
     * Attempts to create an article metadata element.
     * @param rawDocument Jsoup document parsed from raw HTML
     * @param article Readability4J article
     * @return optional article metadata element
     */
    private static Optional<Element> createArticleMetadataElement(Document rawDocument, Article article) {

        Element articleMetadataWrapperElement =
                new Element(Tag.valueOf(DIV_TAG), "").addClass(METADATA_WRAPPER_STYLE_CLASS);

        // add byline element if author(s) present
        OptionalCandidate
                .with((String candidate) ->
                        // acceptable candidates are non-null and non-empty
                        Optional.ofNullable(candidate).filter(presentCandidate -> !presentCandidate.isEmpty())
                )
                .tryout(rawDocument
                        .select("head meta[property=article:author]")
                        .stream()
                        .map(author -> author.attr("content"))
                        .collect(Collectors.joining(", ")))
                .tryout(rawDocument
                        .select("head meta[property=author]")
                        .attr("content"))
                .tryout(rawDocument
                        .select("head meta[name=author]")
                        .attr("content"))
                .getOptional()
                .map(authors ->
                        new Element(Tag.valueOf(SMALL_TAG), "")
                                .text("By " + authors)
                                .addClass(METADATA_STYLE_CLASS))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // add published datetime element if published datetime is present
        Optional.of(rawDocument.select("head meta[property=article:published_time]").attr("content"))
                .filter(dateTime -> !dateTime.isEmpty())
                .map(DateUtil::getHumanReadableDateTimeFrom)
                .map(dateTime ->
                        new Element(Tag.valueOf(SMALL_TAG), "")
                                .text(dateTime)
                                .addClass(METADATA_STYLE_CLASS))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // add section element if section is present
        Optional.of(rawDocument.select("head meta[property=article:section]").attr("content"))
                .filter(section -> !section.isEmpty())
                .map(section ->
                        new Element(Tag.valueOf(SMALL_TAG), "")
                                .text("In " + section + " section")
                                .addClass(METADATA_STYLE_CLASS))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // add reading time element
        Optional.ofNullable(article.getTextContent())
                .filter(text -> !text.isEmpty())
                .map(StringUtil::getNumberOfWords)
                .map(wordCount -> Math.max(1, (int) Math.ceil(wordCount / (float) AVERAGE_WORDS_PER_MINUTE)))
                .map(minutes -> new Element(Tag.valueOf(SMALL_TAG), "")
                        .text(minutes + " minute read")
                        .addClass(METADATA_STYLE_CLASS))
                .ifPresent(articleMetadataWrapperElement::appendChild);

        // return article metadata wrapper element if it has child elements
        return Optional.of(articleMetadataWrapperElement).filter(element -> !element.children().isEmpty());

    }

    /**
     * Attempts to create a title element.
     * @param article Readability4J article
     * @return optional title element
     */
    private static Optional<Element> createTitleElement(Article article) {
        return Optional.ofNullable(article.getTitle())
                .filter(title -> !title.isEmpty())
                .map(title ->
                        new Element(Tag.valueOf(HEADER_TAG), "").text(title).addClass(TITLE_STYLE_CLASS)
                );
    }

    /**
     * Attempts to create a site name element.
     * @param document Jsoup document
     * @return optional site name element
     */
    private static Optional<Element> createSiteNameElement(Document document) {

        // acceptable candidate site names are non-null and non-empty
        OptionalCandidate<String, String> candidateSiteName = new OptionalCandidate<>(candidate ->
                Optional.ofNullable(candidate).filter(presentCandidate -> !presentCandidate.isEmpty())
        );

        String hostName = "";
        try {
            hostName = new URL(document.baseUri()).getHost();
        } catch (MalformedURLException mue) {
            // do nothing
        }

        return candidateSiteName
                .tryout(hostName)
                .tryout(document
                        .select("head meta[property=og:site_name]")
                        .attr("content"))
                .tryout(document
                        .select("head meta[name=application-name]")
                        .attr("content"))
                .getOptional()
                .map(siteName ->
                        new Element(Tag.valueOf(PARAGRAPH_TAG), "").text(siteName).addClass(LEAD_STYLE_CLASS)
                );

    }

}
