package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.PersonBuilder;

public class EntryCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Entry entryWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(entryWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, entryWithNoTags, 1);

        // with tags
        Entry entryWithTags = new PersonBuilder().build();
        personCard = new PersonCard(entryWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, entryWithTags, 2);
    }

    @Test
    public void equals() {
        Entry entry = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(entry, 0);

        // same entry, same index -> returns true
        PersonCard copy = new PersonCard(entry, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different entry, same index -> returns false
        Entry differentEntry = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentEntry, 0)));

        // same entry, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(entry, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedEntry} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Entry expectedEntry, int expectedId) {
        guiRobot.pauseForHuman();

        PersonCardHandle personCardHandle = new PersonCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify entry details are displayed correctly
        assertCardDisplaysPerson(expectedEntry, personCardHandle);
    }
}
