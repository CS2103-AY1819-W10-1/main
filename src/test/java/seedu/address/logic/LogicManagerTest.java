package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.LINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_AMY;
import static seedu.address.testutil.TypicalEntries.AMY;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FeedsCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.mocks.TemporaryStorageManager;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.entry.Entry;
import seedu.address.storage.ArticleStorage;
import seedu.address.storage.DataDirectoryArticleStorage;
import seedu.address.storage.JsonEntryBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.EntryBuilder;


public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    @Before
    public void setUp() throws Exception {
        StorageManager storage = new TemporaryStorageManager(temporaryFolder);
        model = new ModelManager(new EntryBook(), new EntryBook(), new EntryBook(), new UserPrefs(), storage);
        logic = new LogicManager(model);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, String.format(MESSAGE_UNKNOWN_COMMAND, ModelContext.CONTEXT_LIST));
        assertHistoryCorrect(invalidCommand);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        assertHistoryCorrect(deleteCommand);
    }

    @Test
    public void execute_validCommand_success() {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
        assertHistoryCorrect(listCommand);
    }

    @Test
    public void execute_validAliasCommand_success() {
        String listCommand = ListCommand.COMMAND_ALIAS;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
        assertHistoryCorrect(listCommand);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() throws Exception {
        // Setup LogicManager with JsonEntryBookIoExceptionThrowingStub
        JsonEntryBookStorage listEntryBookStorage =
                new JsonEntryBookIoExceptionThrowingStub(temporaryFolder.newFile().toPath());
        JsonEntryBookStorage archivesEntryBookStorage =
                new JsonEntryBookIoExceptionThrowingStub(temporaryFolder.newFile().toPath());
        JsonEntryBookStorage feedsEntryBookStorage =
                new JsonEntryBookIoExceptionThrowingStub(temporaryFolder.newFile().toPath());
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.newFile().toPath());
        ArticleStorage articleStorage = new DataDirectoryArticleStorage(temporaryFolder.newFolder().toPath());
        StorageManager storage = new StorageManager(listEntryBookStorage, archivesEntryBookStorage,
                feedsEntryBookStorage, userPrefsStorage, articleStorage);
        model = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(), model.getFeedsEntryBook(),
                model.getUserPrefs(), storage);
        logic = new LogicManager(model);

        // Execute add command
        String addCommand = AddCommand.COMMAND_WORD + TITLE_DESC_AMY + DESCRIPTION_DESC_AMY + LINK_DESC_AMY;
        Entry expectedEntry = new EntryBuilder(AMY).withTags().build();
        Model expectedModel = new ModelManagerStub();
        String expectedInitialMessage = String.format(AddCommand.MESSAGE_SUCCESS, expectedEntry);
        String expectedFinalMessage = ModelManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        expectedModel.addListEntry(expectedEntry, Optional.empty());
        expectedModel.setException(new CommandException(expectedFinalMessage));
        assertCommandSuccess(addCommand, expectedInitialMessage, expectedModel);
        assertManualExceptionPropagated(CommandException.class, expectedFinalMessage);
        assertHistoryCorrect(addCommand);
    }

    @Test
    public void execute_manualCommandResultSet_success() {
        String expectedMessage = "Command result successfully set manually";
        CommandResult result = new CommandResult(expectedMessage);
        logic.setCommandResult(result);
        assertManualCommandResultSet(expectedMessage);
    }

    @Test
    public void execute_manualExceptionPropagated_failure() {
        String expectedMessage = "Exception successfully set manually";
        Exception exception = new Exception(expectedMessage);
        logic.setException(exception);
        assertManualExceptionPropagated(Exception.class, expectedMessage);
    }

    @Test
    public void getFilteredEntryList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        logic.getFilteredEntryList().remove(0);
    }

    @Test
    public void executeContextSwitch_success() {
        logic.executeContextSwitch(ModelContext.CONTEXT_LIST);
        assertEquals(ListCommand.MESSAGE_SUCCESS, model.getCommandResult().getFeedbackToUser());
        assertEquals(model.getContext(), ModelContext.CONTEXT_LIST);

        logic.executeContextSwitch(ModelContext.CONTEXT_ARCHIVES);
        assertEquals(ArchivesCommand.MESSAGE_SUCCESS, model.getCommandResult().getFeedbackToUser());
        assertEquals(model.getContext(), ModelContext.CONTEXT_ARCHIVES);

        logic.executeContextSwitch(ModelContext.CONTEXT_FEEDS);
        assertEquals(FeedsCommand.MESSAGE_SUCCESS, model.getCommandResult().getFeedbackToUser());
        assertEquals(model.getContext(), ModelContext.CONTEXT_FEEDS);

        // Test switching back to list context in case first one wasn't an accurate test
        logic.executeContextSwitch(ModelContext.CONTEXT_LIST);
        assertEquals(ListCommand.MESSAGE_SUCCESS, model.getCommandResult().getFeedbackToUser());
        assertEquals(model.getContext(), ModelContext.CONTEXT_LIST);
    }

    /**
     * Tests switching to search context,
     * which does network calls (google news command),
     * so we test this separately from above.
     */
    @Test
    public void executeContextSwitchToSearchContext_success() {
        logic.executeContextSwitch(ModelContext.CONTEXT_LIST);
        assertEquals(ListCommand.MESSAGE_SUCCESS, model.getCommandResult().getFeedbackToUser());
        assertEquals(model.getContext(), ModelContext.CONTEXT_LIST);

        logic.executeContextSwitch(ModelContext.CONTEXT_SEARCH);
        assertEquals(model.getContext(), ModelContext.CONTEXT_SEARCH);

        // Test switching back to list context in case first one wasn't an accurate test
        logic.executeContextSwitch(ModelContext.CONTEXT_LIST);
        assertEquals(ListCommand.MESSAGE_SUCCESS, model.getCommandResult().getFeedbackToUser());
        assertEquals(model.getContext(), ModelContext.CONTEXT_LIST);
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the result message is correct.
     * Also confirms that {@code expectedModel} is as specified.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<?> expectedException, String expectedMessage) {
        Model expectedModel = new ModelManager(model.getListEntryBook(), model.getArchivesEntryBook(),
            model.getFeedsEntryBook(), model.getUserPrefs(), model.getStorage());
        assertCommandBehavior(expectedException, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and that the expected exception is thrown,
     * and also confirms that the following two parts of the LogicManager object's state are as expected:<br>
     *      - the internal model manager data are same as those in the {@code expectedModel} <br>
     *      - {@code expectedModel}'s entry book was saved to the storage file.
     */
    private void assertCommandBehavior(Class<?> expectedException, String inputCommand,
                                           String expectedMessage, Model expectedModel) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertEquals(expectedException, null);
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } catch (CommandException | ParseException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedModel, model);
    }

    /**
     * For manually set command result, confirms that the result message is correct.
     * Exception can be propagated from previous commands so it cannot be checked.
     */
    private void assertManualCommandResultSet(String expectedMessage) {
        assertNotNull(model.getCommandResult());
        assertEquals(expectedMessage, model.getCommandResult().getFeedbackToUser());
    }

    /**
     * For manually set exception, confirms that the expected exception is propagated.
     * Command result can be propagated from previous commands so it cannot be checked.
     */
    private void assertManualExceptionPropagated(Class<?> expectedException, String expectedMessage) {
        assertNotNull(model.getException());
        assertEquals(expectedException, model.getException().getClass());
        assertEquals(expectedMessage, model.getException().getMessage());
    }

    /**
     * Asserts that the result display shows all the {@code expectedCommands} upon the execution of
     * {@code HistoryCommand}.
     */
    private void assertHistoryCorrect(String... expectedCommands) {
        try {
            CommandResult result = logic.execute(HistoryCommand.COMMAND_WORD);
            String expectedMessage = String.format(
                    HistoryCommand.MESSAGE_SUCCESS, String.join("\n", expectedCommands));
            assertEquals(expectedMessage, result.getFeedbackToUser());
        } catch (ParseException | CommandException e) {
            throw new AssertionError("Parsing and execution of HistoryCommand.COMMAND_WORD should succeed.", e);
        }
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonEntryBookIoExceptionThrowingStub extends JsonEntryBookStorage {
        private JsonEntryBookIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveEntryBook(ReadOnlyEntryBook listEntryBook, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
