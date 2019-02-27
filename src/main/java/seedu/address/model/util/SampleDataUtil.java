package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code EntryBook} with sample data.
 */
public class SampleDataUtil {
    public static Entry[] getSamplePersons() {
        return new Entry[] {
            new Entry(
                new Title("CS2103 the best mod ever?"),
                new Comment("CS2101 disagrees!"),
                new Link("https://www.comp.nus.edu.sg/programmes/ug/cs/"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("nus", "soc")),
            new Entry(
                new Title("Filler - "),
                new Comment("or is it...?"),
                new Link("file:///example/folder/file"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet()),
            new Entry(
                new Title("Qiji trivalises all engineering majors!"),
                new Comment("Has he taken it too far?"),
                new Link("https://github.com/qjqqyy"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("engineering")),
            new Entry(
                new Title("Jonathan voted best in procrastination!"),
                new Comment("The 22-time winner says \"I don't want to win anymore...\"!"),
                new Link("https://github.com/epicfailname"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("award")),
            new Entry(
                new Title("Thomas invents time travel!"),
                new Comment("Still can't finish his work.."),
                new Link("https://github.com/thomastanck"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("science", "tech")),
            new Entry(
                new Title("Rayner - the new CEO of Amazon"),
                new Comment("Says \"I couldn't have done it without CS2103!\""),
                new Link("https://github.com/rlrh"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("nus", "soc", "amazon"))
        };
    }

    public static ReadOnlyEntryBook getSampleAddressBook() {
        EntryBook sampleAb = new EntryBook();
        for (Entry sampleEntry : getSamplePersons()) {
            sampleAb.addPerson(sampleEntry);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
