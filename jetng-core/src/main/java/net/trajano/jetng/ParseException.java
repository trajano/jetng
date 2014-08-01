package net.trajano.jetng;

import java.io.IOException;

/**
 * Root exception for JET parsing problems.
 *
 * @author Archimedes
 *
 */
@SuppressWarnings("serial")
public class ParseException extends IOException {
    /**
     * Parser context.
     */
    private final ParserContext context;

    /**
     * Constructs the exception.
     *
     * @param message
     *            message
     * @param context
     *            context
     */
    public ParseException(final String message, final ParserContext context) {
        super(message + " at " + context.getCurrentFilePosition());
        this.context = context;
    }

    /**
     * Gets the parser context.
     *
     * @return parser context
     */
    public ParserContext getContext() {
        return context;

    }
}
