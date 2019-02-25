package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class TitleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Title(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Title(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        Assert.assertThrows(NullPointerException.class, () -> Title.isValidName(null));

        // invalid name
        assertFalse(Title.isValidName("")); // empty string
        assertFalse(Title.isValidName(" ")); // spaces only
        assertFalse(Title.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Title.isValidName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(Title.isValidName("peter jack")); // alphabets only
        assertTrue(Title.isValidName("12345")); // numbers only
        assertTrue(Title.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Title.isValidName("Capital Tan")); // with capital letters
        assertTrue(Title.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }
}
