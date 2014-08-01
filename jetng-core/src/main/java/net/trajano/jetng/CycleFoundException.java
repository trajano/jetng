package net.trajano.jetng;

import java.io.File;

/**
 * Fired when a cycle is found.
 *
 * @author Archimedes
 *
 */
@SuppressWarnings("serial")
public class CycleFoundException extends ParseException {
    /**
     * Constructs the exception.
     *
     * @param file
     *            file causing the cycle
     * @param context
     *            parsing context
     */
    public CycleFoundException(final File file, final ParserContext context) {
        super("Cycle found for file: " + file, context);
    }
}
