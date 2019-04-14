package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.DuplicateEntryCommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.DuplicateEntryException;
import seedu.address.util.Network;

/**
 * Lists all entries in the archives to the user.
 */
public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";
    public static final String COMMAND_ALIAS = "unarch";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Un-archives the entry identified by the index number used in the displayed entry list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNARCHIVE_ENTRY_SUCCESS = "Entry unarchived: %1$s";

    private final Index targetIndex;

    public UnarchiveCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Entry> lastShownList = model.getFilteredEntryList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Entry entryToUnarchive = lastShownList.get(targetIndex.getZeroBased());
        Optional<byte[]> articleContent = Network.fetchArticleOptional(entryToUnarchive.getLink().value);
        try {
            model.unarchiveEntry(entryToUnarchive, articleContent);
        } catch (DuplicateEntryException dee) {
            throw new DuplicateEntryCommandException();
        }
        return new CommandResult(String.format(MESSAGE_UNARCHIVE_ENTRY_SUCCESS, entryToUnarchive));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof UnarchiveCommand // instanceof handles nulls
            && targetIndex.equals(((UnarchiveCommand) other).targetIndex)); // state check
    }

}
