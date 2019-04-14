package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;

/**
 * Refreshes all entries in the displayed entry list.
 */
public class RefreshAllFeedsCommand extends Command {

    public static final String COMMAND_WORD = "refreshall";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Refreshes all entries in the displayed entry list.\n"
            + "May take a while to execute!\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_TRIVIAL_SUCCESS = "No feeds to refresh";
    public static final String MESSAGE_SUCCESS = "Refreshed %d feeds";
    public static final String MESSAGE_PARTIAL_SUCCESS =
        "Refreshed %d feeds, but stopping at feed %d (%s) as it could not be refreshed.\n"
            + "Please check that the links point to valid feeds and that you are connected to the internet.";
    public static final String MESSAGE_FAILURE =
        "Stopping as the first feed could not be refreshed.\n"
            + "Please check that the links point to valid feeds and that you are connected to the internet.";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<Entry> filteredEntryList = model.getFilteredEntryList();

        int numEntries = filteredEntryList.size();

        if (numEntries == 0) {
            return new CommandResult(MESSAGE_TRIVIAL_SUCCESS);
        }

        int numRefreshed = 0;
        for (int i = 0; i < numEntries; i++) {
            Command refreshCommand = new RefreshFeedCommand(Index.fromZeroBased(i));
            try {
                refreshCommand.execute(model, history);
                numRefreshed++;
            } catch (CommandException ce) {
                if (i == 0) {
                    throw new CommandException(MESSAGE_FAILURE);
                } else {
                    Entry entryThatCouldNotBeRefreshed = filteredEntryList.get(i);
                    return new CommandResult(String.format(
                        MESSAGE_PARTIAL_SUCCESS,
                        i,
                        i + 1,
                        entryThatCouldNotBeRefreshed.getLink()));
                }
            }
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, numRefreshed));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RefreshAllFeedsCommand); // instanceof handles nulls
    }
}
