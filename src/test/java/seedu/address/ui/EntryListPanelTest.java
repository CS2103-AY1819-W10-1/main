package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.util.Collections;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Email;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Title;
import seedu.address.model.entry.Phone;

public class EntryListPanelTest extends GuiUnitTest {
    private static final ObservableList<Entry> TYPICAL_ENTRIES =
            FXCollections.observableList(getTypicalPersons());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private final SimpleObjectProperty<Entry> selectedPerson = new SimpleObjectProperty<>();
    private PersonListPanelHandle personListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_ENTRIES);

        for (int i = 0; i < TYPICAL_ENTRIES.size(); i++) {
            personListPanelHandle.navigateToCard(TYPICAL_ENTRIES.get(i));
            Entry expectedEntry = TYPICAL_ENTRIES.get(i);
            PersonCardHandle actualCard = personListPanelHandle.getPersonCardHandle(i);

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

        PersonCardHandle expectedPerson = personListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        PersonCardHandle selectedPerson = personListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedPerson, selectedPerson);
    }

    /**
     * Verifies that creating and deleting large number of persons in {@code PersonListPanel} requires lesser than
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
     * {@code PersonListPanel}.
     */
    private ObservableList<Entry> createBackingList(int personCount) {
        ObservableList<Entry> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < personCount; i++) {
            Title title = new Title(i + "a");
            Phone phone = new Phone("000");
            Email email = new Email("a@aa");
            Address address = new Address("a");
            Entry entry = new Entry(title, phone, email, address, Collections.emptySet());
            backingList.add(entry);
        }
        return backingList;
    }

    /**
     * Initializes {@code personListPanelHandle} with a {@code PersonListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code PersonListPanel}.
     */
    private void initUi(ObservableList<Entry> backingList) {
        PersonListPanel personListPanel =
                new PersonListPanel(backingList, selectedPerson, selectedPerson::set);
        uiPartRule.setUiPart(personListPanel);

        personListPanelHandle = new PersonListPanelHandle(getChildNode(personListPanel.getRoot(),
                PersonListPanelHandle.PERSON_LIST_VIEW_ID));
    }
}
