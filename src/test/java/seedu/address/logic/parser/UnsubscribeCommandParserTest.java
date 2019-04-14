package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Test;

import seedu.address.logic.commands.UnsubscribeCommand;

/**
 * Test scope: similar to {@code DeleteCommandParserTest}.
 * @see DeleteCommandParserTest
 */
public class UnsubscribeCommandParserTest {

    private UnsubscribeCommandParser parser = new UnsubscribeCommandParser();

    @Test
    public void parse_validArgs_returnsUnsubscribeCommand() {
        assertParseSuccess(parser, "1", new UnsubscribeCommand(INDEX_FIRST_ENTRY));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnsubscribeCommand.MESSAGE_USAGE));
    }
}
