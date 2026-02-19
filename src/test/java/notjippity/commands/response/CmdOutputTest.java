package notjippity.commands.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Contains JUnit tests for the CmdOutput class.
 */
public class CmdOutputTest {

    /**
     * Tests that constructor correctly sets isError to false.
     */
    @Test
    public void constructor_isErrorFalse_correctlySet() {
        CmdOutput output = new CmdOutput(false, List.of("Message"));
        assertFalse(output.isError());
    }

    /**
     * Tests that constructor correctly sets isError to true.
     */
    @Test
    public void constructor_isErrorTrue_correctlySet() {
        CmdOutput output = new CmdOutput(true, List.of("Error message"));
        assertTrue(output.isError());
    }

    /**
     * Tests that getReply returns the correct messages for single and multiple messages.
     */
    @Test
    public void getReply_variousMessageCounts_returnsCorrectMessages() {
        // Single message
        CmdOutput singleOutput = new CmdOutput(false, List.of("Test message"));
        List<String> singleReply = singleOutput.getReply();
        assertEquals(1, singleReply.size());
        assertEquals("Test message", singleReply.get(0));

        // Multiple messages
        List<String> messages = List.of("Message 1", "Message 2", "Message 3");
        CmdOutput multiOutput = new CmdOutput(false, messages);
        List<String> multiReply = multiOutput.getReply();
        assertEquals(3, multiReply.size());
        assertEquals("Message 1", multiReply.get(0));
        assertEquals("Message 2", multiReply.get(1));
        assertEquals("Message 3", multiReply.get(2));
    }

    /**
     * Tests that getReply returns a defensive copy.
     */
    @Test
    public void getReply_modifyReturnedList_originalUnchanged() {
        List<String> originalMessages = new ArrayList<>(List.of("Original"));
        CmdOutput output = new CmdOutput(false, originalMessages);

        List<String> reply = output.getReply();
        reply.add("Modified");

        // Original should be unchanged
        assertEquals(1, output.getReply().size());
    }

    /**
     * Tests that getReply handles empty list.
     */
    @Test
    public void getReply_emptyList_returnsEmptyList() {
        CmdOutput output = new CmdOutput(false, List.of());
        List<String> reply = output.getReply();
        assertTrue(reply.isEmpty());
    }

    /**
     * Tests that isError and getReply work together correctly.
     */
    @Test
    public void fullOutput_errorWithMessage_correctBehavior() {
        CmdOutput output = new CmdOutput(true, List.of("Something went wrong!"));
        assertTrue(output.isError());
        assertEquals("Something went wrong!", output.getReply().get(0));
    }
}

