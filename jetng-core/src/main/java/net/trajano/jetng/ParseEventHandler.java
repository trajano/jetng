package net.trajano.jetng;

import java.io.IOException;
import java.util.Map;

/**
 * Handles parse events.
 *
 * @author Archimedes
 *
 */
public interface ParseEventHandler {
    /**
     * Unprocessed stream of characters. Alone on the line is implemented as the
     * element starts on a line in which preceding text in only whitespace, and
     * ends on a line that is only whitespace.
     *
     * @param characters
     *            characters
     * @param eol
     *            needs end of line.
     * @param aloneOnLine
     *            flag for alone on the line.
     * @throws IOException
     */
    void characters(ParserContext context, String characters, boolean eol,
            boolean aloneOnLine) throws IOException;

    /**
     * Characters in a comment.
     *
     * @param comment
     *            characters
     * @parm eol needs end of line.
     */
    void comment(ParserContext context, String comment, boolean eol)
            throws IOException;

    /**
     * Handles JET directives.
     *
     * @param context
     * @param directiveName
     * @param attributes
     * @throws IOException
     */
    void directive(ParserContext context, String directiveName,
            Map<String, String> attributes) throws IOException;

    void endComment(ParserContext context) throws IOException;

    /**
     * Checks if the indent level is at zero otherwise throws an exception
     * before ending the document.
     */
    void endDocument(ParserContext context) throws IOException;

    void endExpression(ParserContext context) throws IOException;

    void endScriptlet(ParserContext context) throws IOException;

    void expression(ParserContext context, String expression)
            throws IOException;

    /**
     * Characters in a scriptlet.
     *
     * @param scriptlet
     *            characters
     * @parm eol needs end of line.
     */
    void scriptlet(ParserContext context, String scriptlet, boolean eol)
            throws IOException;

    void startComment(ParserContext context) throws IOException;

    void startDocument(ParserContext context) throws IOException;

    /**
     * Fired at the start of an expression.
     *
     * @param context
     *            parsing context
     */
    void startExpression(ParserContext context) throws IOException;

    /**
     * Fired at the start of a scriptlet.
     *
     * @param context
     *            parsing context
     * @throws IOException
     */
    void startScriptlet(ParserContext context) throws IOException;
}
