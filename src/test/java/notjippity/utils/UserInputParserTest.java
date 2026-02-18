package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class UserInputParserTest {

    @Test
    public void testGetCommand() {
        assertEquals("cmd", UserInputParser.getCommand("cmd"));
        assertEquals("cmd", UserInputParser.getCommand("   cmd"));
        assertEquals("cmd", UserInputParser.getCommand("cmd   "));
        assertEquals("cmd", UserInputParser.getCommand("   cmd   "));
        assertEquals("c", UserInputParser.getCommand("  c  m  d"));
        assertNull(null, UserInputParser.getCommand(""));
        assertNull(null, UserInputParser.getCommand("   "));
        assertEquals("c", UserInputParser.getCommand("     c"));
        assertNull(null, UserInputParser.getCommand(null));
    }

}
