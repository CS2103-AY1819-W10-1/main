package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Entry}.
 */
class JsonAdaptedEntry {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Entry's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedEntry} with the given entry details.
     */
    @JsonCreator
    public JsonAdaptedEntry(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                            @JsonProperty("email") String email, @JsonProperty("address") String address,
                            @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Entry} into this class for Jackson use.
     */
    public JsonAdaptedEntry(Entry source) {
        name = source.getTitle().fullTitle;
        phone = source.getComment().value;
        email = source.getLink().value;
        address = source.getAddress().value;
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted entry object into the model's {@code Entry} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted entry.
     */
    public Entry toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Title.class.getSimpleName()));
        }
        if (!Title.isValidTitle(name)) {
            throw new IllegalValueException(Title.MESSAGE_CONSTRAINTS);
        }
        final Title modelTitle = new Title(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Comment.class.getSimpleName()));
        }
        if (!Comment.isValidComment(phone)) {
            throw new IllegalValueException(Comment.MESSAGE_CONSTRAINTS);
        }
        final Comment modelComment = new Comment(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Link.class.getSimpleName()));
        }
        if (!Link.isValidLink(email)) {
            throw new IllegalValueException(Link.MESSAGE_CONSTRAINTS);
        }
        final Link modelLink = new Link(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Entry(modelTitle, modelComment, modelLink, modelAddress, modelTags);
    }

}
