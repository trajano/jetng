package foo;

public class Bar {

    public String generate(final Object arguments) {
        final StringWriter w = new StringWriter();
        generate(arguments, w);
        return w.toString();
    }

    public void generate(final Object arguments, final PrintWriter out) {
        out.println("Hello world");
        for (int i = 0; i < 10; ++i) { 
            out.print("    Hello ");
            out.print(arguments);
            out.println();
        }
    }
}
