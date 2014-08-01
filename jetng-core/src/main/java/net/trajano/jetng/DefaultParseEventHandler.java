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
    private boolean headerSent = false;

    @Override
    public void characters(final ParserContext context,
            final String characters, final boolean eol,
            final boolean aloneOnLine) {
    }

    @Override
    public void comment(final ParserContext context, final String comment,
            final boolean eol) {
    }

    @Override
    public void directive(final ParserContext context,
            final String directiveName, final Map<String, String> attributes)
                    throws IOException {
        if ("jet".equals(directiveName)) {
            handleJetDirective(context, attributes);
            if (!headerSent) {
                header(context);
                headerSent = true;
            }
        } else if ("include".equals(directiveName)) {
            handleJetDirective(context, attributes);
        } else if ("taglib".equals(directiveName)) {
            // TODO implement taglib support
            throw new ParseException("Unsupported directive:" + directiveName,
                    context);
        } else {
            throw new ParseException("Unsupported directive:" + directiveName,
                    context);
        }
    }

    @Override
    public void endComment(final ParserContext context) {
    }

    @Override
    public void endDocument(final ParserContext context) {
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
            context.setStartTag(attributes.get("endTag"));
        }
        final Set<String> attributeNames = new HashSet<String>(
                attributes.keySet());
        attributeNames.remove("startTag");
        attributeNames.remove("endTag");
        if (!attributeNames.isEmpty() && headerSent) {
            throw new ParseException(
                    "JET directives found that cannot be in included file "
                            + attributeNames, context);
        }
        if (attributes.get("imports") != null) {
            context.addImports(attributes.get("imports").split("\\s"));
        }
        if (attributes.get("package") != null) {
            context.setPackage(attributes.get("package"));
        }
        if (attributes.get("class") != null) {
            context.setClassName(attributes.get("class"));
        }
        if (attributes.get("objectClass") != null) {
            context.setObjectClassName(attributes.get("objectClass"));
        }
    }

    /**
     * This is called by {@link #directive(ParserContext, String, Map)} just
     * after the first jet directive is handled.
     *
     * @param context
     */
    public void header(final ParserContext context) {
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
