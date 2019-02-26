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
            new Entry(new Title("Alex Yeoh"), new Comment("87438807"), new Link("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("friends")),
            new Entry(new Title("Bernice Yu"), new Comment("99272758"), new Link("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("colleagues", "friends")),
            new Entry(new Title("Charlotte Oliveiro"), new Comment("93210283"), new Link("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("neighbours")),
            new Entry(new Title("David Li"), new Comment("91031282"), new Link("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("family")),
            new Entry(new Title("Irfan Ibrahim"), new Comment("92492021"), new Link("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("classmates")),
            new Entry(new Title("Roy Balakrishnan"), new Comment("92624417"), new Link("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("colleagues"))
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
