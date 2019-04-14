package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Test;

import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.ClearArchivesCommand;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

public class ClearArchivesCommandSystemTest extends EntryBookSystemTest {

    @Test
    public void clear() {
        final Model defaultModel = getModel();

        // Switch to archives context
        executeCommand(ArchivesCommand.COMMAND_WORD);

        /* Case: clear non-empty archives entry book,
         * command with leading spaces and trailing alphanumeric characters and
         * spaces -> cleared
         */
        assertCommandSuccess("   " + ClearArchivesCommand.COMMAND_WORD + " ab12   ");
        assertSelectedCardUnchanged();

        /* Case: clear empty address book -> cleared */
        assertCommandSuccess(ClearArchivesCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ClEaR", String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_ARCHIVES));
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ClearArchivesCommand#MESSAGE_SUCCESS}
     * and the model related components equal to an empty model.
     * These verifications are done by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class and the status bar's sync status changes.
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command) {
        Model expectedModel = new ModelManagerStub();
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVES);
        expectedModel.setListEntryBook(getModel().getListEntryBook());
        expectedModel.setFeedsEntryBook(getModel().getFeedsEntryBook());
        assertCommandSuccess(command, ClearArchivesCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     * @see ClearArchivesCommandSystemTest#assertCommandSuccess(String)
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, Model expectedModel) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        assertStatusBarExcludingCountUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar excluding count remain unchanged,
     * and the command box has the error style.
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        expectedModel.setContext(ModelContext.CONTEXT_ARCHIVES);

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertResultDisplayShowsErrorStyle();
        assertStatusBarExcludingCountUnchanged();
    }
}
