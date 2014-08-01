package foo;

public class Bar {

    public String generate(final Meta arguments) {
        final StringWriter w = new StringWriter();
        generate(arguments, w);
        return w.toString();
    }

    public void generate(final Meta arguments, final PrintWriter out) {
        out.println("Hello world");
        out.print(arguments.getValue());
        out.println();
    }
}
