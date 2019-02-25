package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class LinkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Link(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Link(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        Assert.assertThrows(NullPointerException.class, () -> Link.isValidEmail(null));

        // blank email
        assertFalse(Link.isValidEmail("")); // empty string
        assertFalse(Link.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Link.isValidEmail("@example.com")); // missing local part
        assertFalse(Link.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertFalse(Link.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Link.isValidEmail("peterjack@-")); // invalid domain name
        assertFalse(Link.isValidEmail("peterjack@exam_ple.com")); // underscore in domain name
        assertFalse(Link.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Link.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Link.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Link.isValidEmail("peterjack@example.com ")); // trailing space
        assertFalse(Link.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Link.isValidEmail("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Link.isValidEmail("peterjack@example@com")); // '@' symbol in domain name
        assertFalse(Link.isValidEmail("peterjack@.example.com")); // domain name starts with a period
        assertFalse(Link.isValidEmail("peterjack@example.com.")); // domain name ends with a period
        assertFalse(Link.isValidEmail("peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Link.isValidEmail("peterjack@example.com-")); // domain name ends with a hyphen

        // valid email
        assertTrue(Link.isValidEmail("PeterJack_1190@example.com"));
        assertTrue(Link.isValidEmail("a@bc")); // minimal
        assertTrue(Link.isValidEmail("test@localhost")); // alphabets only
        assertTrue(Link.isValidEmail("!#$%&'*+/=?`{|}~^.-@example.org")); // special characters local part
        assertTrue(Link.isValidEmail("123@145")); // numeric local part and domain name
        assertTrue(Link.isValidEmail("a1+be!@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Link.isValidEmail("peter_jack@very-very-very-long-example.com")); // long domain name
        assertTrue(Link.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // long local part
    }
}
