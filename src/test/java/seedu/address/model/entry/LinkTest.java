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
        Assert.assertThrows(NullPointerException.class, () -> Link.isValidLink(null));

        // blank email
        assertFalse(Link.isValidLink("")); // empty string
        assertFalse(Link.isValidLink(" ")); // spaces only

        // missing parts
        assertFalse(Link.isValidLink("@example.com")); // missing local part
        assertFalse(Link.isValidLink("peterjackexample.com")); // missing '@' symbol
        assertFalse(Link.isValidLink("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Link.isValidLink("peterjack.com")); // no protocol
        assertFalse(Link.isValidLink("htps://peterjack.com")); // invalid protocol
        assertFalse(Link.isValidLink("https://peterjack.example.com.")); // invalid ending character
        assertFalse(Link.isValidLink("https://peter jack.example.com")); // spaces in sub-domain
        assertFalse(Link.isValidLink("https://peterjack.exam ple.com")); // spaces in domain name
        assertFalse(Link.isValidLink(" https://peterjack@example.com")); // leading space
        assertFalse(Link.isValidLink("https://peterjack@example.com ")); // trailing space

        /*
        // Legacy tests
        assertFalse(Link.isValidLink("https://peterjack@.example.com")); // domain name starts with a period
        assertFalse(Link.isValidLink("https://peterjack@example.com.")); // domain name ends with a period
        assertFalse(Link.isValidLink("https://peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Link.isValidLink("https://peterjack@example.com-")); // domain name ends with a hyphen
        assertTrue(Link.isValidLink("https://!#$%&'*+/=?`{|}~^.-@example.org")); // special characters
        */

        // valid link
        assertTrue(Link.isValidLink("http://peterJack_1190.example.com")); // https protocol
        assertTrue(Link.isValidLink("https://peterJack_1190.example.com")); // https protocol
        assertTrue(Link.isValidLink("https://peterJack_1190.example.com:443")); // with port
        assertTrue(Link.isValidLink("https://peterJack_1190.example.com/folder/to/file")); // with path to file
        assertTrue(Link.isValidLink("https://www.peterJack_1190.example.com")); // with www

        assertTrue(Link.isValidLink("file:///c/Desktop/file.txt")); // file protocol

        assertTrue(Link.isValidLink("http://localhost")); // localhost
        assertTrue(Link.isValidLink("http://localhost/path/to/file.txt")); // localhost
        assertTrue(Link.isValidLink("http://localhost:8080")); // localhost with port

        assertTrue(Link.isValidLink("https://208.67.222.222")); // IP Address
        assertTrue(Link.isValidLink("https://208.67.222.222/path/to/file.txt")); // IP Address with path

        assertTrue(Link.isValidLink("https://test.localhost")); // alphabets only
        assertTrue(Link.isValidLink("https://a1+be!.example1.com")); // mixture of alphanumeric and special characters

        assertTrue(Link.isValidLink("https://peter_jack-very-very-very-long-example.com")); // long domain name
        assertTrue(Link.isValidLink("https://if.you.dream.it_you.can.do.it.example.com")); // multiple sub-domains
    }
}
