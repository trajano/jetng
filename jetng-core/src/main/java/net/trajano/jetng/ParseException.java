package net.trajano.jetng;

import java.io.IOException;

@SuppressWarnings("serial")
public class ParseException extends IOException {
    private final ParserContext context;

    public ParseException(final String message, final ParserContext context) {
        super(message + " at " + context.getCurrentFilePosition());
        this.context = context;
    }

    public ParserContext getContext() {
        return context;

    }
}
