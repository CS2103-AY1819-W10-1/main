package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.EntryBook;
import seedu.address.model.Model;

public class ClearArchivesCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyEntryBook_success() {
        Model model = new ModelManagerStub();
        Model expectedModel = new ModelManagerStub();

        assertCommandSuccess(
            new ClearArchivesCommand(),
            model,
            commandHistory,
            ClearArchivesCommand.MESSAGE_SUCCESS,
            expectedModel);
    }

    @Test
    public void execute_nonEmptyEntryBook_success() {
        Model model = new TypicalModelManagerStub();
        Model expectedModel = new TypicalModelManagerStub();
        expectedModel.setArchivesEntryBook(new EntryBook());

        assertCommandSuccess(
            new ClearArchivesCommand(),
            model,
            commandHistory,
            ClearArchivesCommand.MESSAGE_SUCCESS,
            expectedModel);
    }

}
