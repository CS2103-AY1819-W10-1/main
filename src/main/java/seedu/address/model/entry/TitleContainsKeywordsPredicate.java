package seedu.address.model.entry;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Entry}'s {@code Title} matches any of the keywords given.
 */
public class TitleContainsKeywordsPredicate implements Predicate<Entry> {
    private final List<String> keywords;

    public TitleContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Entry entry) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(entry.getTitle().fullTitle, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TitleContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((TitleContainsKeywordsPredicate) other).keywords)); // state check
    }

}
