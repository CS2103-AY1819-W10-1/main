package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.util.FeedUtil.DEFAULT_ADDRESS_TEXT;
import static seedu.address.commons.util.FeedUtil.DEFAULT_DESCRIPTION_TEXT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.FeedCommand.MESSAGE_FAILURE_NET;
import static seedu.address.logic.commands.FeedCommand.MESSAGE_FAILURE_XML;
import static seedu.address.logic.commands.FeedCommand.MESSAGE_SUCCESS;

import java.util.Collections;

import org.junit.Test;

import seedu.address.MainApp;
import seedu.address.logic.CommandHistory;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.testutil.EntryBookBuilder;

public class FeedCommandTest {
    private static final String TEST_URL = "https://cs2103-ay1819s2-w10-1.github.io/main/networktests/rss.xml";
    private static final String TEST_URL_LOCAL = MainApp.class.getResource("/RssFeedTest/rss.xml")
            .toExternalForm();

    private static final String MALFORMED_URL = "notavalidprotocol://malformed.url/invalid";
    private static final String NOTAFEED_URL =
        "https://cs2103-ay1819s2-w10-1.github.io/main/networktests/notafeed.notxml";

    private Model model = new ModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * Generates a list of entries. Based on how the default command works, it has
     * to be a function that takes in the url.
     */
    private static EntryBook getTestEntryBook(String url) {
        return new EntryBookBuilder()
                .withEntry(makeEntryFromRssTriple("Anime: Mahoujin Guru Guru",
                        "https://blog.GNU.moe/anime/review/mahoujin-guru-guru.html",
                        "Anime review 1"))
                .withEntry(makeEntryFromRssTriple("Anime: Gamers!",
                        "https://blog.GNU.moe/anime/review/gamers.html",
                        "Anime review 2"))
                .withEntry(makeEntryFromRssTriple("Anime: Made in Abyss",
                        "https://blog.GNU.moe/anime/review/made-in-abyss.html",
                        "Anime review n"))
                .withEntry(makeEntryFromRssTriple("Anime: Mob Psycho 100",
                        "https://blog.GNU.moe/anime/review/mob-psycho.html",
                        "Anime review"))
                .withEntry(makeEntryFromRssTriple("Anime: New Game!!",
                        "https://blog.GNU.moe/anime/review/new-game-2.html",
                        "Anime revieww"))
                .withEntry(makeEntryFromRssTriple("Anime: Saiki Kusuo no Psi-nan",
                        "https://blog.GNU.moe/anime/review/saiki-kusuo.html",
                        "sigh"))
                .withEntry(makeEntryFromRssTriple("Anime: Durarara!!",
                        "https://blog.GNU.moe/anime/review/durarara.html",
                        String.format(DEFAULT_DESCRIPTION_TEXT, url)))
                .withEntry(makeEntryFromRssTriple("Anime: Battle Programmer Shirase",
                        "https://blog.GNU.moe/anime/review/bps.html",
                        "lol"))
                .withEntry(makeEntryFromRssTriple("Anime: Re:Zero",
                        "https://blog.GNU.moe/anime/review/re_zero.html",
                        "idk"))
                .withEntry(makeEntryFromRssTriple("Anime: Youjo Senki",
                        "https://blog.GNU.moe/anime/review/youjo_senki.html",
                        "I like this reviewer"))
                .build();
    }

    /** Makes an EntryBook entry from the 3 fields that we are harvesting from RSS. */
    private static Entry makeEntryFromRssTriple(String title, String link, String description) {
        return new Entry(
                new Title(title),
                new Description(description),
                new Link(link),
                new Address(DEFAULT_ADDRESS_TEXT),
                Collections.emptySet()
        );
    }

    /** Asserts that executing a FeedCommand with the given url imports the Entry list. */
    public void assertLoadingUrlDisplaysEntryBook(String url, EntryBook expectedDisplayedEntryBook) {
        Model model = new ModelManagerStub();
        Model expectedModel = new ModelManagerStub();
        CommandHistory commandHistory = new CommandHistory();

        String expectedMessage = String.format(MESSAGE_SUCCESS, url);
        FeedCommand command = new FeedCommand(url);

        expectedModel.setSearchEntryBook(expectedDisplayedEntryBook);
        expectedModel.setContext(ModelContext.CONTEXT_SEARCH);
        // todo: isolate command test and util test

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        String firstUrl = "https://open.kattis.com/rss/new-problems";
        String secondUrl = "https://en.wikipedia.org/w/index.php?title=Special:RecentChanges&feed=rss";

        FeedCommand feedFirstCommand = new FeedCommand(firstUrl);
        FeedCommand feedSecondCommand = new FeedCommand(secondUrl);

        // same object -> returns true
        assertTrue(feedFirstCommand.equals(feedFirstCommand));

        // same values -> returns true
        FeedCommand feedFirstCommandCopy = new FeedCommand(firstUrl);
        assertTrue(feedFirstCommand.equals(feedFirstCommandCopy));

        // different types -> returns false
        assertFalse(feedFirstCommand.equals(1));

        // null -> returns false
        assertFalse(feedFirstCommand.equals(null));

        // different entry -> returns false
        assertFalse(feedFirstCommand.equals(feedSecondCommand));
    }

    @Test
    public void execute_localUrl_success() {
        assertLoadingUrlDisplaysEntryBook(TEST_URL_LOCAL, getTestEntryBook(TEST_URL_LOCAL));
    }

    @Test
    public void execute_remoteUrl_success() {
        assertLoadingUrlDisplaysEntryBook(TEST_URL, getTestEntryBook(TEST_URL));
    }

    @Test
    public void execute_malformedUrlGiven_commandFails() {
        String expectedMessage = String.format(MESSAGE_FAILURE_NET,
                "java.net.MalformedURLException: unknown protocol: notavalidprotocol");
        FeedCommand command = new FeedCommand(MALFORMED_URL);

        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_urlIsNotAFeed_commandFails() {
        String expectedMessage = String.format(MESSAGE_FAILURE_XML, NOTAFEED_URL);
        FeedCommand command = new FeedCommand(NOTAFEED_URL);

        assertCommandFailure(command, model, commandHistory, expectedMessage);
    }
}
