package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;

/**
 * An Immutable EntryBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableEntryBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate entry(s).";

    private final List<JsonAdaptedEntry> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableEntryBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableEntryBook(@JsonProperty("persons") List<JsonAdaptedEntry> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyEntryBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableEntryBook}.
     */
    public JsonSerializableEntryBook(ReadOnlyEntryBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedEntry::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code EntryBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public EntryBook toModelType() throws IllegalValueException {
        EntryBook addressBook = new EntryBook();
        for (JsonAdaptedEntry jsonAdaptedEntry : persons) {
            Entry entry = jsonAdaptedEntry.toModelType();
            if (addressBook.hasPerson(entry)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(entry);
        }
        return addressBook;
    }

}
