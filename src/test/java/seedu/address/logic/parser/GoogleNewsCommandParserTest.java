package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.junit.Test;

import seedu.address.logic.commands.GoogleNewsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class GoogleNewsCommandParserTest {

    private GoogleNewsCommandParser parser = new GoogleNewsCommandParser();

    @Test
    public void parse_validArgs_success() throws ParseException, UnsupportedEncodingException, MalformedURLException {
        assertEquals(new GoogleNewsCommand("1"), parser.parse("1"));
    }

    @Test
    public void parse_argsAreTrimmed() throws ParseException {
        assertEquals(parser.parse("1"), parser.parse(" 1 "));
    }

    @Test
    public void parse_emptyArgs_returnsDefaultGoogleNewsCommand() throws ParseException, MalformedURLException {
        assertEquals(new GoogleNewsCommand(), parser.parse(""));
    }
}
