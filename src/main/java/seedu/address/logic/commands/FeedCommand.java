package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ENTRIES;

import java.io.IOException;
import java.net.URL;

import com.rometools.rome.io.FeedException;

import seedu.address.commons.util.FeedUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

/**
 * Shows a feed given an URL.
 */
public class FeedCommand extends Command {

    public static final String COMMAND_WORD = "feed";

    public static final String MESSAGE_SUCCESS = "Reading feed: %s";
    public static final String MESSAGE_FAILURE_NET = "Network connection failed:\n%s";
    public static final String MESSAGE_FAILURE_XML = "%s is not a valid RSS/Atom feed!";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Opens a link as an RSS feed and adds all its entries.\n"
            + "Parameters: LINK\n"
            + "Example: " + COMMAND_WORD + " https://open.kattis.com/rss/new-problems";

    private URL feedUrl;
    public FeedCommand(URL feedUrl) {
        this.feedUrl = feedUrl;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        EntryBook toBeDisplayed;

        try {
            toBeDisplayed = FeedUtil.fromFeedUrl(feedUrl);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_NET, e), e);
        } catch (FeedException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_XML, feedUrl), e);
        } /*catch (Exception e) {
            throw new CommandException("Some other problem: " + StringUtil.getDetails(e), e);
        }*/

        model.setSearchEntryBook(toBeDisplayed);
        model.setContext(ModelContext.CONTEXT_SEARCH);
        model.updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);

        return new CommandResult(String.format(MESSAGE_SUCCESS, feedUrl));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FeedCommand // instanceof handles nulls
                && feedUrl.equals(((FeedCommand) other).feedUrl)); // state check
    }
}
