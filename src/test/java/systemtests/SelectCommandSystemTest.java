package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.SelectCommand.MESSAGE_SELECT_ENTRY_SUCCESS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TypicalEntries.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.testutil.TypicalEntries;

public class SelectCommandSystemTest extends EntryBookSystemTest {

    @Test
    public void select() {
        /* ------------------------ Perform select operations on the shown unfiltered list -------------------------- */

        /* Case: select the first card in the entry list, command with leading spaces and trailing spaces
         * -> selected
         */
        String command = "   " + SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased() + "   ";
        assertCommandSuccess(command, INDEX_FIRST_ENTRY);

        /* Case: select the last card in the entry list -> selected */
        Index entryCount = getLastIndex(getModel());
        command = SelectCommand.COMMAND_WORD + " " + entryCount.getOneBased();
        assertCommandSuccess(command, entryCount);

        /* Case: select the middle card in the entry list -> selected */
        Index middleIndex = getMidIndex(getModel());
        command = SelectCommand.COMMAND_WORD + " " + middleIndex.getOneBased();
        assertCommandSuccess(command, middleIndex);

        /* Case: select the current selected card -> selected */
        assertCommandSuccess(command, middleIndex);

        /* ------------------------ Perform select operations on the shown filtered list ---------------------------- */

        /* Case: filtered entry list, select index within bounds of address book but out of bounds of entry list
         * -> rejected
         */
        showEntriesWithTitle(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getListEntryBook().getEntryList().size();
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + invalidIndex, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);

        /* Case: filtered entry list, select index within bounds of address book and entry list -> selected */
        Index validIndex = Index.fromOneBased(1);
        assertTrue(validIndex.getZeroBased() < getModel().getFilteredEntryList().size());
        command = SelectCommand.COMMAND_WORD + " " + validIndex.getOneBased();
        assertCommandSuccess(command, validIndex);

        /* ----------------------------------- Perform invalid select operations ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + 0,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + -1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredEntryList().size() + 1;
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + invalidIndex, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " 1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeLeCt 1", String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST));

        /* Case: select from empty address book -> rejected */
        deleteAllEntries();
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased(),
                MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void select_downloadsUndownloaded() throws IOException {

        /* Case: Non-downloaded entry is downloaded after selection */
        // First add the entry, then we delete the article
        URL wikiurl = TypicalEntries.WIKIPEDIA_ENTRY.getLink().value;
        String command = AddCommand.COMMAND_WORD + " l/" + wikiurl.toString();
        executeCommand(command);
        deleteArticle(wikiurl);

        // Now we check that it's not downloaded
        assertFalse(getOfflineLink(wikiurl).isPresent());

        // Select
        Index validIndex = getLastIndex(getModel());
        command = SelectCommand.COMMAND_WORD + " " + validIndex.getOneBased();
        assertCommandSuccess(command, validIndex);

        // Check that it's downloaded
        assertTrue(getOfflineLink(wikiurl).isPresent());

        /* Case: Non-downloaded entry is invalid, so it's still non-downloaded after selection */
        // The 8th entry is the wikipedia entry
        URL aliceurl = TypicalEntries.ALICE.getLink().value;
        validIndex = Index.fromOneBased(1);
        command = SelectCommand.COMMAND_WORD + " " + validIndex.getOneBased();

        // Check that it's not downloaded, select, then check that it's still not downloaded
        assertFalse(getOfflineLink(aliceurl).isPresent());
        assertCommandSuccess(command, validIndex);
        assertFalse(getOfflineLink(aliceurl).isPresent());

        /* Case: Non-downloaded entry in non-list context is NOT downloaded after selection */
        // First add the entry, then we delete the article
        command = AddCommand.COMMAND_WORD + " l/" + wikiurl.toString();
        executeCommand(command);
        command = ArchiveCommand.COMMAND_WORD + " " + getLastIndex(getModel()).getOneBased();
        executeCommand(command);
        command = ArchivesCommand.COMMAND_WORD;
        executeCommand(command);

        // Now we check that it's not downloaded
        assertFalse(getOfflineLink(wikiurl).isPresent());

        // Select
        validIndex = getLastIndex(getModel());
        command = SelectCommand.COMMAND_WORD + " " + validIndex.getOneBased();
        assertCommandSuccess(command, validIndex);

        // Check that it's NOT downloaded
        assertFalse(getOfflineLink(wikiurl).isPresent());

    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected entry.<br>
     * 4. {@code Storage} and {@code EntryListPanel} remain unchanged.<br>
     * 5. Selected card is at {@code expectedSelectedCardIndex} and the browser url is updated accordingly.<br>
     * 6. Status bar excluding count remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see EntryBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(
                MESSAGE_SELECT_ENTRY_SUCCESS, expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getEntryListPanel().getSelectedCardIndex();

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (preExecutionSelectedCardIndex == expectedSelectedCardIndex.getZeroBased()) {
            assertSelectedCardUnchanged();
        } else {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        }

        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        assertStatusBarExcludingCountUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code EntryListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar excluding count remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
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
