package net.trajano.jetng;

import java.io.IOException;

@SuppressWarnings("serial")
public class ParseException extends IOException {
    public ParseException(final String message, final ParserContext context) {
        super(message);
    }
}
