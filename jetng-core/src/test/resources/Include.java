package net.trajano.apt.jpa.internal;

public class TableModuleGenerator {

    public String generate(final Object arguments) {
        final java.io.StringWriter w = new java.io.StringWriter();
        generate(arguments, new java.io.PrintWriter(w));
        return w.toString();
    }

    public void generate(final Object arguments, final java.io.PrintWriter out) {
        out.println("This is the top level file");
        out.println("This is included");
        out.println("This is the top level file");
        out.print("This is included with ");
        out.print(custom);
        out.println(" tags.");
        out.println("This is the top level file");
        out.println("This is included");
        out.println("This is the top level file");
    }
}