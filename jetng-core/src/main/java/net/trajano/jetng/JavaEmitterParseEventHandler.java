package net.trajano.jetng;

import static java.lang.String.format;

import java.io.PrintWriter;
import java.util.Arrays;

import net.trajano.jetng.internal.Util;

/**
 * Handles parse events.
 *
 * @author Archimedes Trajano
 *
 */
public class JavaEmitterParseEventHandler extends DefaultParseEventHandler {

    /**
     * Writer.
     */
    private final PrintWriter out;

    /**
     * Constructs the handler with a given writer.
     *
     * @param out
     *            writer
     */
    public JavaEmitterParseEventHandler(final PrintWriter out) {
        this.out = out;
    }

    @Override
    public void doCharacters(final ParserContext context, final String characters, final boolean eol,
            final boolean aloneOnLine) {
        if (aloneOnLine) {
            return;
        }
        if (eol) {
            out.println(
                    indents(2 + context.getIndentLevel()) + "out.println(" + Util.escapeJavaString(characters) + ");");
        } else {
            out.println(
                    indents(2 + context.getIndentLevel()) + "out.print(" + Util.escapeJavaString(characters) + ");");
        }
    }

    @Override
    public void doEndDocument(final ParserContext context) {
        if (context.isTopFile()) {
            out.println("    }");
            out.println("}");
        }
    }

    @Override
    public void expression(final ParserContext context, final String expression) {
        out.println(indents(2 + context.getIndentLevel()) + "out.print(" + expression + ");");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void header(final ParserContext context) {
        out.println("package " + context.getPackageName() + ";");
        if (!context.getImports().isEmpty()) {
            out.println();
            for (final String importPackage : context.getImports()) {
                out.println("import " + importPackage + ";");
            }
        }
        out.println();
        out.println("public class " + context.getClassName() + " {");

        out.println();
        out.println(format("    public String generate(final %s arguments) {", context.getArgumentsClassName()));
        out.println("        final java.io.StringWriter w = new java.io.StringWriter();");
        out.println("        generate(arguments, w);");
        out.println("        return w.toString();");
        out.println("    }");

        out.println();
        out.println(format("    public void generate(final %s arguments, final java.io.Writer writer) {",
                context.getArgumentsClassName()));
        out.println("        final java.io.PrintWriter out = new java.io.PrintWriter(writer);");

    }

    /**
     * Build indentation string.
     *
     * @param level
     *            indentation level
     * @return indent.
     */
    private String indents(final int level) {
        final char[] carray = new char[4 * level];
        Arrays.fill(carray, ' ');
        return new String(carray);
    }

    @Override
    public void scriptlet(final ParserContext context, final String scriptlet, final boolean eol) {
        out.println(indents(2 + context.getIndentLevel()) + scriptlet);
    }
}
