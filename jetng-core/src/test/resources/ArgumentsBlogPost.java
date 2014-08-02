package foo;

public class Bar {

    public String generate(final Meta arguments) {
        final java.io.StringWriter w = new java.io.StringWriter();
        generate(arguments, new java.io.PrintWriter(w));
        return w.toString();
    }

    public void generate(final Meta arguments, final java.io.PrintWriter out) {
        out.println("Hello world");
        out.print(arguments.getValue());
        out.println();
    }
}
