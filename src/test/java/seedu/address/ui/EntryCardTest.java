package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryBuilder;

public class EntryCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Entry entryWithNoTags = new EntryBuilder().withTags(new String[0]).build();
        EntryCard entryCard = new EntryCard(entryWithNoTags, 1);
        uiPartRule.setUiPart(entryCard);
        assertCardDisplay(entryCard, entryWithNoTags, 1);

        // with tags
        Entry entryWithTags = new EntryBuilder().build();
        entryCard = new EntryCard(entryWithTags, 2);
        uiPartRule.setUiPart(entryCard);
        assertCardDisplay(entryCard, entryWithTags, 2);
    }

    @Test
    public void equals() {
        Entry entry = new EntryBuilder().build();
        EntryCard entryCard = new EntryCard(entry, 0);

        // same entry, same index -> returns true
        EntryCard copy = new EntryCard(entry, 0);
        assertTrue(entryCard.equals(copy));

        // same object -> returns true
        assertTrue(entryCard.equals(entryCard));

        // null -> returns false
        assertFalse(entryCard.equals(null));

        // different types -> returns false
        assertFalse(entryCard.equals(0));

        // different entry, same index -> returns false
        Entry differentEntry = new EntryBuilder().withName("differentName").build();
        assertFalse(entryCard.equals(new EntryCard(differentEntry, 0)));

        // same entry, different index -> returns false
        assertFalse(entryCard.equals(new EntryCard(entry, 1)));
    }

    /**
     * Asserts that {@code entryCard} displays the details of {@code expectedEntry} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(EntryCard entryCard, Entry expectedEntry, int expectedId) {
        guiRobot.pauseForHuman();

        EntryCardHandle entryCardHandle = new EntryCardHandle(entryCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", entryCardHandle.getId());

        // verify entry details are displayed correctly
        assertCardDisplaysPerson(expectedEntry, entryCardHandle);
    }
}
