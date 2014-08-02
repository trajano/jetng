package net.trajano.jetng;

import static net.trajano.jetng.internal.Util.isTextComing;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.trajano.jetng.internal.DefaultParserContext;

/**
 * Parser.
 *
 * @author Archimedes
 *
 */
public class JetNgParser {
    /**
     * File going to be parsed.
     */
    private final File file;

    /**
     * Parse event handler.
     */
    private final ParseEventHandler handler;

    /**
     * Reader.
     */
    private final PushbackReader reader;

    /**
     * Constructs the parser with specified tag limit size.
     *
     * @param file
     *            file
     * @param handler
     *            handler
     * @param size
     *            maximum size for the tag.
     */
    public JetNgParser(final File file, final ParseEventHandler handler,
            final int size) throws IOException {
        reader = new PushbackReader(new FileReader(file), size + 2);
        this.handler = handler;
        this.file = file;
    }

    /**
     * Constructs the parser with the default maximum tag size of 5.
     *
     * @param reader
     *            reader
     * @param handler
     *            handler
     */
    public JetNgParser(final Reader reader, final ParseEventHandler handler) {
        this(reader, handler, 5);
    }

    /**
     * Constructs the parser with specified tag limit size.
     *
     * @param reader
     *            reader
     * @param handler
     *            handler
     * @param size
     *            maximum size for the tag.
     */
    public JetNgParser(final Reader reader, final ParseEventHandler handler,
            final int size) {
        this.reader = new PushbackReader(reader, size + 2);
        this.handler = handler;
        file = null;
    }

    /**
     * Checks if this is a comment. Just checks if the next character is a "-".
     *
     * @param context
     *            parsing context
     * @param r
     *            reader
     * @return true if it is a comment.
     * @throws IOException
     */
    private boolean isComment(final ParserContext context,
            final PushbackReader r) throws IOException {
        final int c = r.read();
        if (c == '-') {
            context.inc();
            return true;
        }
        r.unread(c);
        return false;
    }

    /**
     * Parse creating new context.
     *
     * @return context that was used
     * @throws IOException
     */
    public ParserContext parse() throws IOException {
        if (file == null) {
            return parse(new DefaultParserContext());
        } else {
            return parse(new DefaultParserContext(file));
        }
    }

    /**
     * Parse using an existing context.
     *
     * @param context
     *            parsing context
     * @return context that was used
     * @throws IOException
     */
    public ParserContext parse(final ParserContext context) throws IOException {
        handler.startDocument(context);
        final StringBuilder currentCharacters = new StringBuilder();
        int c = reader.read();
        boolean directivesInLine = false;
        while (c != -1) {
            context.inc();
            if (c == '\n') {
                context.nl();
                handler.characters(context, currentCharacters.toString(), true,
                        directivesInLine
                        && currentCharacters.toString().trim()
                        .isEmpty());
                currentCharacters.setLength(0);
                directivesInLine = false;
            } else if (isTextComing(context, (char) c, context.getStartTag(),
                    reader)) {
                if (!currentCharacters.toString().trim().isEmpty()) {
                    handler.characters(context, currentCharacters.toString(),
                            false, false);
                    currentCharacters.setLength(0);
                }
                c = reader.read();
                if (c == '@') {
                    context.inc();
                    processDirective(context, reader);
                    directivesInLine = true;
                } else if (c == '=') {
                    context.inc();
                    processExpression(context, reader);
                } else if (c == '-') {
                    context.inc();
                    if (isComment(context, reader)) {
                        processComment(context, reader);
                    } else {
                        context.unindent();
                        processScriptlet(context, reader, true);
                    }
                    directivesInLine = true;
                } else if (c == '+') {
                    context.inc();
                    processScriptlet(context, reader, true);
                    context.indent();
                    directivesInLine = true;
                } else {
                    reader.unread(c);
                    processScriptlet(context, reader, false);
                    directivesInLine = true;
                }
            } else if (c != '\r') {
                // ignore CR.
                currentCharacters.append((char) c);
            }
            c = reader.read();
        }
        if (currentCharacters.length() > 0) {
            handler.characters(context, currentCharacters.toString(), false,
                    directivesInLine
                    && currentCharacters.toString().trim().isEmpty());
        }
        handler.endDocument(context);
        return context;
    }

    /**
     * Process JET comments.
     *
     * @param context
     *            parsing context
     * @param r
     *            reader
     * @throws IOException
     */
    private void processComment(final ParserContext context,
            final PushbackReader r) throws IOException {
        handler.startComment(context);
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            context.inc();
            if (c == '\n') {
                context.nl();
                handler.comment(context, b.toString(), true);
                b.setLength(0);
            } else if (isTextComing(context, (char) c,
                    context.getEndCommentTag(), r)) {
                final String text = b.toString();
                handler.comment(context, text, false);
                handler.endComment(context);
                return;
            } else {
                b.append((char) c);
            }
            c = r.read();
        }
        throw new EOFException();
    }

    /**
     * Process directive.
     *
     * @param context
     *            parsing context
     * @param r
     *            reader
     * @throws IOException
     */
    private void processDirective(final ParserContext context,
            final PushbackReader r) throws IOException {
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            context.inc();
            if (c == '\n') {
                context.nl();
            }
            if (isTextComing(context, (char) c, context.getEndTag(), r)) {

                final String directiveText = b.toString().trim();
                final Pattern directiveNamePattern = Pattern
                        .compile("[A-Za-z]+");
                final Matcher m = directiveNamePattern.matcher(directiveText);
                if (!m.find()) {
                    throw new IOException("unable to find directive name");
                }
                final String directiveName = m.group();
                m.usePattern(Pattern
                        .compile("[ \\t\\r\\n]+([a-zA-Z]+)[ \\t\\r\\n]*=[ \\t\\r\\n]*('([^']+)'|\"([^\"]+)\")"));
                final Map<String, String> attributes = new HashMap<String, String>();
                while (m.find()) {
                    attributes.put(m.group(1),
                            m.group(2).substring(1, m.group(2).length() - 1));
                }
                handler.directive(context, directiveName, attributes);
                context.getCurrentFilePosition().setTags(context.getStartTag(),
                        context.getEndTag());
                return;
            } else {
                b.append((char) c);
            }
            c = r.read();
        }
    }

    /**
     * Process expression element.
     *
     * @param context
     *            parsing context
     * @param r
     *            reader
     * @throws IOException
     *             I/O Exception.
     */
    private void processExpression(final ParserContext context,
            final PushbackReader r) throws IOException {
        handler.startExpression(context);
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            context.inc();
            if (c == '\n') {
                context.nl();
            }
            if (isTextComing(context, (char) c, context.getEndTag(), r)) {
                handler.expression(context, b.toString().trim());
                handler.endExpression(context);
                return;
            } else {
                b.append((char) c);
            }
            c = r.read();
        }
        throw new EOFException();
    }

    /**
     * Process a scriptlet element.
     *
     * @param context
     *            parsing context
     * @param r
     *            reader
     * @param trim
     *            trims the initial whitespace if any
     * @throws IOException
     *             I/O Exception
     */
    private void processScriptlet(final ParserContext context,
            final PushbackReader r, final boolean trim) throws IOException {
        handler.startScriptlet(context);
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            context.inc();
            if (c == '\n') {
                context.nl();
                handler.scriptlet(context, b.toString(), true);
                b.setLength(0);
            } else if (isTextComing(context, (char) c, context.getEndTag(), r)) {
                final String text = b.toString();
                handler.scriptlet(context, text, false);
                handler.endScriptlet(context);
                return;
            } else {
                if (!(trim && b.length() == 0 && (c == ' ' || c == '\t'
                        || c == '\r' || c == '\n'))) {

                    b.append((char) c);
                }
            }
            c = r.read();
        }
        throw new EOFException();
    }
}
