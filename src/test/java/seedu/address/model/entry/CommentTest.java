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
        Assert.assertThrows(NullPointerException.class, () -> Comment.isValidComment(null));

        // invalid phone numbers
        assertFalse(Comment.isValidComment("")); // empty string
        assertFalse(Comment.isValidComment(" ")); // spaces only

        // valid phone numbers
        assertTrue(Comment.isValidComment("comment")); // non-numeric
        assertTrue(Comment.isValidComment("comment comment")); // non-numeric with spaces
        assertTrue(Comment.isValidComment("comment?")); // non-numeric with symbols
        assertTrue(Comment.isValidComment("comment@")); // non-numeric with symbols
        assertTrue(Comment.isValidComment("comment!")); // non-numeric with symbols
        assertTrue(Comment.isValidComment("comment-comment")); // non-numeric with spaces
        assertTrue(Comment.isValidComment("93121534")); // numeric
        assertTrue(Comment.isValidComment("9312153asd4asd219")); // alphanumeric
        assertTrue(Comment.isValidComment("9312153asd4 asd219")); // alphanumeric
        assertTrue(Comment.isValidComment("Five reasons why your best-friend is eating grass. 3# will shock you!"));

    }
}
