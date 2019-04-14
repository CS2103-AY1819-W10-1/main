package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.MainApp;
import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;

/**
 * A utility class containing a list of {@code Entry} objects to be used in tests.
 */
public class TypicalEntries {

    public static final Entry ALICE = new EntryBuilder()
            .withTitle("Alice Pauline")
            .withLink("https://alice.example.com")
            .withDescription("Description place-holder Carl")
            .withTags("friends")
            .build();
    public static final Entry BENSON = new EntryBuilder()
            .withTitle("Benson Meier")
            .withLink("https://johnd.example.com")
            .withDescription("Description place-holder Benson")
            .withTags("owesMoney")
            .build();
    public static final Entry CARL = new EntryBuilder()
            .withTitle("Carl Kurz")
            .withDescription("Description place-holder")
            .withLink("https://heinz.example.com")
            .build();
    public static final Entry DANIEL = new EntryBuilder()
            .withTitle("Daniel Meier")
            .withDescription("Description place-holder")
            .withLink("https://cornelia.example.com")
            .withTags("friends", "Carl")
            .build();
    public static final Entry ELLE = new EntryBuilder()
            .withTitle("Elle Meyer")
            .withDescription("Description place-holder")
            .withLink("https://werner.Carl.example.com")
            .build();
    public static final Entry FIONA = new EntryBuilder()
            .withTitle("Fiona Kunz")
            .withDescription("Description place-holder")
            .withLink("https://lydia.example.com")
            .build();
    public static final Entry GEORGE = new EntryBuilder()
            .withTitle("George Best")
            .withDescription("Description place-holder")
            .withLink("https://anna.example.com")
            .build();
    public static final Entry HANS = new EntryBuilder()
        .withTitle("Hans Solo")
        .withDescription("Description place-holder")
        .withLink("https://bobo.example.com")
        .withTags("friends")
        .build();
    public static final Entry ISABELLE = new EntryBuilder()
        .withTitle("Isabelle Toh")
        .withDescription("Description place-holder")
        .withLink("https://koko.example.com")
        .build();
    public static final Entry JONATHAN = new EntryBuilder()
        .withTitle("Jonathan Cheng")
        .withDescription("Description place-holder")
        .withLink("https://lolo.example.com")
        .build();
    public static final Entry KEVIN = new EntryBuilder()
        .withTitle("Kevin Hart")
        .withDescription("Description place-holder")
        .withLink("https://dodo.example.com")
        .build();
    public static final Entry UKI = new EntryBuilder()
        .withTitle("Uki Toki")
        .withDescription("Description place-holder")
        .withLink("https://uki.example.com")
        .build();
    public static final Entry VOO = new EntryBuilder()
        .withTitle("Voo Toki")
        .withDescription("Description place-holder")
        .withLink("https://voodoo.example.com")
        .build();
    public static final Entry WAX = new EntryBuilder()
        .withTitle("Wax Toki")
        .withDescription("Description place-holder")
        .withLink("https://wax.example.com")
        .build();
    public static final Entry XERNEX = new EntryBuilder()
        .withTitle("Xernex Toki")
        .withDescription("Description place-holder")
        .withLink("https://xernex.example.com")
        .build();
    public static final Entry YOYO = new EntryBuilder()
        .withTitle("Yo Toki")
        .withDescription("Description place-holder")
        .withLink("https://yoyo.example.com")
        .build();
    public static final Entry ZACK = new EntryBuilder()
        .withTitle("Zack Toki")
        .withDescription("Description place-holder")
        .withLink("https://zack.example.com")
        .build();

    // Manually added
    public static final Entry HOON = new EntryBuilder()
            .withTitle("Hoon Meier")
            .withDescription("Description place-holder")
            .withLink("https://stefan.example.com")
            .build();
    public static final Entry IDA = new EntryBuilder()
            .withTitle("Ida Mueller")
            .withDescription("Description place-holder")
            .withLink("https://hans.example.com")
            .build();

    // Manually added - Entry's details found in {@code CommandTestUtil}
    public static final Entry AMY = new EntryBuilder()
            .withTitle(VALID_TITLE_AMY)
            .withDescription(VALID_DESCRIPTION_AMY)
            .withLink(VALID_LINK_AMY)
            .withTags(VALID_TAG_TECH)
            .build();
    public static final Entry BOB = new EntryBuilder()
            .withTitle(VALID_TITLE_BOB)
            .withDescription(VALID_DESCRIPTION_BOB)
            .withLink(VALID_LINK_BOB)
            .withTags(VALID_TAG_SCIENCE, VALID_TAG_TECH)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier";
    public static final String KEYWORD_MATCHING_TOKI = "Toki";
    public static final String KEYPHRASE_NOT_MATCHING_ANYWHERE = "THISSTRINGISNOTMATCHINGANYENTRYANYFIELD";

    // For AddCommandTest
    public static final String DUMMY_TITLE = "Dummy Title";
    public static final String DUMMY_DESCRIPTION = "Dummy description";

    public static final URL STUB_LINK_URL = TestUtil.toUrl("http://www.description.test/title/title_test.html");
    public static final String STUB_LINK_TITLE = "Title Test";
    public static final String STUB_LINK_DESCRIPTION = "www.description.test";

    public static final URL REAL_LINK_URL = MainApp.class.getResource("/ModelManagerTest/NUS_School_of_Computing.html");
    public static final String REAL_LINK_TITLE = "NUS School of Computing - Wikipedia";
    public static final String REAL_LINK_DESCRIPTION = "COM1. The Computing Dean's Office "
            + "and Department of Computer Science are located in this building.";

    public static final URL READABILITY_LINK_URL = MainApp.class.getResource("/ModelManagerTest/ProPublica.html");
    public static final String READABILITY_LINK_TITLE = "A Reading Guide — ProPublica";
    public static final String READABILITY_LINK_DESCRIPTION = "This week’s testimony by President Donald Trump’s "
            + "former personal attorney and fixer held millions rapt with allegations of fraud, coded orders to lie "
            + "and hundreds of threats. Many of those assertions had…";

    public static final URL RELATIVE_LINK_ARTICLE_URL = MainApp.class
            .getResource("/util/relativeLinkedArticle.html");
    public static final URL ABSOLUTE_LINK_ARTICLE_URL = MainApp.class
            .getResource("/util/absoluteLinkedArticle.html");

    public static final Entry STUB_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription("")
            .withLink(STUB_LINK_URL)
            .build();
    public static final Entry STUB_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(STUB_LINK_TITLE)
            .withDescription(STUB_LINK_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();

    public static final Entry STUB_LINK_NO_TITLE_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();
    public static final Entry STUB_LINK_NO_TITLE_COMPLETE = new EntryBuilder()
            .withTitle(STUB_LINK_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();

    public static final Entry STUB_LINK_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription("")
            .withLink(STUB_LINK_URL)
            .build();
    public static final Entry STUB_LINK_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(STUB_LINK_DESCRIPTION)
            .withLink(STUB_LINK_URL)
            .build();

    public static final Entry REAL_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription("")
            .withLink(REAL_LINK_URL)
            .build();
    public static final Entry REAL_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(REAL_LINK_TITLE)
            .withDescription(REAL_LINK_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();

    public static final Entry REAL_LINK_NO_TITLE_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();
    public static final Entry REAL_LINK_NO_TITLE_COMPLETE = new EntryBuilder()
            .withTitle(REAL_LINK_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();

    public static final Entry REAL_LINK_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription("")
            .withLink(REAL_LINK_URL)
            .build();
    public static final Entry REAL_LINK_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(REAL_LINK_DESCRIPTION)
            .withLink(REAL_LINK_URL)
            .build();

    public static final Entry READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription("")
            .withLink(READABILITY_LINK_URL)
            .build();
    public static final Entry READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(READABILITY_LINK_TITLE)
            .withDescription(READABILITY_LINK_DESCRIPTION)
            .withLink(READABILITY_LINK_URL)
            .build();

    public static final Entry READABILITY_LINK_NO_TITLE_INCOMPLETE = new EntryBuilder()
            .withTitle("")
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(READABILITY_LINK_URL)
            .build();
    public static final Entry READABILITY_LINK_NO_TITLE_COMPLETE = new EntryBuilder()
            .withTitle(READABILITY_LINK_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(READABILITY_LINK_URL)
            .build();

    public static final Entry READABILITY_LINK_NO_DESCRIPTION_INCOMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription("")
            .withLink(READABILITY_LINK_URL)
            .build();
    public static final Entry READABILITY_LINK_NO_DESCRIPTION_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(READABILITY_LINK_DESCRIPTION)
            .withLink(READABILITY_LINK_URL)
            .build();

    public static final Entry READABILITY_LINK_COMPLETE = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(READABILITY_LINK_URL)
            .build();

    public static final Entry ENTRY_WITH_RELATIVE_LINK = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(RELATIVE_LINK_ARTICLE_URL)
            .build();
    public static final Entry ENTRY_WITH_ABSOLUTE_LINK = new EntryBuilder()
            .withTitle(DUMMY_TITLE)
            .withDescription(DUMMY_DESCRIPTION)
            .withLink(ABSOLUTE_LINK_ARTICLE_URL)
            .build();

    // For testing of networking
    public static final Entry VALID_HTTPS_LINK = new EntryBuilder()
            .withTitle("Valid HTTPS Link")
            .withDescription("Valid https link")
            .withLink("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/")
            .build();
    public static final Entry VALID_HTTP_LINK = new EntryBuilder()
            .withTitle("Valid HTTP Link")
            .withDescription("Valid http link")
            .withLink("http://cs2103-ay1819s2-w10-1.github.io/main/networktests/")
            .build();
    public static final Entry VALID_FILE_LINK = new EntryBuilder()
            .withTitle("Valid File Link")
            .withDescription("Valid file link")
            .withLink(MainApp.class.getResource("/NetworkTest/default.html"))
            .build();
    public static final Entry INVALID_FILE_LINK = new EntryBuilder()
            .withTitle("Invalid File Link")
            .withDescription("Invalid file link")
            .withLink("file:///folder/file.type")
            .build();
    public static final String FILE_TEST_CONTENTS = "<!DOCTYPE html>\n<html>\n</html>\n";

    // For BrowserPanelTest
    public static final URL BROWSER_PANEL_TEST_ENTRY_BASE_URL =
        MainApp.class.getResource("/view/BrowserPanelTest/test.html");
    public static final Entry BROWSER_PANEL_TEST_ENTRY = new EntryBuilder()
            .withTitle("Browser Panel Test Web Page")
            .withDescription("Browser panel test web page")
            .withLink(BROWSER_PANEL_TEST_ENTRY_BASE_URL)
            .build();
    public static final String WIKIPEDIA_ENTRY_BASE_URL = "http://en.wikipedia.org/wiki/Therapsids";
    public static final Entry WIKIPEDIA_ENTRY = new EntryBuilder()
            .withTitle("Wikipedia Test Web Page")
            .withDescription("Wikipedia test web page")
            .withLink(MainApp.class.getResource("/view/BrowserPanelTest/wikipedia.html"))
            .build();
    public static final Entry REMOTE_WIKIPEDIA_ENTRY = new EntryBuilder()
        .withTitle("Wikipedia Test Web Page")
        .withDescription("Wikipedia test web page")
        .withLink(WIKIPEDIA_ENTRY_BASE_URL)
        .build();

    // bunch of RSS feeds
    public static final Entry KATTIS_FEED_ENTRY = new EntryBuilder()
            .withTitle("Kattis - new problems")
            .withDescription("kattis the kat")
            .withLink("https://open.kattis.com/rss/new-problems")
            .build();

    public static final Entry EMPTY_FEED_ENTRY = new EntryBuilder()
        .withTitle("An empty feed")
        .withDescription("Very empty feed")
        .withLink(MainApp.class.getResource("/RssFeedTest/emptyrss.xml"))
        .build();

    public static final Entry ONE_ITEM_FEED_ENTRY = new EntryBuilder()
        .withTitle("One item feed")
        .withDescription("Almost empty feed")
        .withLink(MainApp.class.getResource("/RssFeedTest/oneitemrss.xml"))
        .build();

    public static final Entry LOCAL_FEED_ENTRY = new EntryBuilder()
            .withTitle("Tsutsukakushi's anime reviews - local copy")
            .withDescription("anime reviews")
            .withLink(MainApp.class.getResource("/RssFeedTest/rss.xml"))
            .build();

    public static final Entry NOT_A_FEED_ENTRY = new EntryBuilder()
        .withTitle("Not a feed")
        .withDescription("Not a feed")
        .withLink(MainApp.class.getResource("/RssFeedTest/notafeed.notxml"))
        .build();

    public static final Entry ANIMEREVIEW_FEED_ENTRY = new EntryBuilder()
            .withTitle("Tsutsukakushi's anime reviews - remote mirror")
            .withDescription("anime reviews!")
            .withLink("https://cs2103-ay1819s2-w10-1.github.io/main/networktests/rss.xml")
            .build();

    public static final Entry ENGADGET_FEED_ENTRY = new EntryBuilder()
            .withTitle("Engadget RSS")
            .withLink("https://live.engadget.com/rss.xml")
            .build();

    private TypicalEntries() {} // prevents instantiation

    /**
     * Returns an {@code EntryBook} with all the typical entries.
     */
    public static EntryBook getTypicalListEntryBook() {
        EntryBook ab = new EntryBook();
        for (Entry entry : getTypicalEntries()) {
            ab.addEntry(entry);
        }
        return ab;
    }

    /**
     * Returns an {@code EntryBook} with all the typical archive entries.
     */
    public static EntryBook getTypicalArchivesEntryBook() {
        EntryBook ab = new EntryBook();
        for (Entry entry : getTypicalArchivesEntries()) {
            ab.addEntry(entry);
        }
        return ab;
    }

    /** Returns a typical EntryBook with typical feed subscriptions. */
    public static EntryBook getTypicalFeedsEntryBook() {
        EntryBook eb = new EntryBook();
        eb.setEntries(getTypicalFeedsEntries());
        return eb;
    }

    public static List<Entry> getTypicalEntries() {
        return new ArrayList<>(Arrays.asList(
            ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE,
            UKI, VOO, WAX, XERNEX, YOYO, ZACK));
    }

    public static List<Entry> getTypicalArchivesEntries() {
        return new ArrayList<>(Arrays.asList(HANS, ISABELLE, JONATHAN, KEVIN));
    }

    public static List<Entry> getTypicalFeedsEntries() {
        return List.of(LOCAL_FEED_ENTRY, KATTIS_FEED_ENTRY);
    }
}
