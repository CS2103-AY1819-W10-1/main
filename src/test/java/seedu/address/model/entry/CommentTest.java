package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class CommentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Comment(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Comment(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        Assert.assertThrows(NullPointerException.class, () -> Comment.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Comment.isValidPhone("")); // empty string
        assertFalse(Comment.isValidPhone(" ")); // spaces only
        assertFalse(Comment.isValidPhone("91")); // less than 3 numbers
        assertFalse(Comment.isValidPhone("phone")); // non-numeric
        assertFalse(Comment.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Comment.isValidPhone("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(Comment.isValidPhone("911")); // exactly 3 numbers
        assertTrue(Comment.isValidPhone("93121534"));
        assertTrue(Comment.isValidPhone("124293842033123")); // long phone numbers
    }
}
