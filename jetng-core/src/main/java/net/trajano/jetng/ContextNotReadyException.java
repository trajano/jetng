package net.trajano.jetng;

/**
 * Context is not yet ready for writing, but some data is being attempted to be
 * written out.
 *
 * @author Archimedes
 *
 */
@SuppressWarnings("serial")
public class ContextNotReadyException extends ParseException {
    /**
     * Constructs the exception.
     *
     * @param context
     *            parsing context
     */
    public ContextNotReadyException(final ParserContext context) {
        super("Context not ready", context);
    }
}
