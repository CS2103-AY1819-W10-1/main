package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ENTRIES;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.entry.Entry;

public class AddIndexCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddIndexCommand(null);
    }

    @Test
    public void execute_entryAcceptedByModel_addSuccessful() throws Exception {
        Model model = makeTypicalModelInSearchContext();

        assertCommandSuccess(model, getFirstIndex(model));
        assertCommandSuccess(model, getMidIndex(model));
        assertCommandSuccess(model, getLastIndex(model));
    }

    /**
     * Creates a new model in the search context with pre-populated entries
     */
    @NotNull
    private Model makeTypicalModelInSearchContext() {
        Model model = new TypicalModelManagerStub();

        // We need a search entry book for testing purposes
        model.setSearchEntryBook(model.getArchivesEntryBook());
        model.setContext(ModelContext.CONTEXT_SEARCH);
        model.updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);
        return model;
    }

    @Test
    public void execute_indexOutOfBounds_throwsCommandException() throws Exception {
        Model model = makeTypicalModelInSearchContext();

        assertCommandFailure(model, getLastPlusOneIndex(model));
    }


    @Test
    public void equals() {
        Index one = Index.fromOneBased(1);
        Index two = Index.fromOneBased(2);
        AddIndexCommand addAtOneCommand = new AddIndexCommand(one);
        AddIndexCommand addAtTwoCommand = new AddIndexCommand(two);

        // same object -> returns true
        assertEquals(addAtOneCommand, addAtOneCommand);

        // same values -> returns true
        AddIndexCommand addAtOneCommandCopy = new AddIndexCommand(one);
        assertEquals(addAtOneCommand, addAtOneCommandCopy);

        // different types -> returns false
        assertNotEquals(1, addAtOneCommand);

        // null -> returns false
        assertNotEquals(null, addAtOneCommand);

        // different entry -> returns false
        assertNotEquals(addAtOneCommand, addAtTwoCommand);
    }

    /**
     * Asserts that the addindex command is successfully run at the specified index
     */
    private void assertCommandSuccess(Model model, Index index) throws CommandException {
        Command command = new AddIndexCommand(index);
        Entry entryThatShouldBeAdded = model.getFilteredEntryList().get(index.getZeroBased());
        CommandResult commandResult = command.execute(model, commandHistory);

        assertEquals(
            String.format(AddCommand.MESSAGE_SUCCESS, entryThatShouldBeAdded),
            commandResult.getFeedbackToUser());
        assertTrue(model.getListEntryBook().getEntryList().contains(entryThatShouldBeAdded));
    }

    /**
     * Asserts that the addindex command is throws at the specified index
     */
    private void assertCommandFailure(Model model, Index index) throws CommandException {
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        Command command = new AddIndexCommand(index);
        command.execute(model, commandHistory);
    }

    private Index getFirstIndex(Model model) {
        assertTrue(model.getFilteredEntryList().size() > 0);
        return Index.fromOneBased(1);
    }

    private Index getMidIndex(Model model) {
        assertTrue(model.getFilteredEntryList().size() > 0);
        return Index.fromOneBased((model.getFilteredEntryList().size() + 1) / 2);
    }

    private Index getLastIndex(Model model) {
        assertTrue(model.getFilteredEntryList().size() > 0);
        return Index.fromOneBased(model.getFilteredEntryList().size());
    }

    private Index getLastPlusOneIndex(Model model) {
        assertTrue(model.getFilteredEntryList().size() > 0);
        return Index.fromOneBased(model.getFilteredEntryList().size() + 1);
    }

}
