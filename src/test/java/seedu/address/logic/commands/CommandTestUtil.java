package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.TitleContainsKeywordsPredicate;
import seedu.address.testutil.EditEntryDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_TITLE_AMY = "Amy Bee bankrupt?";
    public static final String VALID_TITLE_BOB = "Bob Choo's affair!";
    public static final String VALID_DESCRIPTION_AMY = "Bad investments??";
    public static final String VALID_DESCRIPTION_BOB = "The answer will shock you!";
    public static final String VALID_LINK_AMY = "https://amy.example.com";
    public static final String VALID_LINK_BOB = "file:///bob/example/file";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_SCIENCE = "science";
    public static final String VALID_TAG_TECH = "tech";

    public static final String TITLE_DESC_AMY = " " + PREFIX_TITLE + VALID_TITLE_AMY;
    public static final String TITLE_DESC_BOB = " " + PREFIX_TITLE + VALID_TITLE_BOB;
    public static final String DESCRIPTION_DESC_AMY = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_AMY;
    public static final String DESCRIPTION_DESC_BOB = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_BOB;
    public static final String LINK_DESC_AMY = " " + PREFIX_LINK + VALID_LINK_AMY;
    public static final String LINK_DESC_BOB = " " + PREFIX_LINK + VALID_LINK_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_TECH = " " + PREFIX_TAG + VALID_TAG_TECH;
    public static final String TAG_DESC_SCIENCE = " " + PREFIX_TAG + VALID_TAG_SCIENCE;

    public static final String INVALID_TITLE_DESC = " " + PREFIX_TITLE + " "; // string starting with space not allowed
    public static final String INVALID_DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION; // empty string not allowed
    public static final String INVALID_LINK_DESC = " " + PREFIX_LINK + "bob.yahoo.com"; // missing protocol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "tech*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditEntryDescriptor DESC_AMY;
    public static final EditCommand.EditEntryDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditEntryDescriptorBuilder().withTitle(VALID_TITLE_AMY)
                .withDescription(VALID_DESCRIPTION_AMY).withLink(VALID_LINK_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_TECH).build();
        DESC_BOB = new EditEntryDescriptorBuilder().withTitle(VALID_TITLE_BOB)
                .withDescription(VALID_DESCRIPTION_BOB).withLink(VALID_LINK_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_SCIENCE, VALID_TAG_TECH).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} <br>
     * - the {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            CommandResult expectedCommandResult, Model expectedModel) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage, Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered entry list and selected entry in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        EntryBook expectedAddressBook = new EntryBook(actualModel.getEntryBook());
        List<Entry> expectedFilteredList = new ArrayList<>(actualModel.getFilteredEntryList());
        Entry expectedSelectedEntry = actualModel.getSelectedEntry();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedAddressBook, actualModel.getEntryBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredEntryList());
            assertEquals(expectedSelectedEntry, actualModel.getSelectedEntry());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the entry at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEntryList().size());

        Entry entry = model.getFilteredEntryList().get(targetIndex.getZeroBased());
        final String[] splitName = entry.getTitle().fullTitle.split("\\s+");
        model.updateFilteredEntryList(new TitleContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredEntryList().size());
    }

    /**
     * Deletes the first entry in {@code model}'s filtered list from {@code model}'s address book.
     */
    public static void deleteFirstPerson(Model model) {
        Entry firstEntry = model.getFilteredEntryList().get(0);
        model.deleteEntry(firstEntry);
    }

}
