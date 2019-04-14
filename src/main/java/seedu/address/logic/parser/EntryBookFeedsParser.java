package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;

import seedu.address.logic.commands.ClearFeedsCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.RefreshFeedCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UnsubscribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;

/**
 * Represents the parser for the feeds context.
 * It successfully parses a command if and only if the command is a feeds context or context-independent command.
 */
public class EntryBookFeedsParser extends EntryBookParser {

    /** Parses user input into command for execution. */
    @Override
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case RefreshFeedCommand.COMMAND_WORD:
            return new RefreshFeedCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case ClearFeedsCommand.COMMAND_WORD:
            return new ClearFeedsCommand();

        case UnsubscribeCommand.COMMAND_WORD:
        case UnsubscribeCommand.COMMAND_ALIAS:
            return new UnsubscribeCommandParser().parse(arguments);

        default:
            return super.parseCommand(userInput, ModelContext.CONTEXT_FEEDS);
        }
    }
}
