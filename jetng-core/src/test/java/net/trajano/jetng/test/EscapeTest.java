package net.trajano.jetng.test;

import static org.junit.Assert.assertEquals;
import net.trajano.jetng.internal.Util;

import org.junit.Test;

/**
 * Tests the module.
 */
public class EscapeTest {
    /**
     * Tests empty string input.
     */
    @Test
    public void testEmptyString() throws Exception {
        assertEquals("", Util.escapeJavaString(""));
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testNull() throws Exception {
        assertEquals("null", Util.escapeJavaString(null));
    }

    /**
     * Tests quoted strings.
     */
    @Test
    public void testQuotedString() throws Exception {
        assertEquals("\"hel\\\"lo\"", Util.escapeJavaString("hel\"lo"));
    }

    /**
     * Tests single character.
     */
    @Test
    public void testSingleCharacter() throws Exception {
        assertEquals("'a'", Util.escapeJavaString("a"));
        assertEquals("'\\''", Util.escapeJavaString("'"));
        assertEquals("'\"'", Util.escapeJavaString("\""));
        assertEquals("'='", Util.escapeJavaString("="));
        assertEquals("'\\u0215'", Util.escapeJavaString("\u0215"));
    }

    /**
     * Tests unescaped input.
     */
    @Test
    public void testUnescapedString() throws Exception {
        assertEquals("\"hello\"", Util.escapeJavaString("hello"));
    }

    /**
     * Tests Unicode.
     */
    @Test
    public void testUnicode() {
        assertEquals("\"\\thello \\u2663 world\\r\\n\"",
                Util.escapeJavaString("\thello \u2663 world\r\n"));
    }

    /**
     * Tests whitespace.
     */
    @Test
    public void testWhiteSpaceString() {
        assertEquals("\"\\thello world\\r\\n\"",
                Util.escapeJavaString("\thello world\r\n"));
    }
}