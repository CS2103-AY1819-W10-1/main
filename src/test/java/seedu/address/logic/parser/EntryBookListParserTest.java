package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.STYLE_DESC_DARK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_VIEWTYPE_BROWSER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_VIEWTYPE_READER;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ArchiveAllCommand;
import seedu.address.logic.commands.ArchiveCommand;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.BingWebSearchCommand;
import seedu.address.logic.commands.ClearListCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FeedCommand;
import seedu.address.logic.commands.FeedsCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindCommand.FindEntryDescriptor;
import seedu.address.logic.commands.GoogleNewsCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RefreshEntryCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SubscribeCommand;
import seedu.address.logic.commands.ViewModeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ModelContext;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.EntryContainsSearchTermsPredicate;
import seedu.address.testutil.EditEntryDescriptorBuilder;
import seedu.address.testutil.EntryBuilder;
import seedu.address.testutil.EntryUtil;
import seedu.address.testutil.FindEntryDescriptorBuilder;
import seedu.address.ui.ReaderViewStyle;
import seedu.address.ui.ViewMode;
import seedu.address.ui.ViewType;

public class EntryBookListParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EntryBookListParser parser = new EntryBookListParser();

    @Test
    public void parseCommand_add() throws Exception {
        Entry entry = new EntryBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(EntryUtil.getAddCommand(entry));
        assertEquals(new AddCommand(entry), command);
        AddCommand aliasCommand = (AddCommand) parser.parseCommand(EntryUtil.getAddAliasCommand(entry));
        assertEquals(new AddCommand(entry), aliasCommand);
    }

    @Test
    public void parseCommand_archive() throws Exception {
        ArchiveCommand command = (ArchiveCommand) parser.parseCommand(
            ArchiveCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new ArchiveCommand(INDEX_FIRST_ENTRY), command);
        command = (ArchiveCommand) parser.parseCommand(
            ArchiveCommand.COMMAND_ALIAS + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new ArchiveCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_archiveall() throws Exception {
        ArchiveAllCommand command = (ArchiveAllCommand) parser.parseCommand(
            ArchiveAllCommand.COMMAND_WORD);
        assertEquals(new ArchiveAllCommand(), command);
        command = (ArchiveAllCommand) parser.parseCommand(
            ArchiveAllCommand.COMMAND_ALIAS);
        assertEquals(new ArchiveAllCommand(), command);
    }

    @Test
    public void parseCommand_archives() throws Exception {
        assertTrue(parser.parseCommand(ArchivesCommand.COMMAND_WORD) instanceof ArchivesCommand);
        assertTrue(parser.parseCommand(ArchivesCommand.COMMAND_WORD + " 3") instanceof ArchivesCommand);
        assertTrue(parser.parseCommand(ArchivesCommand.COMMAND_ALIAS) instanceof ArchivesCommand);
        assertTrue(parser.parseCommand(ArchivesCommand.COMMAND_ALIAS + " 3") instanceof ArchivesCommand);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearListCommand.COMMAND_WORD) instanceof ClearListCommand);
        assertTrue(parser.parseCommand(ClearListCommand.COMMAND_WORD + " 3") instanceof ClearListCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Entry entry = new EntryBuilder().build();
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(entry).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_ENTRY.getOneBased() + " " + EntryUtil.getEditEntryDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_ENTRY, descriptor), command);
        EditCommand aliasCommand = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_ENTRY.getOneBased() + " " + EntryUtil.getEditEntryDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_ENTRY, descriptor), aliasCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_feed() throws Exception {
        assertTrue(parser.parseCommand(FeedCommand.COMMAND_WORD + " https://url.com/a.xml") instanceof FeedCommand);
    }

    @Test
    public void parseCommand_bing() throws Exception {
        assertTrue(parser.parseCommand(BingWebSearchCommand.COMMAND_WORD + " somekeyword")
                instanceof BingWebSearchCommand);
    }

    @Test
    public void parseCommand_googlenews() throws Exception {
        assertTrue(parser.parseCommand(GoogleNewsCommand.COMMAND_WORD) instanceof GoogleNewsCommand);
        assertTrue(parser.parseCommand(GoogleNewsCommand.COMMAND_ALIAS) instanceof GoogleNewsCommand);
        assertTrue(parser.parseCommand(GoogleNewsCommand.COMMAND_WORD + " kw") instanceof GoogleNewsCommand);
        assertTrue(parser.parseCommand(GoogleNewsCommand.COMMAND_ALIAS + " kw") instanceof GoogleNewsCommand);
    }

    @Test
    public void parseCommand_feeds() throws Exception {
        assertTrue(parser.parseCommand(FeedsCommand.COMMAND_WORD) instanceof FeedsCommand);
        assertTrue(parser.parseCommand(FeedsCommand.COMMAND_WORD + " 3") instanceof FeedsCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        Entry entry = new EntryBuilder().build();
        FindEntryDescriptor descriptor = new FindEntryDescriptorBuilder(entry).build();

        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + EntryUtil.getFindEntryDescriptorDetails(descriptor));
        assertEquals(new FindCommand(new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptor(descriptor))), command);

        FindCommand aliasCommand = (FindCommand) parser.parseCommand(
            FindCommand.COMMAND_ALIAS + " " + EntryUtil.getFindEntryDescriptorDetails(descriptor));
        assertEquals(new FindCommand(new EntryContainsSearchTermsPredicate(
            new FindEntryDescriptor(descriptor))), aliasCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST), pe.getMessage());
        }
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_refresh() throws Exception {
        RefreshEntryCommand command = (RefreshEntryCommand) parser.parseCommand(
            RefreshEntryCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new RefreshEntryCommand(INDEX_FIRST_ENTRY), command);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_ENTRY), command);
        SelectCommand aliasCommand = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_ENTRY.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_ENTRY), aliasCommand);
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
    public void parseCommand_subscribe() throws Exception {
        Entry entry = new EntryBuilder().build();
        SubscribeCommand command = (SubscribeCommand) parser.parseCommand(
            SubscribeCommand.COMMAND_WORD + " " + EntryUtil.getEntryDetails(entry));
        assertEquals(new SubscribeCommand(entry), command);
        SubscribeCommand commandAlias = (SubscribeCommand) parser.parseCommand(
            SubscribeCommand.COMMAND_ALIAS + " " + EntryUtil.getEntryDetails(entry));
        assertEquals(new SubscribeCommand(entry), commandAlias);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST));
        parser.parseCommand("unknownCommand");
    }

    @Test
    public void parseCommand_unsubscribeCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST));
        parser.parseCommand("unsubscribe 9");
    }

    @Test
    public void parseCommand_unarchiveCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST));
        parser.parseCommand("unarchive 9");
    }
}
