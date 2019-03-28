package seedu.address.logic;

import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelContext;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.ui.ViewMode;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;

        try {
            Command command = model.getContext().getParser().parseCommand(commandText);
            commandResult = command.execute(model, history);
        } finally {
            history.add(commandText);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyEntryBook getListEntryBook() {
        return model.getListEntryBook();
    }

    @Override
    public ObservableList<Entry> getFilteredEntryList() {
        return model.getFilteredEntryList();
    }

    @Override
    public ObservableList<String> getHistory() {
        return history.getHistory();
    }

    @Override
    public Path getListEntryBookFilePath() {
        return model.getListEntryBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ReadOnlyProperty<Entry> selectedEntryProperty() {
        return model.selectedEntryProperty();
    }

    @Override
    public void setSelectedEntry(Entry entry) {
        model.setSelectedEntry(entry);
    }

    @Override
    public ReadOnlyProperty<ViewMode> viewModeProperty() {
        return model.viewModeProperty();
    }

    @Override
    public void setViewMode(ViewMode viewMode) {
        model.setViewMode(viewMode);
    }

    @Override
    public ReadOnlyProperty<Exception> exceptionProperty() {
        return model.exceptionProperty();
    }

    @Override
    public void setException(Exception exception) {
        model.setException(exception);
    }

    @Override
    public ReadOnlyProperty<CommandResult> commandResultProperty() {
        return model.commandResultProperty();
    }

    @Override
    public void setCommandResult(CommandResult commandResult) {
        model.setCommandResult(commandResult);
    }

    @Override
    public ReadOnlyProperty<ModelContext> contextProperty() {
        return model.contextProperty();
    }

    @Override
    public void setContext(ModelContext context) {
        model.setContext(context);
    }

}
