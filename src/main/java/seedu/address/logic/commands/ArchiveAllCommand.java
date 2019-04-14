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
public class ArchiveAllCommand extends Command {

    public static final String COMMAND_WORD = "archiveall";
    public static final String COMMAND_ALIAS = "archall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Archives all entries in the displayed entry list.\n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "All entries archived: (%s entries)\n"
        + "Some entries may have been skipped if they were already present in the archives list";

    private static final Logger logger = LogsCenter.getLogger(ArchiveAllCommand.class);

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();
        logger.info(String.format("%d", lastShownList.size()));

        int numArchived = 0;
        for (int i = lastShownList.size(); i > 0; i--) {
            Command archiveCommand = new ArchiveCommand(Index.fromOneBased(i));
            try {
                archiveCommand.execute(model, history);
                numArchived++;
            } catch (DuplicateEntryCommandException dece) {
                logger.info("Skipping duplicate entry at index " + Index.fromOneBased(i));
                // Ignore duplicate entry errors
                // Rethrow other errors
            }
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, numArchived));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ArchiveAllCommand); // instanceof handles nulls
    }
}
