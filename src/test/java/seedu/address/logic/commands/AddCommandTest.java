package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.entry.Entry;
import seedu.address.storage.Storage;
import seedu.address.testutil.EntryBuilder;
import seedu.address.ui.ViewMode;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Entry validEntry = new EntryBuilder().build();

        CommandResult commandResult = new AddCommand(validEntry).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEntry), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validEntry), modelStub.personsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        Entry validEntry = new EntryBuilder().build();
        AddCommand addCommand = new AddCommand(validEntry);
        ModelStub modelStub = new ModelStubWithPerson(validEntry);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);
        addCommand.execute(modelStub, commandHistory);
    }

    // TODO: Add tests for with/without Title/Description
    // TODO: Add tests for Crux, should return non-null Title/Description

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
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getEntryBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEntryBookFilePath(Path entryBookFilePath) {
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
        public void addEntry(Entry entry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEntryBook(ReadOnlyEntryBook entryBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyEntryBook getEntryBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEntry(Entry entry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteEntry(Entry target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEntry(Entry target, Entry editedEntry) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void clearEntryBook() {
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
    }

    /**
     * A Model stub that contains a single entry.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Entry entry;

        ModelStubWithPerson(Entry entry) {
            requireNonNull(entry);
            this.entry = entry;
        }

        @Override
        public boolean hasEntry(Entry entry) {
            requireNonNull(entry);
            return this.entry.isSameEntry(entry);
        }
    }

    /**
     * A Model stub that always accept the entry being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Entry> personsAdded = new ArrayList<>();

        @Override
        public boolean hasEntry(Entry entry) {
            requireNonNull(entry);
            return personsAdded.stream().anyMatch(entry::isSameEntry);
        }

        @Override
        public void addEntry(Entry entry) {
            requireNonNull(entry);
            personsAdded.add(entry);
        }

        @Override
        public ReadOnlyEntryBook getEntryBook() {
            return new EntryBook();
        }
    }

}
