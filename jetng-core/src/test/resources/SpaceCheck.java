package net.trajano.apt.jpa.internal;

/**
 * TableModuleGenerator template.
 */
public class TableModuleGenerator {

    /**
     * Generates TableModuleGenerator template.
     *
     * @param arguments
     *            arguments
     * @return generated template
     */
    public String generate(final Object arguments) {
        final java.io.StringWriter w = new java.io.StringWriter();
        generate(arguments, w);
        return w.toString();
    }

    /**
     * Writes TableModuleGenerator template to a {@link java.io.Writer}.
     *
     * @param arguments
     *            arguments
     * @param writer
     *            writer to output to
     */
    public void generate(final Object arguments, final java.io.Writer writer) {
        final java.io.PrintWriter out = new java.io.PrintWriter(writer);
        out.print("public ");
        out.print(operation.getReturnType());
        out.print(' ');
        out.print(operation.getMethodName());
        out.println();
    }
}
