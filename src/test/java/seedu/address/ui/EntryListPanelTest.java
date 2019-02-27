package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalEntries.getTypicalPersons;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import guitests.guihandles.EntryListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Comment;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Link;
import seedu.address.model.entry.Title;

public class EntryListPanelTest extends GuiUnitTest {
    private static final ObservableList<Entry> TYPICAL_ENTRIES =
            FXCollections.observableList(getTypicalPersons());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Entry> selectedPerson = new SimpleObjectProperty<>();
    private EntryListPanelHandle entryListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_ENTRIES);

        for (int i = 0; i < TYPICAL_ENTRIES.size(); i++) {
            entryListPanelHandle.navigateToCard(TYPICAL_ENTRIES.get(i));
            Entry expectedEntry = TYPICAL_ENTRIES.get(i);
            EntryCardHandle actualCard = entryListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysPerson(expectedEntry, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void selection_modelSelectedPersonChanged_selectionChanges() {
        initUi(TYPICAL_ENTRIES);
        Entry secondEntry = TYPICAL_ENTRIES.get(INDEX_SECOND_PERSON.getZeroBased());
        guiRobot.interact(() -> selectedPerson.set(secondEntry));
        guiRobot.pauseForHuman();

        EntryCardHandle expectedPerson = entryListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        EntryCardHandle selectedPerson = entryListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of persons in {@code EntryListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Entry> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of entry cards exceeded time limit");
    }

    /**
     * Returns a list of persons containing {@code personCount} persons that is used to populate the
     * {@code EntryListPanel}.
     */
    private ObservableList<Entry> createBackingList(int personCount) {
        ObservableList<Entry> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < personCount; i++) {
            Title title = new Title(i + "a");
            Comment comment = new Comment("000");
            Link link = new Link("https://a.aa");
            Address address = new Address("a");
            Entry entry = new Entry(title, comment, link, address, Collections.emptySet());
            backingList.add(entry);
        }
        return backingList;
    }

    /**
     * Initializes {@code entryListPanelHandle} with a {@code EntryListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code EntryListPanel}.
     */
    private void initUi(ObservableList<Entry> backingList) {
        EntryListPanel entryListPanel =
                new EntryListPanel(backingList, selectedPerson, selectedPerson::set);
        uiPartRule.setUiPart(entryListPanel);

        entryListPanelHandle = new EntryListPanelHandle(getChildNode(entryListPanel.getRoot(),
                EntryListPanelHandle.ENTRY_LIST_VIEW_ID));
    }
}
