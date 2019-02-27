package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.COMMENT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.COMMENT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_COMMENT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LINK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TECH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COMMENT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalEntries.AMY;
import static seedu.address.testutil.TypicalEntries.BOB;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Entry expectedEntry = new EntryBuilder(BOB).withTags(VALID_TAG_TECH).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH, new AddCommand(expectedEntry));

        // multiple names - last name accepted
        assertParseSuccess(parser, TITLE_DESC_AMY + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH, new AddCommand(expectedEntry));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, TITLE_DESC_BOB + COMMENT_DESC_AMY + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH, new AddCommand(expectedEntry));

        // multiple emails - last email accepted
        assertParseSuccess(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_AMY + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_TECH, new AddCommand(expectedEntry));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_TECH, new AddCommand(expectedEntry));

        // multiple tags - all accepted
        Entry expectedEntryMultipleTags = new EntryBuilder(BOB).withTags(VALID_TAG_TECH, VALID_TAG_SCIENCE)
                .build();
        assertParseSuccess(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, new AddCommand(expectedEntryMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Entry expectedEntry = new EntryBuilder(AMY).withTags().build();
        assertParseSuccess(parser, TITLE_DESC_AMY + COMMENT_DESC_AMY + LINK_DESC_AMY + ADDRESS_DESC_AMY,
                new AddCommand(expectedEntry));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_TITLE_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, TITLE_DESC_BOB + VALID_COMMENT_BOB + LINK_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + VALID_LINK_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // No longer valid with the optionality of the (invisible) address field
        /*
        // missing address prefix
        assertParseFailure(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
                */

        // all prefixes missing
        assertParseFailure(parser, VALID_TITLE_BOB + VALID_COMMENT_BOB + VALID_LINK_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_TITLE_DESC + COMMENT_DESC_BOB + LINK_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, Title.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, TITLE_DESC_BOB + INVALID_COMMENT_DESC + LINK_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, Comment.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + INVALID_LINK_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, Link.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_TECH, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_TITLE_DESC + COMMENT_DESC_BOB + LINK_DESC_BOB + INVALID_ADDRESS_DESC,
                Title.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + TITLE_DESC_BOB + COMMENT_DESC_BOB + LINK_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_SCIENCE + TAG_DESC_TECH,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
