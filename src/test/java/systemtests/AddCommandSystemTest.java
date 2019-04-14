package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LINK;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LINK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TECH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.AMY;
import static seedu.address.testutil.TypicalEntries.BOB;
import static seedu.address.testutil.TypicalEntries.CARL;
import static seedu.address.testutil.TypicalEntries.HOON;
import static seedu.address.testutil.TypicalEntries.IDA;
import static seedu.address.testutil.TypicalEntries.KEYWORD_MATCHING_MEIER;

import java.util.Optional;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.exceptions.DuplicateEntryCommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryBuilder;
import seedu.address.testutil.EntryUtil;

public class AddCommandSystemTest extends EntryBookSystemTest {

    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a entry without tags to a non-empty entry book, command with leading spaces and trailing spaces
         * -> added
         */
        Entry toAdd = AMY;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + TITLE_DESC_AMY + "  " + DESCRIPTION_DESC_AMY + " "
                + LINK_DESC_AMY + "   " + TAG_DESC_TECH + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: add a entry with all fields same as another entry in the entry book except link -> added */
        toAdd = new EntryBuilder(AMY).withLink(VALID_LINK_BOB).build();
        command = AddCommand.COMMAND_WORD + TITLE_DESC_AMY + DESCRIPTION_DESC_AMY + LINK_DESC_BOB
                + TAG_DESC_TECH;
        assertCommandSuccess(command, toAdd);

        // Deprecated
        /* Case: add a entry with all fields same as another entry in the entry book except description and link
         * -> added
         */
        /*
        toAdd = new EntryBuilder(AMY).withDescription(VALID_DESCRIPTION_BOB).withLink(VALID_LINK_BOB).build();
        command = EntryUtil.getAddCommand(toAdd);
        assertCommandSuccess(command, toAdd);
        */

        /* Case: add to empty entry book -> added */
        deleteAllEntries();
        assertCommandSuccess(ALICE);

        /* Case: add a entry with tags, command with parameters in random order -> added */
        toAdd = BOB;
        command = AddCommand.COMMAND_WORD + TAG_DESC_TECH + DESCRIPTION_DESC_BOB + TITLE_DESC_BOB
                + TAG_DESC_SCIENCE + LINK_DESC_BOB;
        assertCommandSuccess(command, toAdd);

        /* Case: add a entry, missing tags -> added */
        assertCommandSuccess(HOON);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the entry list before adding -> added */
        showEntriesWithTitle(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(IDA);

        /* ------------------------ Perform add operation while a entry card is selected --------------------------- */

        /* Case: selects first card in the entry list, add a entry -> added, card selection remains unchanged */
        selectEntry(Index.fromOneBased(1));
        assertCommandSuccess(CARL);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate entry -> rejected */
        command = EntryUtil.getAddCommand(HOON);
        assertCommandFailure(command, DuplicateEntryCommandException.MESSAGE_DUPLICATE_ENTRY);

        /* Case: add a duplicate entry except with different description -> rejected */
        toAdd = new EntryBuilder(HOON).withDescription(VALID_DESCRIPTION_BOB).build();
        command = EntryUtil.getAddCommand(toAdd);
        assertCommandFailure(command, DuplicateEntryCommandException.MESSAGE_DUPLICATE_ENTRY);

        /* Case: add a duplicate entry except with different title -> rejected */
        toAdd = new EntryBuilder(HOON).withTitle(VALID_TITLE_BOB).build();
        command = EntryUtil.getAddCommand(toAdd);
        assertCommandFailure(command, DuplicateEntryCommandException.MESSAGE_DUPLICATE_ENTRY);

        /* Case: add a duplicate entry except with different tags -> rejected */
        command = EntryUtil.getAddCommand(HOON) + " " + PREFIX_TAG.getPrefix() + "friends";
        assertCommandFailure(command, DuplicateEntryCommandException.MESSAGE_DUPLICATE_ENTRY);

        /* Case: missing link -> rejected */
        command = AddCommand.COMMAND_WORD + TITLE_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + EntryUtil.getEntryDetails(toAdd);
        assertCommandFailure(command, String.format(Messages.MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST));

        /* Case: invalid title -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_TITLE_DESC
            + DESCRIPTION_DESC_AMY + LINK_DESC_AMY;
        assertCommandFailure(command, Title.formExceptionMessage(INVALID_TITLE.trim()));

        /* Case: invalid description -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_DESCRIPTION_DESC
            + TITLE_DESC_AMY + LINK_DESC_AMY;
        assertCommandFailure(command, Description.formExceptionMessage(INVALID_DESCRIPTION.trim()));

        /* Case: invalid link -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_LINK_DESC
             + TITLE_DESC_AMY + DESCRIPTION_DESC_AMY;
        assertCommandFailure(command, Link.formExceptionMessage(INVALID_LINK.trim()));

        /* Case: invalid tag -> rejected */
        command = AddCommand.COMMAND_WORD + TITLE_DESC_AMY + DESCRIPTION_DESC_AMY + LINK_DESC_AMY
                + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.formExceptionMessage(INVALID_TAG.trim()));

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        deleteAllEntries();

        /* Case: using alias to add a entry without tags to a non-empty entry book -> added */
        command = "   " + AddCommand.COMMAND_ALIAS + "  " + TITLE_DESC_AMY + "  " + DESCRIPTION_DESC_AMY + " "
            + LINK_DESC_AMY + "   " + TAG_DESC_TECH + " ";
        assertCommandSuccess(command, AMY);

        /* Case: command with leading spaces and trailing spaces -> added */
        command = "   " + AddCommand.COMMAND_WORD + "  " + TITLE_DESC_BOB + "  " + DESCRIPTION_DESC_BOB + " "
            + LINK_DESC_BOB + "   " + TAG_DESC_TECH + "   " + TAG_DESC_SCIENCE + " ";
        assertCommandSuccess(command, BOB);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code EntryListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see EntryBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Entry toAdd) {
        assertCommandSuccess(EntryUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Entry)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Entry)
     */
    private void assertCommandSuccess(String command, Entry toAdd) {
        Model expectedModel = getModel();
        expectedModel.addListEntry(toAdd, Optional.empty());
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Entry)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code EntryListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Entry)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatusExcludingCount();
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
