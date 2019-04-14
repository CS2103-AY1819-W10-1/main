package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.BingWebSearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class BingWebSearchCommandParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private BingWebSearchCommandParser parser = new BingWebSearchCommandParser();

    @Test
    public void parse_validArgs_success() throws ParseException, UnsupportedEncodingException, MalformedURLException {
        assertEquals(new BingWebSearchCommand("1"), parser.parse("1"));
    }

    @Test
    public void parse_argsAreTrimmed() throws ParseException {
        assertEquals(parser.parse("1"), parser.parse(" 1 "));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() throws ParseException {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BingWebSearchCommand.MESSAGE_USAGE));
        parser.parse("");
    }
}
