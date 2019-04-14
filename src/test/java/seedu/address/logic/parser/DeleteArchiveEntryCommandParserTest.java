package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Test;

import seedu.address.logic.commands.DeleteArchiveEntryCommand;

/**
 * Test scope: similar to {@code DeleteCommandParserTest}.
 * @see DeleteCommandParserTest
 */
public class DeleteArchiveEntryCommandParserTest {

    private DeleteArchiveEntryCommandParser parser = new DeleteArchiveEntryCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteArchiveEntryCommand() {
        assertParseSuccess(parser, "1", new DeleteArchiveEntryCommand(INDEX_FIRST_ENTRY));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteArchiveEntryCommand.MESSAGE_USAGE));
    }
}
