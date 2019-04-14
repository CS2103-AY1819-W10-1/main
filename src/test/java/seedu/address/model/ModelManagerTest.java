package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LINK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.showEntryAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ENTRIES;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.BENSON;
import static seedu.address.testutil.TypicalEntries.BOB;
import static seedu.address.testutil.TypicalEntries.CARL;
import static seedu.address.testutil.TypicalEntries.DANIEL;
import static seedu.address.testutil.TypicalEntries.KATTIS_FEED_ENTRY;
import static seedu.address.testutil.TypicalEntries.WIKIPEDIA_ENTRY;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ENTRY;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.core.GuiSettings;
import seedu.address.mocks.ModelManagerStub;
import seedu.address.mocks.StorageStub;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.storage.Storage;
import seedu.address.testutil.EntryBookBuilder;
import seedu.address.testutil.EntryBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private ModelManager modelManager = new ModelManagerStub();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new EntryBook(), new EntryBook(modelManager.getListEntryBook()));
        assertEquals(null, modelManager.getSelectedEntry());
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setUserPrefs(null);
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setListEntryBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setArticleDataDirectoryPath(Paths.get("article/data/directory/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setListEntryBookFilePath(Paths.get("new/address/book/file/path"));
        userPrefs.setArticleDataDirectoryPath(Paths.get("new/article/data/directory/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setGuiSettings(null);
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setEntryBookFilePath_nullPath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setListEntryBookFilePath(null);
    }

    @Test
    public void setEntryBookFilePath_validPath_setsEntryBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setListEntryBookFilePath(path);
        assertEquals(path, modelManager.getListEntryBookFilePath());
    }

    @Test
    public void setArticleDataDirectoryPath_nullPath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setArticleDataDirectoryPath(null);
    }

    @Test
    public void setArticleDataDirectoryPath_validPath_setsEntryBookFilePath() {
        Path path = Paths.get("article/data/directory/path");
        modelManager.setArticleDataDirectoryPath(path);
        assertEquals(path, modelManager.getArticleDataDirectoryPath());
    }

    @Test
    public void setArchivesEntryBookFilePath_nullPath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.setArchivesEntryBookFilePath(null);
    }

    @Test
    public void setArchivesEntryBookFilePath_validPath_setsEntryBookFilePath() {
        Path path = Paths.get("archive/book/file/path");
        modelManager.setArchivesEntryBookFilePath(path);
        assertEquals(path, modelManager.getArchivesEntryBookFilePath());
    }

    @Test
    public void hasEntry_nullEntry_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasEntry(null);
    }

    @Test
    public void hasEntry_entryNotInEntryBook_returnsFalse() {
        assertFalse(modelManager.hasEntry(ALICE));
    }

    @Test
    public void hasEntry_entryInEntryBook_returnsTrue() {
        modelManager.addListEntry(ALICE, Optional.empty());
        assertTrue(modelManager.hasEntry(ALICE));

        modelManager.setContext(ModelContext.CONTEXT_ARCHIVES);
        modelManager.addArchivesEntry(ALICE);
        assertTrue(modelManager.hasArchivesEntry(ALICE));

        modelManager.setContext(ModelContext.CONTEXT_FEEDS);
        modelManager.addFeedsEntry(ALICE);
        assertTrue(modelManager.hasFeedsEntry(ALICE));
    }

    @Test
    public void deleteEntry_entryIsSelectedAndFirstEntryInFilteredEntryList_selectionCleared() {
        modelManager.addListEntry(ALICE, Optional.empty());
        modelManager.setSelectedEntry(ALICE);
        modelManager.deleteListEntry(ALICE);
        assertEquals(null, modelManager.getSelectedEntry());

        modelManager.setContext(ModelContext.CONTEXT_ARCHIVES);
        modelManager.addArchivesEntry(ALICE);
        modelManager.setSelectedEntry(ALICE);
        modelManager.deleteArchivesEntry(ALICE);
        assertEquals(null, modelManager.getSelectedEntry());

        modelManager.setContext(ModelContext.CONTEXT_FEEDS);
        modelManager.addFeedsEntry(ALICE);
        modelManager.setSelectedEntry(ALICE);
        modelManager.deleteFeedsEntry(ALICE);
        assertEquals(null, modelManager.getSelectedEntry());
    }

    @Test
    public void deleteEntry_entryIsSelectedAndSecondEntryInFilteredEntryList_firstEntrySelected() {
        modelManager.addListEntry(ALICE, Optional.empty());
        modelManager.addListEntry(BOB, Optional.empty());
        assertEquals(Arrays.asList(ALICE, BOB), modelManager.getFilteredEntryList());
        modelManager.setSelectedEntry(BOB);
        modelManager.deleteListEntry(BOB);
        assertEquals(ALICE, modelManager.getSelectedEntry());

        modelManager.setContext(ModelContext.CONTEXT_ARCHIVES);
        modelManager.addArchivesEntry(ALICE);
        modelManager.addArchivesEntry(BOB);
        assertEquals(Arrays.asList(ALICE, BOB), modelManager.getFilteredEntryList());
        modelManager.setSelectedEntry(BOB);
        modelManager.deleteArchivesEntry(BOB);
        assertEquals(ALICE, modelManager.getSelectedEntry());

        modelManager.setContext(ModelContext.CONTEXT_FEEDS);
        modelManager.addFeedsEntry(ALICE);
        modelManager.addFeedsEntry(BOB);
        assertEquals(Arrays.asList(ALICE, BOB), modelManager.getFilteredEntryList());
        modelManager.setSelectedEntry(BOB);
        modelManager.deleteFeedsEntry(BOB);
        assertEquals(ALICE, modelManager.getSelectedEntry());
    }

    @Test
    public void setEntry_entryIsSelected_selectedEntryUpdated() {
        modelManager.addListEntry(ALICE, Optional.empty());
        modelManager.setSelectedEntry(ALICE);
        Entry updatedAlice = new EntryBuilder(ALICE).withLink(VALID_LINK_BOB).build();
        modelManager.setListEntry(ALICE, updatedAlice);
        assertEquals(updatedAlice, modelManager.getSelectedEntry());
    }

    @Test
    public void getFilteredEntryList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredEntryList().remove(0);
    }

    @Test
    public void setSelectedEntry_entryNotInFilteredEntryList_throwsEntryNotFoundException() {
        thrown.expect(EntryNotFoundException.class);
        modelManager.setSelectedEntry(ALICE);
    }

    @Test
    public void setSelectedEntry_entryInFilteredEntryList_setsSelectedEntry() {
        modelManager.addListEntry(ALICE, Optional.empty());
        assertEquals(Collections.singletonList(ALICE), modelManager.getFilteredEntryList());
        modelManager.setSelectedEntry(ALICE);
        assertEquals(ALICE, modelManager.getSelectedEntry());

        modelManager.setContext(ModelContext.CONTEXT_ARCHIVES);
        modelManager.addArchivesEntry(ALICE);
        assertEquals(Collections.singletonList(ALICE), modelManager.getFilteredEntryList());
        modelManager.setSelectedEntry(ALICE);
        assertEquals(ALICE, modelManager.getSelectedEntry());

        modelManager.setContext(ModelContext.CONTEXT_FEEDS);
        modelManager.addFeedsEntry(ALICE);
        assertEquals(Collections.singletonList(ALICE), modelManager.getFilteredEntryList());
        modelManager.setSelectedEntry(ALICE);
        assertEquals(ALICE, modelManager.getSelectedEntry());
    }

    @Test
    public void equals() {
        EntryBook listEntryBook = new EntryBookBuilder().withEntry(ALICE).withEntry(BENSON).build();
        EntryBook archivesEntryBook = new EntryBookBuilder().withEntry(CARL).withEntry(DANIEL).build();
        EntryBook searchEntryBook = new EntryBookBuilder().withEntry(WIKIPEDIA_ENTRY).build();
        EntryBook feedsEntryBook = new EntryBookBuilder().withEntry(KATTIS_FEED_ENTRY).build();
        EntryBook differentListEntryBook = new EntryBook();
        EntryBook differentArchivesEntryBook = new EntryBook();
        EntryBook differentFeedsEntryBook = new EntryBook();

        UserPrefs userPrefs = new UserPrefs();
        Storage storage = new StorageStub();

        // same values -> returns true
        modelManager = new ModelManager(listEntryBook, archivesEntryBook, feedsEntryBook, userPrefs, storage);
        ModelManager modelManagerCopy = new ModelManager(listEntryBook, archivesEntryBook, feedsEntryBook, userPrefs,
                storage);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different context -> returns false
        modelManager.setContext(ModelContext.CONTEXT_ARCHIVES); // default context is LIST
        assertFalse(modelManager.equals(new ModelManager(listEntryBook, archivesEntryBook, feedsEntryBook, userPrefs,
                storage)));
        modelManager.setContext(ModelContext.CONTEXT_LIST);

        // different listEntryBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentListEntryBook, archivesEntryBook, feedsEntryBook,
            userPrefs, storage)));

        // different archivesEntryBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(listEntryBook, differentArchivesEntryBook, feedsEntryBook,
            userPrefs, storage)));

        // different searchEntryBook -> returns false
        modelManagerCopy.setSearchEntryBook(searchEntryBook);
        assertFalse(modelManager.equals(modelManagerCopy));

        // different feedsEntryBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(listEntryBook, archivesEntryBook, differentFeedsEntryBook,
                userPrefs, storage)));

        // different filteredList -> returns false
        showEntryAtIndex(modelManager, INDEX_FIRST_ENTRY);
        assertFalse(modelManager.equals(new ModelManager(listEntryBook, archivesEntryBook, feedsEntryBook, userPrefs,
                storage)));

        // resets modelManager to initial state for upcoming tests
        modelManager.setContext(ModelContext.CONTEXT_LIST);
        modelManager.updateFilteredEntryList(PREDICATE_SHOW_ALL_ENTRIES);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setListEntryBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(listEntryBook, archivesEntryBook, feedsEntryBook,
            differentUserPrefs, storage)));

        UserPrefs differentUserPrefs2 = new UserPrefs();
        differentUserPrefs2.setArticleDataDirectoryPath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(listEntryBook, archivesEntryBook, feedsEntryBook,
            differentUserPrefs2, storage)));

        // different context -> returns false
        ModelManager differentContextModelManager = new ModelManager(listEntryBook, archivesEntryBook, feedsEntryBook,
            userPrefs, storage);
        differentContextModelManager.setContext(ModelContext.CONTEXT_ARCHIVES);
        assertFalse(modelManager.equals(differentContextModelManager));
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }
}
