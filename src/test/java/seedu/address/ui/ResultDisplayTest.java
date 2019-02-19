package seedu.address.ui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.ResultDisplayHandle;

public class ResultDisplayTest extends GuiUnitTest {

    private ResultDisplay resultDisplay;
    private ResultDisplayHandle resultDisplayHandle;

    private ArrayList<String> defaultStyleOfResultDisplay;
    private ArrayList<String> errorStyleOfResultDisplay;

    @Before
    public void setUp() {
        resultDisplay = new ResultDisplay();
        uiPartRule.setUiPart(resultDisplay);

        resultDisplayHandle = new ResultDisplayHandle(getChildNode(resultDisplay.getRoot(),
                ResultDisplayHandle.RESULT_DISPLAY_ID));

        defaultStyleOfResultDisplay = new ArrayList<>(resultDisplayHandle.getStyleClass());

        errorStyleOfResultDisplay = new ArrayList<>(defaultStyleOfResultDisplay);
        errorStyleOfResultDisplay.add(CommandBox.ERROR_STYLE_CLASS);
    }

    @Test
    public void defaultDisplay_emptyDisplay() {
        // default result text
        guiRobot.pauseForHuman();
        assertEquals("", resultDisplayHandle.getText());
    }

    @Test
    public void emptyFeedback_emptyDisplay() {
        guiRobot.interact(() -> resultDisplay.setFeedbackSuccessToUser(""));
        guiRobot.pauseForHuman();
        assertEquals("", resultDisplayHandle.getText());
        assertEquals(defaultStyleOfResultDisplay, resultDisplayHandle.getStyleClass());
    }

    @Test
    public void successfulFeedback() {
        guiRobot.interact(() -> resultDisplay.setFeedbackSuccessToUser("Dummy feedback to user"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy feedback to user", resultDisplayHandle.getText());
        assertStyleForSuccessfulFeedback(resultDisplayHandle);
    }

    @Test
    public void errorFeedback() {
        guiRobot.interact(() -> resultDisplay.setFeedbackErrorToUser("Dummy feedback to user"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy feedback to user", resultDisplayHandle.getText());
        assertStyleForErrorFeedback(resultDisplayHandle);
    }

    @Test
    public void consecutiveFeedBack() {
        // new error result received
        guiRobot.interact(() -> resultDisplay.setFeedbackErrorToUser("Dummy feedback to user"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy feedback to user", resultDisplayHandle.getText());
        assertStyleForErrorFeedback(resultDisplayHandle);

        // new error result received
        guiRobot.interact(() -> resultDisplay.setFeedbackErrorToUser("Dummy feedback to user"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy feedback to user", resultDisplayHandle.getText());
        assertStyleForErrorFeedback(resultDisplayHandle);

        // new success result received
        guiRobot.interact(() -> resultDisplay.setFeedbackSuccessToUser("Dummy feedback to user"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy feedback to user", resultDisplayHandle.getText());
        assertStyleForSuccessfulFeedback(resultDisplayHandle);

        // new success result received
        guiRobot.interact(() -> resultDisplay.setFeedbackSuccessToUser("Dummy feedback to user"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy feedback to user", resultDisplayHandle.getText());
        assertStyleForSuccessfulFeedback(resultDisplayHandle);

        // new error result received
        guiRobot.interact(() -> resultDisplay.setFeedbackErrorToUser("Dummy feedback to user"));
        guiRobot.pauseForHuman();
        assertEquals("Dummy feedback to user", resultDisplayHandle.getText());
        assertStyleForErrorFeedback(resultDisplayHandle);
    }

    private void assertStyleForSuccessfulFeedback(ResultDisplayHandle resultDisplayHandle) {
        assertEquals(defaultStyleOfResultDisplay, resultDisplayHandle.getStyleClass());
    }

    private void assertStyleForErrorFeedback(ResultDisplayHandle resultDisplayHandle) {
        assertEquals(errorStyleOfResultDisplay, resultDisplayHandle.getStyleClass());
    }
}
