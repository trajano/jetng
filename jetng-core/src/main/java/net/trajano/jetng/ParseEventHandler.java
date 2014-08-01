package net.trajano.jetng;

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
     */
    void characters(ParserContext context, String characters, boolean eol,
            boolean aloneOnLine);

    /**
     * Characters in a comment.
     *
     * @param comment
     *            characters
     * @parm eol needs end of line.
     */
    void comment(ParserContext context, String comment, boolean eol);

    /**
     * Handles JET directives.
     *
     * @param context
     * @param directiveName
     * @param attributes
     */
    void directive(ParserContext context, String directiveName,
            Map<String, String> attributes);

    void endComment(ParserContext context);

    void endDocument(ParserContext context);

    void endExpression(ParserContext context);

    void endScriptlet(ParserContext context);

    void expression(ParserContext context, String expression);

    /**
     * Characters in a scriptlet.
     *
     * @param scriptlet
     *            characters
     * @parm eol needs end of line.
     */
    void scriptlet(ParserContext context, String scriptlet, boolean eol);

    void startComment(ParserContext context);

    void startDocument(ParserContext context);

    /**
     * Fired at the start of an expression.
     */
    void startExpression(ParserContext context);

    void startScriptlet(ParserContext context);
}
