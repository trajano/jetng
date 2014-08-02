package net.trajano.jetng.internal;

import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashSet;
import java.util.Set;

import net.trajano.jetng.ParserContext;

/**
 * This class contains methods that have been exported so it can be tested
 * without making it part of the public API.
 *
 * @author Archimedes Trajano
 *
 */
public final class Util {
    /**
     * Characters that are safe to put in a Java string without escaping.
     */
    private static final Set<Character> SAFE_CHARACTERS;
    static {
        SAFE_CHARACTERS = new HashSet<Character>();
        for (final char c : " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+{}|:;'/?<>,.[]-="
                .toCharArray()) {
            SAFE_CHARACTERS.add(c);
        }
    }

    /**
     * Escapes a string to make it work as a Java string. The result will
     * already be quoted. The exception is if "s" is null this will return
     * "null" as a string (without quotes). If the input string is empty this
     * will return an empty string. If it is a single character, then the quoted
     * string returned is single quoted.
     *
     * @param s
     *            string to escape
     * @return quoted escaped string
     */
    public static String escapeJavaString(final String s) {
        if (s == null) {
            return "null";
        } else if (s.isEmpty()) {
            return "";
        } else if ("'".equals(s)) {
            return "'\\''";
        } else if ("\"".equals(s)) {
            return "'\"'";
        } else if (s.length() == 1) {
            return "'" + escapeSingleCharacter(s.charAt(0)) + "'";
        }
        final char[] carray = s.toCharArray();
        final StringBuilder b = new StringBuilder("\"");
        for (final char c : carray) {
            if (c == '"') {
                b.append("\\\"");
            } else {
                b.append(escapeSingleCharacter(c));
            }
        }
        b.append('"');
        return b.toString();
    }

    /**
     * Escapes a single character.
     *
     * @param c
     *            character to escape
     * @return escaped character
     */
    private static String escapeSingleCharacter(final char c) {
        if (c == '\r') {
            return "\\r";
        } else if (c == '\n') {
            return "\\n";
        } else if (c == '\t') {
            return "\\t";
        } else if (SAFE_CHARACTERS.contains(c)) {
            return String.valueOf(c);
        } else {
            return String.format("\\u%04x", (int) c);
        }
    }

    /**
     * If a defined text is coming up, this will return <code>true</code> and
     * the reader will be at the point after the text. Otherwise, the data will
     * be pushed back up until the character
     *
     * @param context
     *            parsing context
     * @param firstChar
     *            first character
     * @param text
     *            text being expected.
     * @param r
     *            reader
     * @return end comment
     */
    public static boolean isTextComing(final ParserContext context,
            final char firstChar, final String text, final PushbackReader r)
            throws IOException {

        final char[] carray = text.toCharArray();
        if (firstChar != carray[0]) {
            return false;
        }
        int i = 1;
        while (i < carray.length) {
            final int c = r.read();
            if (c == -1) {
                throw new EOFException();
            } else if (c != carray[i]) {
                r.unread(c);
                r.unread(carray, 1, i - 1);
                return false;
            }
            ++i;
        }
        for (int j = 0; j < carray.length - 1; ++j) {
            context.inc();
        }
        return true;
    }

    /**
     * Prevent instantiation of utility class.
     */
    private Util() {
    }
}
