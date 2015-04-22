package net.trajano.apt.jpa.internal;

import java.util.*;
import javax.xml.*;

public class TableModuleGeneratorNg2 {

    public String generate(final MetaTableModule arguments) {
        final java.io.StringWriter w = new java.io.StringWriter();
        generate(arguments, w);
        return w.toString();
    }

    public void generate(final MetaTableModule arguments, final java.io.Writer writer) {
        final java.io.PrintWriter out = new java.io.PrintWriter(writer);
        out.print("package ");
        out.print(argument.getPackageName());
        out.println(';');
        out.println();
        out.println("import java.util.List;");
        out.println();
        out.println("import javax.ejb.Stateless;");
        out.println("import javax.persistence.EntityManager;");
        out.println("import javax.persistence.FlushModeType;");
        out.println("import javax.persistence.PersistenceContext;");
        out.println();
        out.println("/**");
        out.print(" * SLSB to manage {@link ");
        out.print(argument.getEntityClassName());
        out.println("} using the table model design pattern.");
        out.println(" */");
        out.println("@Stateless");
        out.print("public class ");
        out.print(argument.getClassName());
        out.println(" {");
        out.println("    /**");
        out.println("     * Entity manager.");
        out.println("     */");
        out.println("    @PersistenceContext");
        out.println("    private EntityManager em;");
        out.println();
        out.println("    /**");
        out.println("     * Gets a specific record.");
        out.println("     * ");
        out.println("     * @param id");
        out.println("     *            primary key");
        out.println("     * @return persisted bean");
        out.println("     */");
        out.print("    public ");
        out.print(argument.getEntityClassName());
        out.print(" get(final ");
        out.print(argument.getIdType());
        out.println(" id) {");
        out.print("        return em.find(");
        out.print(argument.getEntityClassName());
        out.println(".class, id);");
        out.println("    }");
        out.println();
        for (final MetaNamedQuery namedQuery : argument.getNamedQueries()) { 
            out.println("    /**");
            out.print("     * Execute named query ");
            out.print(namedQuery.getName());
            out.println('.');
            out.print("    ");
            for (int i = 0 ; i < namedQuery.getParameters().size(); ++i) { 
                out.print("     * @param ");
                out.print(namedQuery.getParameters().get(i));
                out.print(' ');
                out.print(namedQuery.getParameters().get(i));
                out.println(' ');
                out.print("    ");
            } 
            out.print("     * @return list of ");
            out.print(argument.getEntityClassName());
            out.println();
            out.println("     */");
            out.print("    public List<");
            out.print(argument.getEntityClassName());
            out.print("> ");
            out.print(namedQuery.getMethodName());
            out.print('(');
            for (int i = 0 ; i < namedQuery.getParameters().size(); ++i) { 
                out.print("final Object ");
                out.print(namedQuery.getParameters().get(i));
                if (i < namedQuery.getParameters().size() - 1) { 
                    out.print(',');
                } 
            } 
            out.println(") {");
            out.println("        return em");
            out.print("                .createNamedQuery(\"");
            out.print(namedQuery.getName());
            out.println("\",");
            out.print("                        ");
            out.print(argument.getEntityClassName());
            out.println(".class)");
            out.println("                .setFlushMode(FlushModeType.AUTO)");
            for (int i = 0 ; i < namedQuery.getParameters().size(); ++i) { 
                out.print("                .setParameter(\"");
                out.print(namedQuery.getParameters().get(i));
                out.print("\", ");
                out.print(namedQuery.getParameters().get(i));
                out.println(") ");
            } 
            out.println("                .getResultList();");
            out.println("    }");
            out.println();
        } 
        for (final MetaOperation operation: argument.getExtraOperations()) { 
            out.println("    /**");
            out.print("     * Execute extra operation ");
            out.print(operation.getMethodName());
            out.println('.');
            out.print("    ");
            for (int i = 0 ; i < operation.getParameterNames().size(); ++i) { 
                out.print("     * @param ");
                out.print(operation.getParameterNames().get(i));
                out.print(' ');
                out.print(operation.getParameterNames().get(i));
                out.println(' ');
                out.print("    ");
            } 
            out.print("     * @return ");
            out.print(operation.getReturnType());
            out.println(' ');
            out.println("     */");
            out.print("    public ");
            out.print(operation.getReturnType());
            out.print(' ');
            out.print(operation.getMethodName());
            out.print('(');
            for (int i = 0 ; i < operation.getParameterDeclarations().size(); ++i) { 
                out.print(operation.getParameterDeclarations().get(i));
                if (i < operation.getParameterDeclarations().size() - 1) { 
                    out.print(',');
                } 
            } 
            out.println(')');
            out.print("    ");
            if (!operation.getThrownTypes().isEmpty()) {
                out.print("      throws ");
                for (int i = 0 ; i < operation.getThrownTypes().size(); ++i) { 
                    out.print(operation.getThrownTypes().get(i));
                    if (i < operation.getThrownTypes().size() - 1) { 
                        out.print(',');
                    } 
                } 
                out.print("    ");
            } 
            out.println("    {");
            out.print("    ");
            if (!"void".equals(operation.getReturnType())) {
                out.println("        return ");
                out.print("    ");
            } 
            out.print("        ");
            out.print(argument.getEntityClassName());
            out.print('.');
            out.print(operation.getMethodName());
            out.print("(em");
            for (int i = 0 ; i < operation.getParameterNames().size(); ++i) { 
                out.print(", ");
                out.print(operation.getParameterNames().get(i));
            } 
            out.println(");");
            out.println("    }");
            out.println();
        } 
        out.println("    /**");
        out.println("     * Save, flush and refresh a persisted bean. The flush and refreshing is");
        out.println("     * needed to ensure that the temporal data contains the proper values.");
        out.println("     * ");
        out.println("     * @param bean");
        out.println("     *            bean to persist.");
        out.println("     */");
        out.print("    public void save(final ");
        out.print(argument.getEntityClassName());
        out.println(" bean) {");
        out.println("        em.persist(bean);");
        out.println("        em.flush();");
        out.println("        em.refresh(bean);");
        out.println("    }");
        out.println();
        out.println("    /**");
        out.println("     * Removes the bean from the persistence.");
        out.println("     * ");
        out.println("     * @param bean");
        out.println("     *            bean to remove.");
        out.println("     */");
        out.print("    public void remove(final ");
        out.print(argument.getEntityClassName());
        out.println(" bean) {");
        out.println("        em.persist(bean);");
        out.println("        em.flush();");
        out.println("    }");
        out.println();
        out.println("    /**");
        out.println("     * Injects the entity manager for testing.");
        out.println("     * ");
        out.println("     * @param entityManager");
        out.println("     *            entity manager.");
        out.println("     */");
        out.println("    public void setEntityManager(final EntityManager entityManager) {");
        out.println("        em = entityManager;");
        out.println("    }");
        out.println('}');
    }
}
