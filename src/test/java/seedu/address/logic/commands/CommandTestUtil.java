package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STYLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.EntryContainsSearchTermsPredicate;
import seedu.address.testutil.EditEntryDescriptorBuilder;
import seedu.address.testutil.FindEntryDescriptorBuilder;
import seedu.address.testutil.TestUtil;
import seedu.address.ui.ReaderViewStyle;
import seedu.address.ui.ViewType;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_TITLE_AMY = "Amy Bee bankrupt?";
    public static final String VALID_TITLE_BOB = "Bob Choo's affair!";
    public static final String VALID_DESCRIPTION_AMY = "Bad investments??";
    public static final String VALID_DESCRIPTION_BOB = "The answer will shock you!";
    public static final URL VALID_LINK_AMY = TestUtil.toUrl("https://amy.example.com");
    public static final URL VALID_LINK_BOB = TestUtil.toUrl("file:///bob/example/file");
    public static final String VALID_TAG_SCIENCE = "science";
    public static final String VALID_TAG_TECH = "tech";
    public static final String VALID_VIEWTYPE_BROWSER = ViewType.BROWSER.toString();
    public static final String VALID_VIEWTYPE_READER = ViewType.READER.toString();
    public static final String VALID_STYLE_DARK = ReaderViewStyle.DARK.toString();

    public static final String TITLE_DESC_AMY = " " + PREFIX_TITLE + VALID_TITLE_AMY;
    public static final String TITLE_DESC_BOB = " " + PREFIX_TITLE + VALID_TITLE_BOB;
    public static final String DESCRIPTION_DESC_AMY = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_AMY;
    public static final String DESCRIPTION_DESC_BOB = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_BOB;
    public static final String LINK_DESC_AMY = " " + PREFIX_LINK + VALID_LINK_AMY;
    public static final String LINK_DESC_BOB = " " + PREFIX_LINK + VALID_LINK_BOB;
    public static final String TAG_DESC_TECH = " " + PREFIX_TAG + VALID_TAG_TECH;
    public static final String TAG_DESC_SCIENCE = " " + PREFIX_TAG + VALID_TAG_SCIENCE;
    public static final String STYLE_DESC_DARK = " " + PREFIX_STYLE + VALID_STYLE_DARK;

    // string starting with space not allowed
    public static final String INVALID_TITLE = " ";
    public static final String INVALID_TITLE_DESC = " " + PREFIX_TITLE + INVALID_TITLE;
    // string starting with space not allowed
    public static final String INVALID_DESCRIPTION = " ";
    public static final String INVALID_DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION + INVALID_DESCRIPTION;
    // missing protocol
    public static final String INVALID_LINK = "bob.yahoo.com";
    public static final String INVALID_LINK_DESC = " " + PREFIX_LINK + INVALID_LINK;
    // '*' not allowed in tags
    public static final String INVALID_TAG = "tech*";
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + INVALID_TAG;
    // invalid view types
    public static final String INVALID_VIEWTYPE = "nonsense";
    // invalid reader view styles
    public static final String INVALID_STYLE = "nonsense";
    public static final String INVALID_STYLE_DESC = " " + PREFIX_STYLE + INVALID_STYLE;

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditEntryDescriptor DESC_AMY;
    public static final EditCommand.EditEntryDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditEntryDescriptorBuilder()
                .withTitle(VALID_TITLE_AMY)
                .withDescription(VALID_DESCRIPTION_AMY)
                .withTags(VALID_TAG_TECH).build();
        DESC_BOB = new EditEntryDescriptorBuilder()
                .withTitle(VALID_TITLE_BOB)
                .withDescription(VALID_DESCRIPTION_BOB)
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
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandHistory, String, Model)}
     * that ignores checking model equality.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandHistory actualCommandHistory,
                                            String expectedMessage) {
        assertCommandSuccess(command, actualModel, actualCommandHistory, expectedMessage, actualModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the entry book, filtered entry list and selected entry in {@code actualModel} remain unchanged <br>
     * - {@code actualCommandHistory} remains unchanged.
     */
    public static void assertCommandFailure(Command command, Model actualModel, CommandHistory actualCommandHistory,
                                            String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        EntryBook expectedEntryBook = new EntryBook(actualModel.getListEntryBook());
        List<Entry> expectedFilteredList = new ArrayList<>(actualModel.getFilteredEntryList());
        Entry expectedSelectedEntry = actualModel.getSelectedEntry();

        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);

        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedEntryBook, actualModel.getListEntryBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredEntryList());
            assertEquals(expectedSelectedEntry, actualModel.getSelectedEntry());
            assertEquals(expectedCommandHistory, actualCommandHistory);
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the entry at the given {@code targetIndex} in the
     * {@code model}'s entry book.
     */
    public static void showEntryAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredEntryList().size());

        Entry entry = model.getFilteredEntryList().get(targetIndex.getZeroBased());
        model.updateFilteredEntryList(new EntryContainsSearchTermsPredicate(
                                                new FindEntryDescriptorBuilder()
                                                    .withLink(entry.getLink().value.toString())
                                                    .build()));

        assertEquals(1, model.getFilteredEntryList().size());
    }

    /**
     * Deletes the first entry in {@code model}'s filtered list from {@code model}'s entry book.
     */
    public static void deleteFirstEntry(Model model) {
        Entry firstEntry = model.getFilteredEntryList().get(0);
        model.deleteListEntry(firstEntry);
    }

}
