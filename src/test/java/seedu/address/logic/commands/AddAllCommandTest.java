package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ENTRIES;

import org.jetbrains.annotations.NotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;

public class AddAllCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allEntriesAddedSuccessfully() throws Exception {
        Model model = makeTypicalModelInSearchContext();

        CommandResult commandResult = new AddAllCommand().execute(model, commandHistory);

        assertEquals(
            String.format(AddAllCommand.MESSAGE_SUCCESS, model.getFilteredEntryList().size()),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .allMatch(model::hasEntry)
        );

        // Executing the command again results in no entries added because they are all duplicates
        commandResult = new AddAllCommand().execute(model, commandHistory);

        assertEquals(
            String.format(AddAllCommand.MESSAGE_SUCCESS, 0),
            commandResult.getFeedbackToUser());
        assertTrue(
            model.getFilteredEntryList().stream()
                .allMatch(model::hasEntry)
        );
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

}
