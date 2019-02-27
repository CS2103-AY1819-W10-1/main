package seedu.address.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SCIENCE;
import static seedu.address.testutil.TypicalEntries.ALICE;
import static seedu.address.testutil.TypicalEntries.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.entry.exceptions.DuplicateEntryException;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.testutil.EntryBuilder;

public class UniqueEntryListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniquePersonList uniquePersonList = new UniquePersonList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniquePersonList.contains(null);
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        assertTrue(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        Entry editedAlice = new EntryBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_SCIENCE)
                .build();
        assertTrue(uniquePersonList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniquePersonList.add(null);
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        thrown.expect(DuplicateEntryException.class);
        uniquePersonList.add(ALICE);
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniquePersonList.setPerson(null, ALICE);
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniquePersonList.setPerson(ALICE, null);
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        thrown.expect(EntryNotFoundException.class);
        uniquePersonList.setPerson(ALICE, ALICE);
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(ALICE);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        uniquePersonList.add(ALICE);
        Entry editedAlice = new EntryBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_SCIENCE)
                .build();
        uniquePersonList.setPerson(ALICE, editedAlice);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(editedAlice);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, BOB);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        thrown.expect(DuplicateEntryException.class);
        uniquePersonList.setPerson(ALICE, BOB);
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniquePersonList.remove(null);
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        thrown.expect(EntryNotFoundException.class);
        uniquePersonList.remove(ALICE);
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniquePersonList.add(ALICE);
        uniquePersonList.remove(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniquePersonList.setPersons((UniquePersonList) null);
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniquePersonList.add(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        uniquePersonList.setPersons(expectedUniquePersonList);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniquePersonList.setPersons((List<Entry>) null);
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniquePersonList.add(ALICE);
        List<Entry> entryList = Collections.singletonList(BOB);
        uniquePersonList.setPersons(entryList);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Entry> listWithDuplicateEntries = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateEntryException.class);
        uniquePersonList.setPersons(listWithDuplicateEntries);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniquePersonList.asUnmodifiableObservableList().remove(0);
    }
}
