package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.parser.exceptions.ParseException;
// No longer valid with the optionality of the (invisible) address field
// import seedu.address.model.entry.Address;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.Assert;

public class ParserUtilTest {
    private static final String INVALID_TITLE = " ";
    private static final String INVALID_DESCRIPTION = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_LINK = "example.com"; // No protocol
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_TITLE = "Rachel Walker voted prettiest girl on earth!";
    private static final String VALID_COMMENT = "But is she really a girl?";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_LINK = "https://rachel.example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_invalidInputNonNumeric_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseIndex("abc");
    }

    @Test
    public void parseIndex_invalidInputOnlyDecimal_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseIndex("0x1");
    }

    @Test
    public void parseIndex_outOfRangeInputTooBig_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(((long) Integer.MAX_VALUE) + 1));
    }

    @Test
    public void parseIndex_outOfRangeInputNegative_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex("-1");
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_ENTRY, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_ENTRY, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseTitle_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseTitle((Optional<String>) null));
    }

    @Test
    public void parseTitle_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseTitle(Optional.of(INVALID_TITLE)));
    }

    @Test
    public void parseTitle_emptyValue_returnsDefaultTitle() throws Exception {
        Title expectedTitle = new Title(Title.DEFAULT_TITLE);
        assertEquals(expectedTitle, ParserUtil.parseTitle(Optional.empty()));
    }

    @Test
    public void parseTitle_validValueWithoutWhitespace_returnsTitle() throws Exception {
        Title expectedTitle = new Title(VALID_TITLE);
        assertEquals(expectedTitle, ParserUtil.parseTitle(Optional.of(VALID_TITLE)));
    }

    @Test
    public void parseTitle_validValueWithWhitespace_returnsTrimmedTitle() throws Exception {
        String titleWithWhitespace = WHITESPACE + VALID_TITLE + WHITESPACE;
        Title expectedTitle = new Title(VALID_TITLE);
        assertEquals(expectedTitle, ParserUtil.parseTitle(Optional.of(titleWithWhitespace)));
    }

    @Test
    public void parseDescription_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseDescription((Optional<String>) null));
    }

    @Test
    public void parseDescription_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseDescription(Optional.of(INVALID_DESCRIPTION)));
    }

    @Test
    public void parseDescription_emptyValue_returnsDefaultDescription() throws Exception {
        Description expectedDescription = new Description(Description.DEFAULT_DESCRIPTION);
        assertEquals(expectedDescription, ParserUtil.parseDescription(Optional.empty()));
    }

    @Test
    public void parseDescription_validValueWithoutWhitespace_returnsDescription() throws Exception {
        Description expectedDescription = new Description(VALID_COMMENT);
        assertEquals(expectedDescription, ParserUtil.parseDescription(Optional.of(VALID_COMMENT)));
    }

    @Test
    public void parseDescription_validValueWithWhitespace_returnsTrimmedDescription() throws Exception {
        String descriptionWithWhitespace = WHITESPACE + VALID_COMMENT + WHITESPACE;
        Description expectedDescription = new Description(VALID_COMMENT);
        assertEquals(expectedDescription, ParserUtil.parseDescription(Optional.of(descriptionWithWhitespace)));
    }

    @Test
    public void parseLink_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> ParserUtil.parseLink((Optional<String>) null));
    }

    @Test
    public void parseLink_invalidValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseLink(Optional.of(INVALID_LINK)));
    }

    @Test
    public void parseLink_emptyValue_throwsParseException() {
        Assert.assertThrows(ParseException.class, () -> ParserUtil.parseLink(Optional.empty()));
    }

    @Test
    public void parseLink_validValueWithoutWhitespace_returnsLink() throws Exception {
        Link expectedLink = new Link(VALID_LINK);
        assertEquals(expectedLink, ParserUtil.parseLink(Optional.of(VALID_LINK)));
    }

    @Test
    public void parseLink_validValueWithWhitespace_returnsTrimmedLink() throws Exception {
        String linkWithWhitespace = WHITESPACE + VALID_LINK + WHITESPACE;
        Link expectedLink = new Link(VALID_LINK);
        assertEquals(expectedLink, ParserUtil.parseLink(Optional.of(linkWithWhitespace)));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTag(null);
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseTag(INVALID_TAG);
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTags(null);
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
