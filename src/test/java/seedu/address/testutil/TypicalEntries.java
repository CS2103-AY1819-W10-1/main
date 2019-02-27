package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COMMENT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COMMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;

/**
 * A utility class containing a list of {@code Entry} objects to be used in tests.
 */
public class TypicalEntries {

    public static final Entry ALICE = new EntryBuilder()
            .withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withEmail("https://alice.example.com")
            .withPhone("Comment place-holder")
            .withTags("friends")
            .build();
    public static final Entry BENSON = new EntryBuilder()
            .withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("https://johnd.example.com")
            .withPhone("Comment place-holder")
            .withTags("owesMoney", "friends")
            .build();
    public static final Entry CARL = new EntryBuilder()
            .withName("Carl Kurz")
            .withPhone("Comment place-holder")
            .withEmail("https://heinz.example.com")
            .withAddress("wall street")
            .build();
    public static final Entry DANIEL = new EntryBuilder()
            .withName("Daniel Meier")
            .withPhone("Comment place-holder")
            .withEmail("https://cornelia.example.com")
            .withAddress("10th street")
            .withTags("friends")
            .build();
    public static final Entry ELLE = new EntryBuilder()
            .withName("Elle Meyer")
            .withPhone("Comment place-holder")
            .withEmail("https://werner.example.com")
            .withAddress("michegan ave")
            .build();
    public static final Entry FIONA = new EntryBuilder()
            .withName("Fiona Kunz")
            .withPhone("Comment place-holder")
            .withEmail("https://lydia.example.com")
            .withAddress("little tokyo")
            .build();
    public static final Entry GEORGE = new EntryBuilder()
            .withName("George Best")
            .withPhone("Comment place-holder")
            .withEmail("https://anna.example.com")
            .withAddress("4th street")
            .build();

    // Manually added
    public static final Entry HOON = new EntryBuilder()
            .withName("Hoon Meier")
            .withPhone("Comment place-holder")
            .withEmail("https://stefan.example.com")
            .withAddress("little india")
            .build();
    public static final Entry IDA = new EntryBuilder()
            .withName("Ida Mueller")
            .withPhone("Comment place-holder")
            .withEmail("https://hans.example.com")
            .withAddress("chicago ave")
            .build();

    // Manually added - Entry's details found in {@code CommandTestUtil}
    public static final Entry AMY = new EntryBuilder()
            .withName(VALID_TITLE_AMY)
            .withPhone(VALID_COMMENT_AMY)
            .withEmail(VALID_LINK_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withTags(VALID_TAG_TECH)
            .build();
    public static final Entry BOB = new EntryBuilder()
            .withName(VALID_TITLE_BOB)
            .withPhone(VALID_COMMENT_BOB)
            .withEmail(VALID_LINK_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withTags(VALID_TAG_SCIENCE, VALID_TAG_TECH)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalEntries() {} // prevents instantiation

    /**
     * Returns an {@code EntryBook} with all the typical persons.
     */
    public static EntryBook getTypicalAddressBook() {
        EntryBook ab = new EntryBook();
        for (Entry entry : getTypicalPersons()) {
            ab.addPerson(entry);
        }
        return ab;
    }

    public static List<Entry> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
