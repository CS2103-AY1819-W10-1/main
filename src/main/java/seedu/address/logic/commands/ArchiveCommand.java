package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.DuplicateEntryCommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.DuplicateEntryException;

/**
 * Archives an entry in the reading list.
 */
public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";
    public static final String COMMAND_ALIAS = "arch";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Archives the entry identified by the index number used in the displayed entry list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_ARCHIVE_ENTRY_SUCCESS = "Entry archived: %1$s";

    private final Index targetIndex;

    public ArchiveCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }
        Entry entryToArchive = lastShownList.get(targetIndex.getZeroBased());
        try {
            model.archiveEntry(entryToArchive);
        } catch (DuplicateEntryException dee) {
            throw new DuplicateEntryCommandException();
        }
        return new CommandResult(String.format(MESSAGE_ARCHIVE_ENTRY_SUCCESS, entryToArchive));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ArchiveCommand // instanceof handles nulls
            && targetIndex.equals(((ArchiveCommand) other).targetIndex)); // state check
    }
}
