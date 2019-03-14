package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_newPerson_success() {
        Entry validEntry = new EntryBuilder().build();

        Model expectedModel = model.clone();
        expectedModel.addEntry(validEntry);

        assertCommandSuccess(new AddCommand(validEntry), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validEntry), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Entry entryInList = model.getEntryBook().getEntryList().get(0);
        assertCommandFailure(new AddCommand(entryInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
