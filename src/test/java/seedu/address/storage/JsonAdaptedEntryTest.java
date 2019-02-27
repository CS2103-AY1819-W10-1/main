package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.JsonAdaptedEntry.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalEntries.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.testutil.Assert;

public class JsonAdaptedEntryTest {
    private static final String INVALID_TITLE = " ";
    private static final String INVALID_COMMENT = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_LINK = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_TITLE = BENSON.getTitle().toString();
    private static final String VALID_COMMENT = BENSON.getComment().toString();
    private static final String VALID_LINK = BENSON.getLink().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedEntry person = new JsonAdaptedEntry(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedEntry person =
                new JsonAdaptedEntry(INVALID_TITLE, VALID_COMMENT, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Title.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedEntry person = new JsonAdaptedEntry(null, VALID_COMMENT, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Title.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedEntry person =
                new JsonAdaptedEntry(VALID_TITLE, INVALID_COMMENT, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Comment.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedEntry person = new JsonAdaptedEntry(VALID_TITLE, null, VALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Comment.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedEntry person =
                new JsonAdaptedEntry(VALID_TITLE, VALID_COMMENT, INVALID_LINK, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Link.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedEntry person = new JsonAdaptedEntry(VALID_TITLE, VALID_COMMENT, null, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Link.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedEntry person =
                new JsonAdaptedEntry(VALID_TITLE, VALID_COMMENT, VALID_LINK, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedEntry person = new JsonAdaptedEntry(VALID_TITLE, VALID_COMMENT, VALID_LINK, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedEntry person =
                new JsonAdaptedEntry(VALID_TITLE, VALID_COMMENT, VALID_LINK, VALID_ADDRESS, invalidTags);
        Assert.assertThrows(IllegalValueException.class, person::toModelType);
    }

}
