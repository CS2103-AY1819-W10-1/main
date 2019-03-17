package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UnarchiveCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.entry.Entry;

public class ContextSwitchSystemTest extends AddressBookSystemTest {

    @Test
    public void contextSwitch() {

        /* Case: archive the first entry in the list */
        Model expectedModel = getModel();
        String command = ArchiveCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased();
        Entry archivedEntry = archiveEntry(expectedModel, INDEX_FIRST_ENTRY);
        String expectedResultMessage = String.format(ArchiveCommand.MESSAGE_ARCHIVE_ENTRY_SUCCESS, archivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: non-list-context command unarchive fails */
        command = UnarchiveCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased();
        expectedResultMessage = String.format(MESSAGE_UNKNOWN_COMMAND);
        assertCommandFailure(command, expectedResultMessage);

        /* Case: view archives, model should not change */
        command = ArchivesCommand.COMMAND_WORD;
        assertArchivesCommandSuccess(command);

        /* Case: unarchive the first entry in the list */
        expectedModel = getModel();
        command = UnarchiveCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased();
        Entry unarchivedEntry = unarchiveEntry(expectedModel, INDEX_FIRST_ENTRY);
        expectedResultMessage = String.format(UnarchiveCommand.MESSAGE_UNARCHIVE_ENTRY_SUCCESS, unarchivedEntry);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: non-archives-context command archive fails */
        command = ArchiveCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased();
        expectedResultMessage = String.format(MESSAGE_UNKNOWN_COMMAND);
        assertCommandFailure(command, expectedResultMessage);

        /* Case: view entry book, model should not change */
        command = ListCommand.COMMAND_WORD;
        assertListCommandSuccess(command);
    }

    /**
     * Archives the {@code Entry} at the specified {@code index} in {@code model}'s entry book.
     * @return the archived entry
     */
    private Entry archiveEntry(Model model, Index index) {
        Entry targetEntry = getPerson(model, index);
        model.archiveEntry(targetEntry);
        return targetEntry;
    }

    /**
     * Un-archives the {@code Entry} at the specified {@code index} in {@code model}'s entry book.
     * @return the un-archived entry
     */
    private Entry unarchiveEntry(Model model, Index index) {
        Entry targetEntry = getPerson(model, index);
        model.unarchiveEntry(targetEntry);
        return targetEntry;
    }

    /**
     * Executes the given command.
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code Command}. <br>
     * 4. {@code Storage} and {@code EntryListPanel} equal to the corresponding components in
     * the current model.
     * 5. Browser url and selected card deselected.<br>
     * 6. Status bar's sync status excluding count changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardDeselected();
        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        assertStatusBarExcludingCountUnchanged();
    }

    /**
     * Asserts that a list command successfully switches context of the Model.
     * @see ContextSwitchSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertListCommandSuccess(String command) {
        Model expectedModel = getModel();
        expectedModel.setContext(ModelContext.CONTEXT_LIST);
        String expectedResultMessage = String.format(ListCommand.MESSAGE_SUCCESS);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Asserts that a archives command successfully switches context of the Model.
     * @see ContextSwitchSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertArchivesCommandSuccess(String command) {
        Model expectedModel = getModel();
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVE);
        String expectedResultMessage = String.format(ArchivesCommand.MESSAGE_SUCCESS);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code EntryListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertResultDisplayShowsErrorStyle();
        assertStatusBarExcludingCountUnchanged();
    }

}
