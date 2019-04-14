package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.DuplicateEntryCommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;

/**
 * Archives all entries in the displayed entry list.
 */
public class UnarchiveAllCommand extends Command {

    public static final String COMMAND_WORD = "unarchiveall";
    public static final String COMMAND_ALIAS = "unarchall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Un-archives all entries in the displayed entry list.\n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "All entries unarchived: (%s entries)\n"
        + "Some entries may have been skipped if they were already present in the reading list";

    private static final Logger logger = LogsCenter.getLogger(UnarchiveAllCommand.class);

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();
        logger.info(String.format("%d", lastShownList.size()));

        int numUnarchived = 0;
        for (int i = lastShownList.size(); i > 0; i--) {
            Command unarchiveCommand = new UnarchiveCommand(Index.fromOneBased(i));
            try {
                unarchiveCommand.execute(model, history);
                numUnarchived++;
            } catch (DuplicateEntryCommandException dece) {
                logger.info("Skipping duplicate entry at index " + Index.fromOneBased(i));
                // Ignore duplicate entry errors
                // Rethrow other errors
            }
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, numUnarchived));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof UnarchiveAllCommand); // instanceof handles nulls
    }
}
