package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code EntryBook} that keeps track of its own history.
 */
public class VersionedEntryBook extends EntryBook {

    private final List<ReadOnlyEntryBook> entryBookStateList;
    private int currentStatePointer;

    public VersionedEntryBook(ReadOnlyEntryBook initialState) {
        super(initialState);

        entryBookStateList = new ArrayList<>();
        entryBookStateList.add(new EntryBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code EntryBook} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        entryBookStateList.add(new EntryBook(this));
        currentStatePointer++;
        indicateModified();
    }

    private void removeStatesAfterCurrentPointer() {
        entryBookStateList.subList(currentStatePointer + 1, entryBookStateList.size()).clear();
    }

    /**
     * Restores the entry book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(entryBookStateList.get(currentStatePointer));
    }

    /**
     * Restores the entry book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(entryBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has entry book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has entry book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < entryBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedEntryBook)) {
            return false;
        }

        VersionedEntryBook otherVersionedEntryBook = (VersionedEntryBook) other;

        // state check
        return super.equals(otherVersionedEntryBook)
                && entryBookStateList.equals(otherVersionedEntryBook.entryBookStateList)
                && currentStatePointer == otherVersionedEntryBook.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of entryBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of entryBookState list, unable to redo.");
        }
    }
}
