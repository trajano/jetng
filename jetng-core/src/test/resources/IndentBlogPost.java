package foo;

/**
 * Bar template.
 */
public class Bar {

    /**
     * Generates Bar template.
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
     * Writes Bar template to a {@link java.io.Writer}.
     *
     * @param arguments
     *            arguments
     * @param writer
     *            writer to output to
     */
    public void generate(final Object arguments, final java.io.Writer writer) {
        final java.io.PrintWriter out = new java.io.PrintWriter(writer);
        out.println("Hello world");
        for (int i = 0; i < 10; ++i) { 
            out.print("    Hello ");
            out.print(arguments);
            out.println();
        }
    }
}
