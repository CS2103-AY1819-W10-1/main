package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Test;

import seedu.address.logic.commands.RefreshFeedCommand;

/**
 * Test scope: similar to {@code DeleteCommandParserTest}.
 * @see DeleteCommandParserTest
 */
public class RefreshFeedCommandParserTest {

    private RefreshFeedCommandParser parser = new RefreshFeedCommandParser();

    @Test
    public void parse_validArgs_returnsRefreshFeedCommand() {
        assertParseSuccess(parser, "1", new RefreshFeedCommand(INDEX_FIRST_ENTRY));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RefreshFeedCommand.MESSAGE_USAGE));
    }
}
