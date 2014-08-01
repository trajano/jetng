package foo;

public class Bar {

    public String generate(final Object arguments) {
        final StringWriter w = new StringWriter();
        generate(arguments, w);
        return w.toString();
    }

    public void generate(final Object arguments, final PrintWriter out) {
        out.println("Hello world");
         Meta meta = (Meta)arguments; 
        out.print(meta.getValue());
        out.println();
    }
}
