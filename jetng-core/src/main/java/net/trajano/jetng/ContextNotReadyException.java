package net.trajano.jetng;

@SuppressWarnings("serial")
public class ContextNotReadyException extends ParseException {
    public ContextNotReadyException(final ParserContext context) {
        super("Context not ready", context);
    }
}
