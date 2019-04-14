package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Test;

import seedu.address.logic.commands.ClearFeedsCommand;
import seedu.address.logic.commands.FeedsCommand;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

public class ClearFeedsCommandSystemTest extends EntryBookSystemTest {

    @Test
    public void clear() {
        final Model defaultModel = getModel();

        // Switch to feeds context
        executeCommand(FeedsCommand.COMMAND_WORD);

        /* Case: clear non-empty feeds entry book, command with leading spaces and trailing alphanumeric characters and
         * spaces -> cleared
         */
        assertCommandSuccess("   " + ClearFeedsCommand.COMMAND_WORD + " ab12   ");
        assertSelectedCardUnchanged();

        /* Case: clear empty feeds entry book -> cleared */
        assertCommandSuccess(ClearFeedsCommand.COMMAND_WORD);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ClEaR", String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_FEEDS));
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ClearFeedsCommand#MESSAGE_SUCCESS}
     * and the model related components equal to an empty model.
     * These verifications are done by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class and the status bar's sync status changes.
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command) {
        Model expectedModel = new ModelManagerStub();
        expectedModel.setContext(ModelContext.CONTEXT_FEEDS);
        expectedModel.setListEntryBook(getModel().getListEntryBook());
        expectedModel.setArchivesEntryBook(getModel().getArchivesEntryBook());
        assertCommandSuccess(command, ClearFeedsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     * @see ClearFeedsCommandSystemTest#assertCommandSuccess(String)
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
        expectedModel.setContext(ModelContext.CONTEXT_FEEDS);

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertResultDisplayShowsErrorStyle();
        assertStatusBarExcludingCountUnchanged();
    }
}
