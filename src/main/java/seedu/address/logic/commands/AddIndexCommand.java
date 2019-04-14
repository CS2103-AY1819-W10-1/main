package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;

/**
 * Adds an entry identified using its displayed index to the List context EntryBook.
 * This usage only makes sense in search context.
 */
public class AddIndexCommand extends Command {

    public static final String COMMAND_WORD = "addindex";
    public static final String COMMAND_ALIAS = "addi";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds the entry identified by the index number used in the displayed entry list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index targetIndex;

    public AddIndexCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Entry entryToAdd = lastShownList.get(targetIndex.getZeroBased());
        Command addCommand = new AddCommand(entryToAdd);
        return addCommand.execute(model, history);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddIndexCommand // instanceof handles nulls
                && targetIndex.equals(((AddIndexCommand) other).targetIndex)); // state check
    }
}
