package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.DuplicateEntryCommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;

/**
 * Adds an entry identified using its displayed index to the List context EntryBook.
 * This usage only makes sense in search context.
 */
public class AddAllCommand extends Command {

    public static final String COMMAND_WORD = "addall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds all entries currently in the displayed entry list.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "All entries added (%s entries)\n"
        + "Some entries may have been skipped if they were already present in the reading list";

    private static final Logger logger = LogsCenter.getLogger(UnarchiveAllCommand.class);

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();

        int numAdded = 0;
        for (Entry entryToAdd : lastShownList) {
            Command addCommand = new AddCommand(entryToAdd);
            try {
                addCommand.execute(model, history);
                numAdded++;
            } catch (DuplicateEntryCommandException dece) {
                logger.warning("Skipping entry which is already in reading list:\n"
                    + entryToAdd);
                // Ignore duplicate entry errors
                // Rethrow other errors
            }
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, numAdded));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || other instanceof AddAllCommand; // instanceof handles nulls
    }
}
