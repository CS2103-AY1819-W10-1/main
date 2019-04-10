package seedu.address.logic.parser;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import seedu.address.logic.commands.GoogleNewsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class GoogleNewsCommandParser implements Parser<GoogleNewsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GoogleNewsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        try {
            if (trimmedArgs.isEmpty()) {
                return new GoogleNewsCommand();
            }
            return new GoogleNewsCommand(trimmedArgs);
        } catch (UnsupportedEncodingException uee) {
            throw new ParseException("UTF-8 encoding not supported by system!", uee);
        } catch (MalformedURLException mue) {
            throw new ParseException("Sorry, Google News is unavailable. " + mue.getMessage(), mue);
        }
    }

}
