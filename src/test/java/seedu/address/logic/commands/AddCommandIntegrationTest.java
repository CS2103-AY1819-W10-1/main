package seedu.address.logic.commands;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import static seedu.address.testutil.TypicalEntries.ENTRY_WITH_ABSOLUTE_LINK;
import static seedu.address.testutil.TypicalEntries.ENTRY_WITH_RELATIVE_LINK;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_DESCRIPTION_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_INCOMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE;
import static seedu.address.testutil.TypicalEntries.READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.DuplicateEntryCommandException;
import seedu.address.mocks.TemporaryStorageManager;
import seedu.address.mocks.TypicalModelManagerStub;
import seedu.address.model.Model;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Model model = new TypicalModelManagerStub();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_newEntry_success() {
        Entry validEntry = new EntryBuilder().build();

        Model expectedModel = model.clone();
        expectedModel.addListEntry(validEntry, Optional.empty());

        assertCommandSuccess(new AddCommand(validEntry), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validEntry), expectedModel);
    }

    @Test
    public void execute_newEntryHasNoTitleAndNoDescription_titleAndDescriptionReplacedSuccess() {
        Model expectedModel = model.clone();
        expectedModel.addListEntry(READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE, Optional.empty());
        assertCommandSuccess(new AddCommand(READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_INCOMPLETE),
                model,
                commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, READABILITY_LINK_NO_TITLE_NO_DESCRIPTION_COMPLETE),
                expectedModel);
    }

    @Test
    public void execute_newEntryHasNoTitle_titleReplacedSuccess() {
        Model expectedModel = model.clone();
        expectedModel.addListEntry(READABILITY_LINK_NO_TITLE_COMPLETE, Optional.empty());
        assertCommandSuccess(new AddCommand(READABILITY_LINK_NO_TITLE_INCOMPLETE), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, READABILITY_LINK_NO_TITLE_COMPLETE), expectedModel);
    }

    @Test
    public void execute_newEntryHasNoDescription_descriptionReplacedSuccess() {
        Model expectedModel = model.clone();
        expectedModel.addListEntry(READABILITY_LINK_NO_DESCRIPTION_COMPLETE, Optional.empty());
        assertCommandSuccess(new AddCommand(READABILITY_LINK_NO_DESCRIPTION_INCOMPLETE), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, READABILITY_LINK_NO_DESCRIPTION_COMPLETE), expectedModel);
    }

    @Test
    public void execute_duplicateEntry_throwsCommandException() {
        Entry entryInList = model.getListEntryBook().getEntryList().get(0);
        assertCommandFailure(new AddCommand(entryInList), model, commandHistory,
                DuplicateEntryCommandException.MESSAGE_DUPLICATE_ENTRY);
    }

    @Test
    public void execute_linksAreConverted() throws CommandException, IOException {

        // We need to make model have a Storage for this test
        model = new TypicalModelManagerStub(new TemporaryStorageManager(temporaryFolder));

        new AddCommand(ENTRY_WITH_RELATIVE_LINK).execute(model, commandHistory);
        Path relativeArticlePath = model.getStorage().getArticlePath(ENTRY_WITH_RELATIVE_LINK.getLink().value);
        byte[] relativeArticleContent = Files.readAllBytes(relativeArticlePath);

        new AddCommand(ENTRY_WITH_ABSOLUTE_LINK).execute(model, commandHistory);
        Path absoluteArticlePath = model.getStorage().getArticlePath(ENTRY_WITH_ABSOLUTE_LINK.getLink().value);
        byte[] absoluteArticleContent = Files.readAllBytes(absoluteArticlePath);

        assertTrue(relativeArticleContent.length > 0);
        assertTrue(absoluteArticleContent.length > 0);
        assertArrayEquals(relativeArticleContent, absoluteArticleContent);
    }

}
