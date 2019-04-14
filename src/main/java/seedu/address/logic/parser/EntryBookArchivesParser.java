package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;

import seedu.address.logic.commands.ClearArchivesCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteArchiveEntryCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UnarchiveAllCommand;
import seedu.address.logic.commands.UnarchiveCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;

/**
 * Represents the parser for the archive-context.
 * It successfully parses a command if and only if the command is archive-context command.
 */
public class EntryBookArchivesParser extends EntryBookParser {

    /**
     * Parses user input into command for execution.
     * Parses successfully if and only if the command is archive-context command.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case DeleteArchiveEntryCommand.COMMAND_WORD:
            return new DeleteArchiveEntryCommandParser().parse(arguments);

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case ClearArchivesCommand.COMMAND_WORD:
            return new ClearArchivesCommand();

        case UnarchiveCommand.COMMAND_WORD:
        case UnarchiveCommand.COMMAND_ALIAS:
            return new UnarchiveCommandParser().parse(arguments);

        case UnarchiveAllCommand.COMMAND_WORD:
        case UnarchiveAllCommand.COMMAND_ALIAS:
            return new UnarchiveAllCommand();

        default:
            return super.parseCommand(userInput, ModelContext.CONTEXT_ARCHIVES);
        }
    }

}
