package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.RefreshFeedCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UnsubscribeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;

public class EntryBookFeedsParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EntryBookFeedsParser parser = new EntryBookFeedsParser();

    @Test
    public void parseCommand_emptyString_throwsParseException() throws ParseException {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_refresh() throws Exception {
        RefreshFeedCommand command = (RefreshFeedCommand) parser.parseCommand(
                RefreshFeedCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new RefreshFeedCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
            SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_select_alias() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
            SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_unsubscribe() throws Exception {
        UnsubscribeCommand command = (UnsubscribeCommand) parser.parseCommand(
                UnsubscribeCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new UnsubscribeCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_unsubscribe_alias() throws Exception {
        UnsubscribeCommand command = (UnsubscribeCommand) parser.parseCommand(
                UnsubscribeCommand.COMMAND_ALIAS + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new UnsubscribeCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_FEEDS));
        parser.parseCommand("unknownCommand");
    }

    @Test
    public void parseCommand_addindexCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_FEEDS));
        parser.parseCommand("addindex 9");
    }

    @Test
    public void parseCommand_unarchiveCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_FEEDS));
        parser.parseCommand("unarchive 9");
    }
}
