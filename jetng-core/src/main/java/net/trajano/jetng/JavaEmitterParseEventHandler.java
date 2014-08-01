package net.trajano.jetng;

import static java.lang.String.format;

import java.io.PrintWriter;
import java.util.Arrays;

import net.trajano.jetng.internal.Util;

/**
 * Handles parse events.
 *
 * @author Archimedes
 *
 */
public class JavaEmitterParseEventHandler extends DefaultParseEventHandler {

    private final PrintWriter out;

    public JavaEmitterParseEventHandler(final PrintWriter out) {
        this.out = out;
    }

    @Override
    public void doCharacters(final ParserContext context,
            final String characters, final boolean eol,
            final boolean aloneOnLine) {
        if (aloneOnLine) {
            return;
        }
        if (eol) {
            out.println(indents(2 + context.getIndentLevel()) + "out.println("
                    + Util.escapeJavaString(characters) + ");");
        } else {
            out.println(indents(2 + context.getIndentLevel()) + "out.print("
                    + Util.escapeJavaString(characters) + ");");
        }
    }

    @Override
    public void doComment(final ParserContext context, final String comment,
            final boolean eol) {
    }

    @Override
    public void doEndDocument(final ParserContext context) {
        if (context.isTopFile()) {
            out.println("    }");
            out.println("}");
        }
    }

    @Override
    public void endComment(final ParserContext context) {
    }

    @Override
    public void endExpression(final ParserContext context) {
    }

    @Override
    public void endScriptlet(final ParserContext context) {
    }

    @Override
    public void expression(final ParserContext context, final String expression) {
        out.println(indents(2 + context.getIndentLevel()) + "out.print("
                + expression + ");");
    }

    @Override
    public void header(final ParserContext context) {
        out.println("package " + context.getPackage() + ";");
        if (!context.getImports().isEmpty()) {
            out.println();
            for (final String importPackage : context.getImports()) {
                out.println("import " + importPackage + ";");
            }
        }
        out.println();
        out.println("public class " + context.getClassName() + " {");

        out.println();
        out.println(format("    public String generate(final %s arguments) {",
                context.getArgumentsClassName()));
        out.println("        final StringWriter w = new StringWriter();");
        out.println("        generate(arguments, w);");
        out.println("        return w.toString();");
        out.println("    }");

        out.println();
        out.println(format(
                "    public void generate(final %s arguments, final PrintWriter out) {",
                context.getArgumentsClassName()));

    }

    /**
     * Build indentation string.
     *
     * @param level
     * @return
     */
    private String indents(final int level) {
        final char[] carray = new char[4 * level];
        Arrays.fill(carray, ' ');
        return new String(carray);
    }

    @Override
    public void scriptlet(final ParserContext context, final String scriptlet,
            final boolean eol) {
        out.println(indents(2 + context.getIndentLevel()) + scriptlet);
    }
}
