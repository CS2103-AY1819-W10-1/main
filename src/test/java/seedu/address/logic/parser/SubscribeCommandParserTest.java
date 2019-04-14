package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LINK;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LINK_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_TECH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_TECH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_BOB;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalEntries.AMY;
import static seedu.address.testutil.TypicalEntries.BOB;

import org.junit.Test;

import seedu.address.logic.commands.SubscribeCommand;
import seedu.address.model.entry.Description;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryBuilder;

public class SubscribeCommandParserTest {
    private SubscribeCommandParser parser = new SubscribeCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Entry expectedEntry = new EntryBuilder(BOB).withTags(VALID_TAG_TECH).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + LINK_DESC_BOB
                + TAG_DESC_TECH, new SubscribeCommand(expectedEntry));

        // multiple titles - last title accepted
        assertParseSuccess(parser, TITLE_DESC_AMY + TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + LINK_DESC_BOB
                + TAG_DESC_TECH, new SubscribeCommand(expectedEntry));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, TITLE_DESC_BOB + DESCRIPTION_DESC_AMY + DESCRIPTION_DESC_BOB + LINK_DESC_BOB
                + TAG_DESC_TECH, new SubscribeCommand(expectedEntry));

        // multiple links - last link accepted
        assertParseSuccess(parser, TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + LINK_DESC_AMY + LINK_DESC_BOB
                + TAG_DESC_TECH, new SubscribeCommand(expectedEntry));

        // multiple tags - all accepted
        Entry expectedEntryMultipleTags = new EntryBuilder(BOB).withTags(VALID_TAG_TECH, VALID_TAG_SCIENCE)
                .build();
        assertParseSuccess(parser, TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + LINK_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, new SubscribeCommand(expectedEntryMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Entry expectedEntry = new EntryBuilder(AMY).withTags().build();
        assertParseSuccess(parser, TITLE_DESC_AMY + DESCRIPTION_DESC_AMY + LINK_DESC_AMY,
                new SubscribeCommand(expectedEntry));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SubscribeCommand.MESSAGE_USAGE);

        // missing link prefix
        assertParseFailure(parser, TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + VALID_LINK_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_TITLE_BOB + VALID_DESCRIPTION_BOB + VALID_LINK_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid title
        assertParseFailure(parser, INVALID_TITLE_DESC + DESCRIPTION_DESC_BOB + LINK_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, Title.formExceptionMessage(INVALID_TITLE.trim()));

        // invalid description
        assertParseFailure(parser, TITLE_DESC_BOB + INVALID_DESCRIPTION_DESC + LINK_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, Description.formExceptionMessage(INVALID_DESCRIPTION.trim()));

        // invalid link
        assertParseFailure(parser, TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + INVALID_LINK_DESC
                + TAG_DESC_SCIENCE + TAG_DESC_TECH, Link.formExceptionMessage(INVALID_LINK.trim()));

        // invalid tag
        assertParseFailure(parser, TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + LINK_DESC_BOB
                + INVALID_TAG_DESC, Tag.formExceptionMessage(INVALID_TAG.trim()));

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_TITLE_DESC + DESCRIPTION_DESC_BOB + LINK_DESC_BOB,
                Title.formExceptionMessage(INVALID_TITLE.trim()));

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + TITLE_DESC_BOB + DESCRIPTION_DESC_BOB + LINK_DESC_BOB
                + TAG_DESC_SCIENCE + TAG_DESC_TECH,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SubscribeCommand.MESSAGE_USAGE));
    }
}
