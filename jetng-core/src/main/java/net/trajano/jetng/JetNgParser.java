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
     * Parser context.
     */
    private DefaultParserContext context;

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
     * @param r
     *            reader.
     * @return true if it is a comment.
     * @throws IOException
     */
    private boolean isComment(final PushbackReader r) throws IOException {
        final int c = r.read();
        if (c == '-') {
            return true;
        }
        r.unread(c);
        return false;
    }

    /**
     * Parse.
     *
     * @throws IOException
     */
    public void parse() throws IOException {
        if (file == null) {
            context = new DefaultParserContext();
        } else {
            context = new DefaultParserContext(file);
        }
        handler.startDocument(context);
        final StringBuilder currentCharacters = new StringBuilder();
        int c = reader.read();
        boolean directivesInLine = false;
        while (c != -1) {
            if (c == '\n') {
                handler.characters(context, currentCharacters.toString(), true,
                        directivesInLine
                        && currentCharacters.toString().trim()
                        .isEmpty());
                currentCharacters.setLength(0);
                directivesInLine = false;
            } else if (isTextComing((char) c, context.getStartTag(), reader)) {
                if (!currentCharacters.toString().trim().isEmpty()) {
                    handler.characters(context, currentCharacters.toString(),
                            false, false);
                    currentCharacters.setLength(0);
                }
                c = reader.read();
                if (c == '@') {
                    processDirective(reader);
                    directivesInLine = true;
                } else if (c == '=') {
                    processExpression(reader);
                } else if (c == '-') {
                    if (isComment(reader)) {
                        processComment(reader);
                    } else {
                        context.unindent();
                        processScriptlet(reader, true);
                    }
                    directivesInLine = true;
                } else if (c == '+') {
                    processScriptlet(reader, true);
                    context.indent();
                    directivesInLine = true;
                } else {
                    processScriptlet(reader, false);
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
    }

    /**
     * Process JET comments.
     *
     * @param r
     *            reader
     * @throws IOException
     */
    private void processComment(final PushbackReader r) throws IOException {
        handler.startComment(context);
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            if (c == '\n') {
                handler.comment(context, b.toString(), true);
                b.setLength(0);
            } else if (isTextComing((char) c, context.getEndCommentTag(), r)) {
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
     * @param r
     *            reader
     * @throws IOException
     */
    private void processDirective(final PushbackReader r) throws IOException {
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            if (isTextComing((char) c, context.getEndTag(), r)) {

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
     * @param r
     *            reader
     * @throws IOException
     *             I/O Exception.
     */
    private void processExpression(final PushbackReader r) throws IOException {
        handler.startExpression(context);
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            if (isTextComing((char) c, context.getEndTag(), r)) {
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
     * @param r
     *            reader
     * @param trim
     *            trims the initial whitespace if any
     * @throws IOException
     *             I/O Exception
     */
    private void processScriptlet(final PushbackReader r, final boolean trim)
            throws IOException {
        handler.startScriptlet(context);
        final StringBuilder b = new StringBuilder();
        int c = r.read();
        while (c != -1) {
            if (c == '\n') {
                handler.scriptlet(context, b.toString(), true);
                b.setLength(0);
            } else if (isTextComing((char) c, context.getEndTag(), r)) {
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
