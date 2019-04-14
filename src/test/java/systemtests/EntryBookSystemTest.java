package systemtests;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.address.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.address.ui.testutil.GuiTestAssert.assertListMatching;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.EntryListPanelHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import seedu.address.TestApp;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.ClearListCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.testutil.TypicalEntries;
import seedu.address.ui.BrowserPanel;
import seedu.address.ui.CommandBox;
import seedu.address.ui.ResultDisplay;

/**
 * A system test class for EntryBook, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class EntryBookSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private static final List<String> RESULT_DISPLAY_DEFAULT_STYLE = Arrays.asList("text-input",
                                                                                   "text-area",
                                                                                   "result-display");
    private static final List<String> RESULT_DISPLAY_ERROR_STYLE =
            Arrays.asList("text-input", "text-area", "result-display", ResultDisplay.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(
                                this::getInitialDataListEntryBook,
                                this::getInitialDataArchivesEntryBook,
                                this::getInitialDataFeedsEntryBook,
                                getDataFileLocationListEntryBook(),
                                getDataFileLocationArchivesEntryBook(),
                                getDataFileLocationFeedsEntryBook());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        waitUntilBrowserLoaded(getBrowserPanel());
        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() {
        setupHelper.tearDownStage();
    }

    /**
     * Returns the data for the list entry book to be loaded into the file in
     * {@link #getDataFileLocationListEntryBook()}.
     */
    protected EntryBook getInitialDataListEntryBook() {
        return TypicalEntries.getTypicalListEntryBook();
    }

    /**
     * Returns the data for the archives entry book to be loaded into the file in
     * {@link #getDataFileLocationArchivesEntryBook()}.
     */
    protected EntryBook getInitialDataArchivesEntryBook() {
        return TypicalEntries.getTypicalArchivesEntryBook();
    }

    /**
     * Returns the data for the feeds entry book to be loaded into the file in
     * {@link #getDataFileLocationFeedsEntryBook()}.
     */
    protected EntryBook getInitialDataFeedsEntryBook() {
        return TypicalEntries.getTypicalFeedsEntryBook();
    }

    /**
     * Returns the directory of the data file for the list entry book.
     */
    protected Path getDataFileLocationListEntryBook() {
        return TestApp.SAVE_LOCATION_LIST_ENTRYBOOK_FOR_TESTING;
    }

    /**
     * Returns the directory of the data file for the archives entry book.
     */
    protected Path getDataFileLocationArchivesEntryBook() {
        return TestApp.SAVE_LOCATION_ARCHIVES_ENTRYBOOK_FOR_TESTING;
    }

    /**
     * Returns the directory of the data file for the feeds entry book.
     */
    protected Path getDataFileLocationFeedsEntryBook() {
        return TestApp.SAVE_LOCATION_FEEDS_ENTRYBOOK_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public EntryListPanelHandle getEntryListPanel() {
        return mainWindowHandle.getEntryListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public BrowserPanelHandle getBrowserPanel() {
        return mainWindowHandle.getBrowserPanel();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    public void deleteArticle(URL url) throws IOException {
        testApp.deleteArticle(url);
    }

    public Optional<URL> getOfflineLink(URL url) {
        return testApp.getOfflineLink(url);
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

        waitUntilBrowserLoaded(getBrowserPanel());
    }

    /**
     * Displays all entries in the list entry book.
     */
    protected void showAllListEntries() {
        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getListEntryBook().getEntryList().size(), getModel().getFilteredEntryList().size());
    }

    /**
     * Displays all entries in the archives entry book.
     */
    protected void showAllArchivesEntries() {
        executeCommand(ArchivesCommand.COMMAND_WORD);
        assertEquals(getModel().getArchivesEntryBook().getEntryList().size(), getModel().getFilteredEntryList().size());
    }

    /**
     * Displays all entries with any parts of their titles matching {@code keyword} (case-insensitive).
     */
    protected void showEntriesWithTitle(String keyword) {
        executeCommand(FindCommand.COMMAND_WORD + " " + PREFIX_TITLE + keyword);
    }

    /**
     * Selects the entry at {@code index} of the displayed list.
     */
    protected void selectEntry(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getEntryListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all entries in the address book.
     */
    protected void deleteAllEntries() {
        executeCommand(ClearListCommand.COMMAND_WORD);
        assertEquals(0, getModel().getListEntryBook().getEntryList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the storage contains the same entry objects as {@code expectedModel}
     * and the entry list panel displays the entries in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
            Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(new EntryBook(expectedModel.getListEntryBook()), testApp.readStorageListEntryBook());
        assertEquals(new EntryBook(expectedModel.getArchivesEntryBook()), testApp.readStorageArchivesEntryBook());
        assertEquals(new EntryBook(expectedModel.getFeedsEntryBook()), testApp.readStorageFeedsEntryBook());
        assertEquals(expectedModel.getContext(), testApp.getModel().getContext());
        assertListMatching(getEntryListPanel(), expectedModel.getFilteredEntryList());
    }

    /**
     * Calls {@code BrowserPanelHandle}, {@code EntryListPanelHandle} and {@code StatusBarFooterHandle} to remember
     * their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        getBrowserPanel().rememberUrl();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getEntryListPanel().rememberSelectedEntryCard();
    }

    /**
     * Asserts that the previously selected card is now deselected and the browser's url is now displaying the
     * default page.
     * @see BrowserPanelHandle#isUrlChanged()
     */
    protected void assertSelectedCardDeselected() {
        assertEquals(BrowserPanel.DEFAULT_PAGE, getBrowserPanel().getLoadedUrl());
        assertFalse(getEntryListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's url is changed to display the details of the entry in the entry list panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see EntryListPanelHandle#isSelectedEntryCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        getEntryListPanel().navigateToCard(getEntryListPanel().getSelectedCardIndex());
        String selectedCardLink = getEntryListPanel().getHandleToSelectedCard().getLink();
        URL expectedUrl;
        try {
            expectedUrl = new URL(selectedCardLink);
        } catch (MalformedURLException mue) {
            throw new AssertionError("URL expected to be valid.", mue);
        }
        // assertEquals(expectedUrl, getBrowserPanel().getLoadedUrl());
        // TODO: make tests work consistently independent of Internet access and installation directory

        assertEquals(expectedSelectedCardIndex.getZeroBased(), getEntryListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the browser's url and the selected card in the entry list panel remain unchanged.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see EntryListPanelHandle#isSelectedEntryCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getBrowserPanel().isUrlChanged());
        assertFalse(getEntryListPanel().isSelectedEntryCardChanged());
    }

    /**
     * Asserts that the command box shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the result display shows the default style.
     */
    protected void assertResultDisplayShowsDefaultStyle() {
        assertEquals(RESULT_DISPLAY_DEFAULT_STYLE, getResultDisplay().getStyleClass());
    }

    /**
     * Asserts that the result display shows the error style.
     */
    protected void assertResultDisplayShowsErrorStyle() {
        assertEquals(RESULT_DISPLAY_ERROR_STYLE, getResultDisplay().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarExcludingCountUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatusExcludingCount() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        assertEquals("", getCommandBox().getInput());
        assertEquals("", getResultDisplay().getText());
        assertListMatching(getEntryListPanel(), getModel().getFilteredEntryList());
        assertEquals(BrowserPanel.DEFAULT_PAGE, getBrowserPanel().getLoadedUrl());
        assertEquals(Paths.get(".").resolve(testApp.getListEntryBookStorageSaveLocation()).toString(),
                getStatusBarFooter().getSaveLocation());
        assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }

    /**
     * Sets the command result in the app.
     * @param commandResult command result to set
     */
    protected void setCommandResultInApp(CommandResult commandResult) {
        testApp.setCommandResult(commandResult);
    }

    /**
     * Sets the exception in the app.
     * @param e exception to set
     */
    protected void setExceptionInApp(Exception e) {
        testApp.setException(e);
    }

}
