package notjippity.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ParserTest {

    @Test
    public void testGetCommand() {
        assertEquals("cmd", Parser.getCommand("cmd"));
        assertEquals("cmd", Parser.getCommand("   cmd"));
        assertEquals("cmd", Parser.getCommand("cmd   "));
        assertEquals("cmd", Parser.getCommand("   cmd   "));
        assertEquals("c", Parser.getCommand("  c  m  d"));
        assertNull(null, Parser.getCommand(""));
        assertNull(null, Parser.getCommand("   "));
        assertEquals("c", Parser.getCommand("     c"));
        assertNull(null, Parser.getCommand(null));
    }

}
