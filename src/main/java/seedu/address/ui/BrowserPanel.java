package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.UrlUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.entry.Entry;
import seedu.address.ui.util.ReaderViewUtil;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String BROWSER_FILE_FOLDER = "/browser/";

    public static final URL DEFAULT_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "default.html"));
    public static final URL ERROR_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "error.html"));
    public static final URL READER_VIEW_FAILURE_PAGE =
            requireNonNull(MainApp.class.getResource(BROWSER_FILE_FOLDER + "reader_view_failure.html"));
    private static final List<URL> INTERNAL_URLS = List.of(DEFAULT_PAGE, ERROR_PAGE, READER_VIEW_FAILURE_PAGE);

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;

    private WebEngine webEngine = browser.getEngine();

    private URL lastExternalUrl; // URL of the last external page - should be offline or online type
    private URL lastEntryUrl; // URL of the last selected entry - should be online type
    private ViewMode viewMode; // current view mode
    private final Function<URL, Optional<URL>> getOfflineUrl; // gets the offline URL for the given URL
    private final Function<URL, Optional<String>> getArticle; // gets the stored offline article for the given URL

    public BrowserPanel(ObservableValue<Entry> selectedEntry,
                        ObservableValue<ViewMode> viewMode,
                        Function<URL, Optional<URL>> getOfflineUrl,
                        Function<URL, Optional<String>> getArticle) {
        super(FXML);

        // Initialization
        this.lastExternalUrl = null;
        this.lastEntryUrl = null;
        this.viewMode = viewMode.getValue();
        this.getOfflineUrl = getOfflineUrl;
        this.getArticle = getArticle;

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        // React to browser loading state changes.
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            switch (newState) {
            case RUNNING:
                handleRunning();
                break;
            case SUCCEEDED:
                handleSucceeded();
                break;
            case FAILED:
                handleFailed();
                break;
            default:
                break;
            }
        });

        // React to current location changes.
        webEngine.locationProperty().addListener((observable, oldLocation, newLocation) -> {
            // Update state - update last URL if new URL is external (offline or online, not internal or content)
            UrlUtil.fromString(newLocation)
                    .filter(newUrl -> !INTERNAL_URLS.contains(newUrl))
                    .ifPresent(newUrl -> lastExternalUrl = newUrl);
        });

        // React to selected entry changes.
        selectedEntry.addListener((observable, oldEntry, newEntry) -> {
            // Next actions - load new selected entry page if possible
            Optional.ofNullable(newEntry).ifPresentOrElse(this::loadEntryPage, this::loadDefaultPage);
        });

        // React to view mode changes.
        viewMode.addListener((observable, oldViewMode, newViewMode) -> {
            // Update state
            this.viewMode = newViewMode;

            // Next actions
            /* STRATEGY:
             * If changing to reader view and currently in content, only change stylesheet.
             * If changing to reader view and currently in offline page,
             * directly load reader view content if possible or else load reader view of current document.
             * If changing to reader view and currently in online page, load reader view of current document.
             * If changing to browser view and currently in internal page or content, reload last URL.
             */
            if (newViewMode.hasReaderViewType() && currentlyInContent()) {
                setStyleSheet(newViewMode.getReaderViewStyle().getStylesheetLocation());
            } else if (newViewMode.hasReaderViewType() && currentlyInOfflinePage()) {
                getArticle
                        .apply(lastEntryUrl)
                        .ifPresentOrElse(this::loadReaderOfLastEntry, this::loadReaderOfLastEntry);
            } else if (newViewMode.hasReaderViewType() && currentlyInOnlinePage()) {
                loadReaderOfLastUrl();
            } else if (newViewMode.hasBrowserViewType() && !currentlyInExternalPage()) {
                loadPageOfLastUrl();
            }
        });

        // Load default page on startup.
        loadDefaultPage();
    }

    //=========== Loading state handlers ===============================================================================

    /**
     * Handler for running state.
     */
    private void handleRunning() {
        logCurrentUrlAndTypeWithStatus("Loading");
    }

    /**
     * Handler for succeeded state.
     */
    private void handleSucceeded() {
        logCurrentUrlAndTypeWithStatus("Successfully loaded");

        // Next actions
        /* STRATEGY:
         * If using reader view mode, and if currently in online page, load reader view of current loaded document,
         * but if currently in offline page, load reader view of current loaded document with last entry as base URL.
         */
        if (viewMode.hasReaderViewType() && currentlyInOnlinePage()) {
            loadReaderOfLastUrl();
        } else if (viewMode.hasReaderViewType() && currentlyInOfflinePage()) {
            loadReaderOfLastEntry();
        }
    }

    /**
     * Handler for failed state.
     */
    private void handleFailed() {
        logCurrentUrlAndTypeWithStatus("Failed to load");

        // Next actions
        loadErrorPage();
    }

    /**
     * Handler for reader view failed state.
     */
    private void handleReaderViewFailure() {
        logCurrentUrlAndTypeWithStatus("Failed to load reader view of");

        // Next actions
        loadReaderViewFailurePage();
    }

    //=========== State methods ========================================================================================

    /**
     * Finds out the type of the given URL.
     * @param currentUrl URL to find type of
     * @return type of the currentUrl
     */
    private UrlType getUrlType(Optional<URL> currentUrl) {
        if (!currentUrl.isPresent()) {
            return UrlType.CONTENT;
        } else if (INTERNAL_URLS.contains(currentUrl.get())) {
            return UrlType.INTERNAL;
        } else if (currentUrl.get().getProtocol().contains("file")) {
            return UrlType.OFFLINE;
        } else {
            return UrlType.ONLINE;
        }
    }

    private UrlType getCurrentUrlType() {
        return getUrlType(UrlUtil.fromString(webEngine.getLocation()));
    }

    private boolean currentlyInInternalPage() {
        return getCurrentUrlType().equals(UrlType.INTERNAL);
    }

    private boolean currentlyInOnlinePage() {
        return getCurrentUrlType().equals(UrlType.ONLINE);
    }

    private boolean currentlyInOfflinePage() {
        return getCurrentUrlType().equals(UrlType.OFFLINE);
    }

    private boolean currentlyInContent() {
        return getCurrentUrlType().equals(UrlType.CONTENT);
    }

    private boolean currentlyInExternalPage() {
        return currentlyInOnlinePage() || currentlyInOfflinePage();
    }

    /**
     * Logs current URL with its type and loading status.
     * @param status loading status
     */
    private void logCurrentUrlAndTypeWithStatus(String status) {
        Optional<URL> currentUrl = UrlUtil.fromString(webEngine.getLocation());
        UrlType currentUrlType = getUrlType(currentUrl);

        String message = String.format("%s %s: %s", status, currentUrlType, currentUrl.orElse(lastExternalUrl));
        logger.info(message);
    }

    //=========== Convenience methods ==================================================================================

    private void loadDefaultPage() {
        loadPage(DEFAULT_PAGE.toExternalForm());
    }

    private void loadErrorPage() {
        loadPage(ERROR_PAGE.toExternalForm());
    }

    private void loadReaderViewFailurePage() {
        loadPage(READER_VIEW_FAILURE_PAGE.toExternalForm());
    }

    private void loadPageOfLastUrl() {
        loadPage(lastExternalUrl.toExternalForm());
    }

    private void loadPageOfLastEntry() {
        loadPage(lastEntryUrl.toExternalForm());
    }

    private void loadReaderOfLastUrl() {
        loadReader(lastExternalUrl.toExternalForm());
    }

    private void loadReaderOfLastEntry() {
        loadReader(lastEntryUrl.toExternalForm());
    }

    private void loadReaderOfLastEntry(String rawHtml) {
        loadReader(rawHtml, lastEntryUrl.toExternalForm());
    }

    //=========== Advanced methods =====================================================================================

    /**
     * Loads the given entry's web page.
     * @param entry entry to load
     */
    private void loadEntryPage(Entry entry) {
        // Update state
        lastEntryUrl = entry.getLink().value;

        // Next actions
        /* STRATEGY:
         * For reader view mode, if offline article is available, directly load reader view content,
         * else load online page and wait for handleSucceeded to load reader view.
         * For browser view mode, load offline page of entry if possible, else load online page of entry.
         */
        if (viewMode.hasReaderViewType()) {
            getOfflineUrl
                    .apply(lastEntryUrl) // get entry's offline URL if available
                    .map(url -> lastExternalUrl = url) // update last URL manually as HTML content is directly loaded
                    .flatMap(unused -> getArticle.apply(lastEntryUrl)) // get HTML content
                    .ifPresentOrElse(this::loadReaderOfLastEntry, this::loadPageOfLastEntry);
        } else {
            getOfflineUrl
                    .apply(lastEntryUrl) // get entry's offline URL if available
                    .map(URL::toExternalForm)
                    .ifPresentOrElse(this::loadPage, this::loadPageOfLastEntry);
        }
    }

    /**
     * Loads reader view of current content.
     * Assumes original web page is already loaded.
     * @param baseUrl base URL used to resolve relative links to absolute links
     */
    private void loadReader(String baseUrl) {
        // Next actions
        try {
            String documentHtml = XmlUtil.convertDocumentToString(webEngine.getDocument());
            loadReader(documentHtml, baseUrl);
        } catch (TransformerException te) {
            handleReaderViewFailure();
        }
    }

    /**
     * Loads reader view of given HTML.
     * @param rawHtml HTML to generate reader view of
     * @param baseUrl base URL used to resolve relative links to absolute links
     */
    private void loadReader(String rawHtml, String baseUrl) {
        // Next actions
        setStyleSheet(viewMode.getReaderViewStyle().getStylesheetLocation());
        try {
            String readerViewHtml = ReaderViewUtil.generateReaderViewStringFrom(rawHtml, baseUrl);
            loadContent(readerViewHtml);
        } catch (IllegalArgumentException e) {
            handleReaderViewFailure();
        }
    }

    //=========== Base methods =========================================================================================

    /**
     * Loads a Web page.
     * @param url URL of the Web page to load
     */
    private void loadPage(String url) {
        webEngine.setUserStyleSheetLocation(null); // reset stylesheet
        webEngine.load(url);
    }

    /**
     * Loads HTML content.
     * @param html HTML content to load
     */
    private void loadContent(String html) {
        webEngine.loadContent(html);
    }

    /**
     * Sets the CSS stylesheet.
     * @param styleSheetUrl URL of the stylesheet to set
     */
    private void setStyleSheet(URL styleSheetUrl) {
        try {
            Platform.runLater(() -> webEngine.setUserStyleSheetLocation(styleSheetUrl.toExternalForm()));
        } catch (IllegalArgumentException | NullPointerException e) {
            String message = "Failed to set user style sheet location";
            logger.warning(message);
        }
    }

}
