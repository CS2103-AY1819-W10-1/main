package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.COMMENT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.COMMENT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_COMMENT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LINK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TECH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COMMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalEntries.AMY;
import static seedu.address.testutil.TypicalEntries.BOB;
import static seedu.address.testutil.TypicalEntries.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryBuilder;
import seedu.address.testutil.EntryUtil;

public class EditCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void edit() {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_PERSON;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + TITLE_DESC_BOB + "  "
                + COMMENT_DESC_BOB + " " + LINK_DESC_BOB + "  " + ADDRESS_DESC_BOB + " " + TAG_DESC_SCIENCE + " ";
        Entry editedEntry = new EntryBuilder(BOB).withTags(VALID_TAG_SCIENCE).build();
        assertCommandSuccess(command, index, editedEntry);

        /* Case: undo editing the last entry in the list -> last entry restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last entry in the list -> last entry edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.setPerson(getModel().getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), editedEntry);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a entry with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        assertCommandSuccess(command, index, BOB);

        /* Case: edit a entry with new values same as another entry's values but with different name -> edited */
        assertTrue(getModel().getAddressBook().getPersonList().contains(BOB));
        index = INDEX_SECOND_PERSON;
        assertNotEquals(getModel().getFilteredPersonList().get(index.getZeroBased()), BOB);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_AMY + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        editedEntry = new EntryBuilder(BOB).withName(VALID_TITLE_AMY).build();
        assertCommandSuccess(command, index, editedEntry);

        /* Case: edit a entry with new values same as another entry's values but with different phone and email
         * -> edited
         */
        index = INDEX_SECOND_PERSON;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + COMMENT_DESC_AMY + LINK_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        editedEntry = new EntryBuilder(BOB).withPhone(VALID_COMMENT_AMY).withEmail(VALID_LINK_AMY).build();
        assertCommandSuccess(command, index, editedEntry);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_PERSON;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Entry entryToEdit = getModel().getFilteredPersonList().get(index.getZeroBased());
        editedEntry = new EntryBuilder(entryToEdit).withTags().build();
        assertCommandSuccess(command, index, editedEntry);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered entry list, edit index within bounds of address book and entry list -> edited */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_PERSON;
        assertTrue(index.getZeroBased() < getModel().getFilteredPersonList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + TITLE_DESC_BOB;
        entryToEdit = getModel().getFilteredPersonList().get(index.getZeroBased());
        editedEntry = new EntryBuilder(entryToEdit).withName(VALID_TITLE_BOB).build();
        assertCommandSuccess(command, index, editedEntry);

        /* Case: filtered entry list, edit index within bounds of address book but out of bounds of entry list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getAddressBook().getPersonList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + TITLE_DESC_BOB,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a entry card is selected -------------------------- */

        /* Case: selects first card in the entry list, edit a entry -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllPersons();
        index = INDEX_FIRST_PERSON;
        selectPerson(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_AMY + COMMENT_DESC_AMY + LINK_DESC_AMY
                + ADDRESS_DESC_AMY + TAG_DESC_TECH;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new entry's name
        assertCommandSuccess(command, index, AMY, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + TITLE_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + TITLE_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredPersonList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + TITLE_DESC_BOB,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + TITLE_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + INVALID_TITLE_DESC,
                Title.MESSAGE_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + INVALID_COMMENT_DESC,
                Comment.MESSAGE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + INVALID_LINK_DESC,
                Link.MESSAGE_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + INVALID_ADDRESS_DESC,
                Address.MESSAGE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + INVALID_TAG_DESC,
                Tag.MESSAGE_CONSTRAINTS);

        /* Case: edit a entry with new values same as another entry's values -> rejected */
        executeCommand(EntryUtil.getAddCommand(BOB));
        assertTrue(getModel().getAddressBook().getPersonList().contains(BOB));
        index = INDEX_FIRST_PERSON;
        assertFalse(getModel().getFilteredPersonList().get(index.getZeroBased()).equals(BOB));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: edit a entry with new values same as another entry's values but with different tags -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_SCIENCE;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: edit a entry with new values same as another entry's values but with different address -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_AMY + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: edit a entry with new values same as another entry's values but with different phone -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + COMMENT_DESC_AMY + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PERSON);

        /* Case: edit a entry with new values same as another entry's values but with different email -> rejected */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_TECH + TAG_DESC_SCIENCE;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Entry, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Entry, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Entry editedEntry) {
        assertCommandSuccess(command, toEdit, editedEntry, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the entry at index {@code toEdit} being
     * updated to values specified {@code editedEntry}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Entry editedEntry,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.setPerson(expectedModel.getFilteredPersonList().get(toEdit.getZeroBased()), editedEntry);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedEntry), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertResultDisplayShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
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
        assertStatusBarUnchanged();
    }
}
