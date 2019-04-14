package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;

import seedu.address.logic.commands.ArchiveAllCommand;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.RefreshEntryCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;

/**
 * Represents the parser for the list-context.
 * It successfully parses a command if and only if the command is list-context command.
 */
public class EntryBookListParser extends EntryBookParser {

    /**
     * Parses user input into command for execution.
     * Parses successfully if and only if the command is list-context command.
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

        case ArchiveCommand.COMMAND_WORD:
        case ArchiveCommand.COMMAND_ALIAS:
            return new ArchiveCommandParser().parse(arguments);

        case ArchiveAllCommand.COMMAND_WORD:
        case ArchiveAllCommand.COMMAND_ALIAS:
            return new ArchiveAllCommand();

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case RefreshEntryCommand.COMMAND_WORD:
        case RefreshEntryCommand.COMMAND_ALIAS:
            return new RefreshEntryCommandParser().parse(arguments);


        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case ViewModeCommand.COMMAND_WORD:
        case ViewModeCommand.COMMAND_ALIAS:
            return new ViewModeCommandParser().parse(arguments);

        default:
            return super.parseCommand(userInput, ModelContext.CONTEXT_LIST);
        }
    }

}
