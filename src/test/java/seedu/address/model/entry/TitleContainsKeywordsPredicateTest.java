package seedu.address.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.EntryBuilder;

public class TitleContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TitleContainsKeywordsPredicate firstPredicate = new TitleContainsKeywordsPredicate(firstPredicateKeywordList);
        TitleContainsKeywordsPredicate secondPredicate = new TitleContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TitleContainsKeywordsPredicate firstPredicateCopy = new TitleContainsKeywordsPredicate(
            firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different entry -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        TitleContainsKeywordsPredicate predicate = new TitleContainsKeywordsPredicate(
            Collections.singletonList("Alice"));
        assertTrue(predicate.test(new EntryBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new EntryBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new EntryBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new EntryBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        TitleContainsKeywordsPredicate predicate = new TitleContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new EntryBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new EntryBuilder().withName("Alice Bob").build()));

        // Keywords match phone, email and address, but does not match name
        predicate = new TitleContainsKeywordsPredicate(Arrays.asList("12345", "https://example.com", "Main", "Street"));
        assertFalse(predicate.test(new EntryBuilder().withName("Alice").withPhone("12345")
                .withEmail("https://example.com").withAddress("Main Street").build()));
    }
}
