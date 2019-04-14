package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.STYLE_DESC_DARK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_VIEWTYPE_BROWSER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_VIEWTYPE_READER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddAllCommand;
import seedu.address.logic.commands.AddIndexCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;
import seedu.address.ui.ReaderViewStyle;
import seedu.address.ui.ViewMode;
import seedu.address.ui.ViewType;

public class EntryBookSearchParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EntryBookSearchParser parser = new EntryBookSearchParser();

    @Test
    public void parseCommand_emptyString_throwsParseException() throws ParseException {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_addindex() throws Exception {
        AddIndexCommand command = (AddIndexCommand) parser.parseCommand(
                AddIndexCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new AddIndexCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_addindex_alias() throws Exception {
        AddIndexCommand command = (AddIndexCommand) parser.parseCommand(
                AddIndexCommand.COMMAND_ALIAS + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new AddIndexCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_addall() throws Exception {
        AddAllCommand command = (AddAllCommand) parser.parseCommand(
            AddAllCommand.COMMAND_WORD);
        assertEquals(new AddAllCommand(), command);
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
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_view() throws Exception {
        ViewModeCommand command = (ViewModeCommand) parser.parseCommand(
            ViewModeCommand.COMMAND_WORD + " " + VALID_VIEWTYPE_BROWSER);
        assertEquals(new ViewModeCommand(new ViewMode(ViewType.BROWSER)), command);
        ViewModeCommand aliasCommand = (ViewModeCommand) parser.parseCommand(
            ViewModeCommand.COMMAND_ALIAS + " " + VALID_VIEWTYPE_READER + STYLE_DESC_DARK);
        assertEquals(new ViewModeCommand(new ViewMode(ViewType.READER, ReaderViewStyle.DARK)), aliasCommand);
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_SEARCH));
        parser.parseCommand("unknownCommand");
    }

    @Test
    public void parseCommand_otherContextCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_SEARCH));
        parser.parseCommand("unsubscribe 9");
    }

    @Test
    public void parseCommand_unarchiveCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_SEARCH));
        parser.parseCommand("unarchive 9");
    }
}
