package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.network.Network;

/**
 * Shows a feed given an URL.
 */
public class FeedCommand extends Command {

    public static final String COMMAND_WORD = "feed";

    public static final String MESSAGE_SUCCESS = "Opened feed %s";
    public static final String MESSAGE_FAILURE_NET = "Network connection failed:\n%s";
    public static final String MESSAGE_FAILURE_XML = "%s is not a valid RSS/Atom feed!";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Opens a link as an RSS feed and adds all its entries.\n"
            + "Parameters: LINK\n"
            + "Example: " + COMMAND_WORD + " https://open.kattis.com/rss/new-problems";
    public static final String DEFAULT_DESCRIPTION_TEXT = "imported from %s";

    private String feedUrl;
    public FeedCommand(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        try {
            InputStream inputStream = Network.fetchAsStream(feedUrl);
            SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(inputStream));
            convertToEntryList(syndFeed).forEach(model::addEntry);
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_NET, e), e);
        } catch (FeedException e) {
            throw new CommandException(String.format(MESSAGE_FAILURE_XML, feedUrl), e);
        } catch (Exception e) {
            throw new CommandException("Some other problem: " + StringUtil.getDetails(e), e);
        }

        model.updateFilteredEntryList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, feedUrl));
    }

    private List<Entry> convertToEntryList(SyndFeed syndFeed) {
        return syndFeed.getEntries().stream()
                .map(this::syndEntryToEntryBookEntry)
                .collect(Collectors.toList());
    }

    /** Converts a SyndEntry into an EntryBook Entry. */
    private Entry syndEntryToEntryBookEntry(SyndEntry syndEntry) {
        return new Entry(
                new Title(syndEntry.getTitle().trim()),
                extractDescription(syndEntry),
                new Link(syndEntry.getLink()),
                new Address("unused"),
                Collections.emptySet()
        );
    }

    /** Extracts a useful description from a SyndEntry. */
    private Description extractDescription(SyndEntry syndEntry) {
        String description = syndEntry.getDescription().getValue().replace('\n', ' ').trim();
        if (description.isEmpty()) {
            description = String.format(DEFAULT_DESCRIPTION_TEXT, feedUrl);
        }
        return new Description(description);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FeedCommand // instanceof handles nulls
                && feedUrl.equals(((FeedCommand) other).feedUrl)); // state check
    }
}
