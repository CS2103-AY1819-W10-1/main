package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;

import seedu.address.logic.commands.AddAllCommand;
import seedu.address.logic.commands.AddIndexCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;

/**
 * Represents the parser for the search context.
 * It successfully parses a command if and only if the command is a search context or context-switching command.
 */
public class EntryBookSearchParser extends EntryBookParser {

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

        case AddIndexCommand.COMMAND_WORD:
        case AddIndexCommand.COMMAND_ALIAS:
            return new AddIndexCommandParser().parse(arguments);

        case AddAllCommand.COMMAND_WORD:
            return new AddAllCommand();

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case ViewModeCommand.COMMAND_WORD:
        case ViewModeCommand.COMMAND_ALIAS:
            return new ViewModeCommandParser().parse(arguments);

        default:
            return super.parseCommand(userInput, ModelContext.CONTEXT_SEARCH);
        }
    }
}
