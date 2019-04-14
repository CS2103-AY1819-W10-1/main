package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_DESCRIPTION_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_NO_DESCRIPTION_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_NO_TITLE_COMPLETE;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_NO_TITLE_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.REAL_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_NO_DESCRIPTION_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_NO_TITLE_COMPLETE;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_NO_TITLE_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.STUB_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.DuplicateEntryCommandException;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.DuplicateEntryException;
import seedu.address.storage.Storage;
import seedu.address.testutil.EntryBuilder;
import seedu.address.ui.ViewMode;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullEntry_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_entryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingEntryAdded modelStub = new ModelStubAcceptingEntryAdded();
        Entry validEntry = new EntryBuilder().build();

        CommandResult commandResult = new AddCommand(validEntry).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEntry), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validEntry), modelStub.entriesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateEntry_throwsCommandException() throws Exception {
        Entry validEntry = new EntryBuilder().build();
        AddCommand addCommand = new AddCommand(validEntry);
        ModelStub modelStub = new ModelStubWithEntry(validEntry);

        thrown.expect(DuplicateEntryCommandException.class);
        thrown.expectMessage(DuplicateEntryCommandException.MESSAGE_DUPLICATE_ENTRY);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_stubEntryHasNoTitleAndNoDescription_titleAndDescriptionReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(
                STUB_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE, STUB_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE);
    }

    @Test
    public void execute_stubEntryHasNoTitle_onlyTitleReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(STUB_LINK_NO_TITLE_INCOMPLETE, STUB_LINK_NO_TITLE_COMPLETE);
    }

    @Test
    public void execute_stubEntryHasNoDescription_onlyDescriptionReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(STUB_LINK_NO_DESCRIPTION_INCOMPLETE, STUB_LINK_NO_DESCRIPTION_COMPLETE);
    }

    @Test
    public void execute_realEntryHasNoTitleAndNoDescription_titleAndDescriptionReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(
                REAL_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE, REAL_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE);
    }

    @Test
    public void execute_realEntryHasNoTitle_onlytitleReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(REAL_LINK_NO_TITLE_INCOMPLETE, REAL_LINK_NO_TITLE_COMPLETE);
    }

    @Test
    public void execute_realEntryHasNoDescription_onlydescriptionReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(REAL_LINK_NO_DESCRIPTION_INCOMPLETE, REAL_LINK_NO_DESCRIPTION_COMPLETE);
    }

    @Test
    public void execute_cruxEntryHasNoTitleAndNoDescription_titleAndDescriptionReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(
                READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE, READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE);
    }

    @Test
    public void execute_cruxEntryHasNoTitle_onlyTitleReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(READABILITY_LINK_NO_TITLE_INCOMPLETE, READABILITY_LINK_NO_TITLE_COMPLETE);
    }

    @Test
    public void execute_cruxEntryHasNoDescription_onlyDescriptionReplaced() throws Exception {
        assertInitialEntryBecomesFinalEntry(READABILITY_LINK_NO_DESCRIPTION_INCOMPLETE,
                READABILITY_LINK_NO_DESCRIPTION_COMPLETE);
    }

    @Test
    public void execute_entryHasTitleAndDescription_noChange() throws Exception {
        assertInitialEntryBecomesFinalEntry(READABILITY_LINK_COMPLETE, READABILITY_LINK_COMPLETE);
    }

    @Test
    public void equals() {
        Entry alice = new EntryBuilder().withTitle("Alice").build();
        Entry bob = new EntryBuilder().withTitle("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different entry -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Asserts that {@code initialEntry} in an {@code AddCommand} becomes {@code finalEntry} after its execution.
     */
    private void assertInitialEntryBecomesFinalEntry(Entry initialEntry, Entry finalEntry) throws Exception {
        ModelStubAcceptingEntryAdded modelStub = new ModelStubAcceptingEntryAdded();

        CommandResult commandResult = new AddCommand(initialEntry).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, finalEntry), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(finalEntry), modelStub.entriesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Storage getStorage() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteArticle(URL url) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Path> addArticle(URL url, byte[] articleContent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<String> getArticle(URL url) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getListEntryBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void setListEntryBookFilePath(Path listEntryBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getArchivesEntryBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setArchivesEntryBookFilePath(Path archivesEntryBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getArticleDataDirectoryPath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setArticleDataDirectoryPath(Path articleDataDirectoryPath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasOfflineCopy(URL url) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<URL> getOfflineLink(URL url) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEntry(Entry entry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addListEntry(Entry entry, Optional<byte[]> articleContent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setListEntryBook(ReadOnlyEntryBook listEntryBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasListEntry(Entry listEntry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyEntryBook getListEntryBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteListEntry(Entry target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setListEntry(Entry target, Entry editedEntry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void clearListEntryBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void refreshEntry(Entry entry, byte[] articleContent) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addArchivesEntry(Entry entry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setArchivesEntryBook(ReadOnlyEntryBook archivesEntryBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyEntryBook getArchivesEntryBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasArchivesEntry(Entry archiveEntry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteArchivesEntry(Entry target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void clearArchivesEntryBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setFeedsEntryBook(ReadOnlyEntryBook feedsEntryBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyEntryBook getFeedsEntryBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasFeedsEntry(Entry feed) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteFeedsEntry(Entry target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addFeedsEntry(Entry feed) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void clearFeedsEntryBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSearchEntryBook(ReadOnlyEntryBook searchEntryBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Entry> getFilteredEntryList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredEntryList(Predicate<Entry> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Entry> selectedEntryProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Entry getSelectedEntry() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedEntry(Entry entry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<ViewMode> viewModeProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ViewMode getViewMode() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setViewMode(ViewMode viewMode) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Exception> exceptionProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Exception getException() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setException(Exception exceptionToBePropagated) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<CommandResult> commandResultProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public CommandResult getCommandResult() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCommandResult(CommandResult result) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Model clone() {
            return this;
        }

        @Override
        public ReadOnlyProperty<ModelContext> contextProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ModelContext getContext() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setContext(ModelContext context) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void archiveEntry(Entry target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void unarchiveEntry(Entry entry, Optional<byte[]> articleContent) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single entry.
     */
    private class ModelStubWithEntry extends ModelStub {
        private final Entry entry;

        ModelStubWithEntry(Entry entry) {
            requireNonNull(entry);
            this.entry = entry;
        }

        @Override
        public boolean hasEntry(Entry listEntry) {
            requireNonNull(listEntry);
            return this.entry.isSameEntry(listEntry);
        }

        @Override
        public void addListEntry(Entry listEntry, Optional<byte[]> articleContents) {
            if (listEntry.equals(entry)) {
                throw new DuplicateEntryException();
            }
        }
    }

    /**
     * A Model stub that always accepts the entry being added.
     */
    private class ModelStubAcceptingEntryAdded extends ModelStub {
        private final ArrayList<Entry> entriesAdded = new ArrayList<>();

        @Override
        public Optional<Path> addArticle(URL url, byte[] articleContent) {
            return Optional.empty();
        }

        @Override
        public boolean hasEntry(Entry listEntry) {
            requireNonNull(listEntry);
            return entriesAdded.stream().anyMatch(listEntry::isSameEntry);
        }

        @Override
        public void addListEntry(Entry entry, Optional<byte[]> articleContent) {
            requireNonNull(entry);
            entriesAdded.add(entry);
        }

        @Override
        public ReadOnlyEntryBook getListEntryBook() {
            return new EntryBook();
        }
    }

}
