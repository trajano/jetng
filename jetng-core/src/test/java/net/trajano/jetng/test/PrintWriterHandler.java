package net.trajano.jetng.test;

import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import net.trajano.jetng.ParseEventHandler;
import net.trajano.jetng.ParserContext;

/**
 * This will print out the JET file with a few extra comments.
 *
 * @author Archimedes Trajano
 *
 */
public class PrintWriterHandler implements ParseEventHandler {
    private final PrintWriter out;

    public PrintWriterHandler(final PrintWriter out) {
        this.out = out;
    }

    @Override
    public void characters(final ParserContext context,
            final String characters, final boolean eol,
            final boolean aloneOnLine) {
        if (aloneOnLine) {
            return;
        }
        if (eol) {
            out.println(characters);
        } else {
            out.print(characters);
        }
    }

    @Override
    public void comment(final ParserContext context, final String comment,
            final boolean eol) {
        if (eol) {
            out.println(comment);
        } else {
            out.print(comment);
        }
    }

    @Override
    public void directive(final ParserContext context,
            final String directiveName, final Map<String, String> attributes) {
        out.print("<%@ " + directiveName + " "
                + new TreeMap<String, String>(attributes) + "%>");
    }

    @Override
    public void endComment(final ParserContext context) {
        out.print("--%>");
    }

    @Override
    public void endDocument(final ParserContext context) {
        out.println("<%--endDocument--%>");
    }

    @Override
    public void endExpression(final ParserContext context) {
        out.print("%>");

    }

    @Override
    public void endScriptlet(final ParserContext context) {
        out.print("%>");
    }

    @Override
    public void expression(final ParserContext context, final String expression) {
        out.print(expression);

    }

    @Override
    public void scriptlet(final ParserContext context, final String scriptlet,
            final boolean eol) {
        if (eol) {
            out.println(scriptlet);
        } else {
            out.print(scriptlet);
        }
    }

    @Override
    public void startComment(final ParserContext context) {
        out.print("<%--");
    }

    @Override
    public void startDocument(final ParserContext context) {
        out.println("<%--startDocument--%>");
    }

    @Override
    public void startExpression(final ParserContext context) {
        out.print("<%=");
    }

    @Override
    public void startScriptlet(final ParserContext context) {
        out.print("<%");
    }
}
