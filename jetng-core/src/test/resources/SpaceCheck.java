package net.trajano.apt.jpa.internal;

public class TableModuleGenerator {

    public String generate(final Object arguments) {
        final java.io.StringWriter w = new java.io.StringWriter();
        generate(arguments, new java.io.PrintWriter(w));
        return w.toString();
    }

    public void generate(final Object arguments, final java.io.PrintWriter out) {
        out.print("public ");
        out.print(operation.getReturnType());
        out.print(' ');
        out.print(operation.getMethodName());
        out.println();
    }
}
