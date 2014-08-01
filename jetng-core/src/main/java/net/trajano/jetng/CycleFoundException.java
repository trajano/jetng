package net.trajano.jetng;

import java.io.File;

@SuppressWarnings("serial")
public class CycleFoundException extends ParseException {
    public CycleFoundException(final File file, final ParserContext context) {
        super("Cycle found for file: " + file, context);
    }
}
