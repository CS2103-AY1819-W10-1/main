package seedu.address.logic;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.ArchivesCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.FeedsCommand;
import seedu.address.logic.commands.GoogleNewsCommand;
import seedu.address.logic.commands.ListCommand;
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
    public void executeCommand(Command command) {
        logger.info("----------------[COMMAND][" + command + "]");

        try {
            CommandResult commandResult = command.execute(model, history);
            setCommandResult(commandResult);
        } catch (CommandException ce) {
            setException(ce);
        }
    }

    @Override
    public void executeContextSwitch(ModelContext context) {
        switch (context) {
        case CONTEXT_SEARCH:
            try {
                executeCommand(new GoogleNewsCommand());
            } catch (MalformedURLException mue) {
                setException(new CommandException("Sorry, Google News is unavailable."));
                executeCommand(new ListCommand());
            }
            break;
        case CONTEXT_LIST:
            executeCommand(new ListCommand());
            break;
        case CONTEXT_ARCHIVES:
            executeCommand(new ArchivesCommand());
            break;
        case CONTEXT_FEEDS:
            executeCommand(new FeedsCommand());
            break;
        default:
            break;
        }
    }

    @Override
    public ReadOnlyEntryBook getListEntryBook() {
        return model.getListEntryBook();
    }

    @Override
    public Optional<URL> getOfflineLink(URL url) {
        return model.getOfflineLink(url);
    }

    @Override
    public Optional<String> getArticle(URL url) {
        return model.getArticle(url);
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

}
