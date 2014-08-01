package net.trajano.jetng.internal;

import java.io.EOFException;
import java.io.IOException;
import java.io.PushbackReader;

/**
 * This class contains methods that have been exported so it can be tested
 * without making it part of the public API.
 *
 * @author Archimedes Trajano
 *
 */
public final class ReaderUtil {
    /**
     * If a defined text is coming up, this will return <code>true</code> and
     * the reader will be at the point after the text. Otherwise, the data will
     * be pushed back up until the character
     *
     * @param firstChar
     *            first character
     * @param text
     *            text being expected.
     * @param r
     *            reader
     * @return end comment
     */
    public static boolean isTextComing(final char firstChar, final String text,
            final PushbackReader r) throws IOException {

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
        return true;
    }

    /**
     * Prevent instantiation of utility class.
     */
    private ReaderUtil() {
    }
}
