package foo;

public class Bar {

    public String generate(final Object arguments) {
        final java.io.StringWriter w = new java.io.StringWriter();
        generate(arguments, new java.io.PrintWriter(w));
        return w.toString();
    }

    public void generate(final Object arguments, final java.io.PrintWriter out) {
        out.println("Hello world");
         Meta meta = (Meta)arguments; 
        out.print(meta.getValue());
        out.println();
    }
}
