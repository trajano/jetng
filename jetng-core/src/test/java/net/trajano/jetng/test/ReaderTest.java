package net.trajano.jetng.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.PushbackReader;
import java.io.StringReader;

import net.trajano.jetng.internal.Util;

import org.junit.Test;

/**
 * Tests the module.
 */
public class ReaderTest {
    /**
     * Tests the module method.
     */
    @Test
    public void testBadStartTag() throws Exception {
        final PushbackReader r = new PushbackReader(new StringReader("X<% abc"));
        assertFalse(Util.isTextComing((char) r.read(), "<%", r));
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testEndOfComment() throws Exception {
        final PushbackReader r = new PushbackReader(
                new StringReader("--%> abc"));
        assertTrue(Util.isTextComing((char) r.read(), "--%>", r));
        assertEquals(' ', (char) r.read());
        assertEquals('a', (char) r.read());
        assertEquals('b', (char) r.read());
        assertEquals('c', (char) r.read());
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testNotEndOfComment() throws Exception {
        final String text = "-%foo -%>";
        final PushbackReader r = new PushbackReader(new StringReader(text), 10);
        assertFalse(Util.isTextComing((char) r.read(), "--%>", r));
        final char[] cbuf = new char[text.length() - 1];
        r.read(cbuf);
        assertArrayEquals(text.substring(1).toCharArray(), cbuf);
    }

    /**
     * Tests the pushbackReader
     */
    @Test
    public void testPushbackReader() throws Exception {
        final String text = "-%foo -%>";
        final PushbackReader r = new PushbackReader(new StringReader(text), 10);
        assertEquals('-', (char) r.read());
        assertEquals('%', (char) r.read());
        r.unread("-%".toCharArray(), 0, 2);
        assertEquals('-', (char) r.read());
        assertEquals('%', (char) r.read());
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testStartTag() throws Exception {
        final PushbackReader r = new PushbackReader(new StringReader("<% abc"));
        assertTrue(Util.isTextComing((char) r.read(), "<%", r));
        assertEquals(' ', (char) r.read());
        assertEquals('a', (char) r.read());
        assertEquals('b', (char) r.read());
        assertEquals('c', (char) r.read());
    }
}