package net.trajano.jetng;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handles parse events.
 *
 * @author Archimedes
 *
 */
public class DefaultParseEventHandler implements ParseEventHandler {
    /**
     * Flag indicating the {@link #header(ParserContext)} has been called.
     */
    private boolean headerSent;

    @Override
    public final void characters(final ParserContext context,
            final String characters, final boolean eol,
            final boolean aloneOnLine) throws IOException {
        if (!isContextReadyForWriting(context)) {
            throw new ContextNotReadyException(context);
        }
        doCharacters(context, characters, eol, aloneOnLine);

    }

    @Override
    public void comment(final ParserContext context, final String comment,
            final boolean eol) throws IOException {
        if (!isContextReadyForWriting(context)) {
            throw new ContextNotReadyException(context);
        }
        doComment(context, comment, eol);
    }

    @Override
    public void directive(final ParserContext context,
            final String directiveName, final Map<String, String> attributes)
                    throws IOException {
        if ("jet".equals(directiveName)) {
            handleJetDirective(context, attributes);
            if (!headerSent) {
                if (!isContextReadyForWriting(context)) {
                    throw new ContextNotReadyException(context);
                }
                header(context);
                headerSent = true;
            }
        } else if ("include".equals(directiveName)) {
            handleIncludeDirective(context, attributes);
        } else if ("taglib".equals(directiveName)) {
            // TODO implement taglib support
            throw new ParseException("Unsupported directive:" + directiveName,
                    context);
        } else {
            throw new ParseException("Unsupported directive:" + directiveName,
                    context);
        }
    }

    /**
     *
     * @param context
     * @param characters
     * @param eol
     * @param aloneOnLine
     */
    protected void doCharacters(final ParserContext context,
            final String characters, final boolean eol,
            final boolean aloneOnLine) {
    }

    public void doComment(final ParserContext context, final String comment,
            final boolean eol) throws IOException {
    }

    protected void doEndDocument(final ParserContext context) {
    }

    @Override
    public void endComment(final ParserContext context) {
    }

    @Override
    public final void endDocument(final ParserContext context)
            throws IOException {
        if (context.getIndentLevel() != 0) {
            throw new ParseException(
                    "Indent level is not at zero at the end of the document",
                    context);
        }
        doEndDocument(context);
    }

    @Override
    public void endExpression(final ParserContext context) {
    }

    @Override
    public void endScriptlet(final ParserContext context) {
    }

    @Override
    public void expression(final ParserContext context, final String expression) {
    }

    /**
     * Handle include directive.
     *
     * @param context
     * @param attributes
     * @throws IOException
     */
    private void handleIncludeDirective(final ParserContext context,
            final Map<String, String> attributes) throws IOException {

        context.pushFile(attributes.get("file"));
        new JetNgParser(context.getCurrentFilePosition().getFile(), this, 6)
        .parse(context);
        context.popFile();
        context.setStartTag(context.getCurrentFilePosition().getStartTag());
        context.setEndTag(context.getCurrentFilePosition().getEndTag());
    }

    /**
     * This will throw a parse exception if anything but startTag or endTag is
     * set.
     *
     * @param context
     * @param attributes
     */
    private void handleJetDirective(final ParserContext context,
            final Map<String, String> attributes) throws IOException {
        if (attributes.get("startTag") != null) {
            context.setStartTag(attributes.get("startTag"));
        }
        if (attributes.get("endTag") != null) {
            context.setEndTag(attributes.get("endTag"));
        }
        final Set<String> attributeNames = new HashSet<String>(
                attributes.keySet());
        attributeNames.remove("startTag");
        attributeNames.remove("endTag");
        if (!attributeNames.isEmpty() && headerSent) {
            throw new ParseException(
                    "Unexpected JET directives attributes found "
                            + attributeNames, context);
        }
        if (attributes.get("imports") != null) {
            context.addImports(attributes.get("imports").split("\\s"));
            attributeNames.remove("imports");
        }
        if (attributes.get("package") != null) {
            context.setPackage(attributes.get("package"));
            attributeNames.remove("package");
        }
        if (attributes.get("class") != null) {
            context.setClassName(attributes.get("class"));
            attributeNames.remove("class");
        }
        if (attributes.get("argumentsClass") != null) {
            context.setArgumentsClassName(attributes.get("argumentsClass"));
            attributeNames.remove("argumentsClass");
        }
        if (!attributeNames.isEmpty()) {
            throw new ParseException("Unsupported jet directive attributes  "
                    + attributeNames, context);
        }
    }

    /**
     * This is called by {@link #directive(ParserContext, String, Map)} just
     * after the first jet directive is handled.
     *
     * @param context
     *            context
     */
    public void header(final ParserContext context) {
    }

    /**
     * Check if the context is ready for writing. The class and package names
     * have to be set for it to be ready for writing.
     *
     * @param context
     *            context to evaluate
     * @return <code>true</code> if the context is ready for writing.
     */
    private boolean isContextReadyForWriting(final ParserContext context) {
        return context.getClassName() != null && context.getPackage() != null;
    }

    @Override
    public void scriptlet(final ParserContext context, final String scriptlet,
            final boolean eol) {
    }

    @Override
    public void startComment(final ParserContext context) {
    }

    @Override
    public void startDocument(final ParserContext context) {
    }

    @Override
    public void startExpression(final ParserContext context) {
    }

    @Override
    public void startScriptlet(final ParserContext context) {
    }
}
