package net.trajano.jetng;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.trajano.jetng.internal.FilePosition;

/**
 * Context for the JET parser.
 *
 * @author Archimedes Trajano
 */
public interface ParserContext {
    void addImports(String... imports);

    /**
     * Gets the arguments class name.
     *
     * @return arguments class name
     */
    String getArgumentsClassName();

    /**
     * Gets the class name.
     *
     * @return class name
     */
    String getClassName();

    FilePosition getCurrentFilePosition();

    /**
     * End comment tag. Should be "--" + {@link #getEndTag()}.
     *
     * @return end comment tag
     */
    String getEndCommentTag();

    /**
     * Get end tag.
     *
     * @return end tag
     */
    String getEndTag();

    /**
     * Returns the imports.
     *
     * @return
     */
    Collection<String> getImports();

    /**
     * Gets the current indent level.
     *
     * @return
     */
    int getIndentLevel();

    String getPackage();

    /**
     * Get start tag.
     *
     * @return start tag
     */
    String getStartTag();

    void inc();

    /**
     * Increases indent level.
     */
    void indent();

    /**
     * Dealing with the topmost file.
     *
     * @return
     */
    boolean isTopFile();

    void nl();

    void popFile();

    void pushFile(File file) throws IOException;

    void pushFile(String filename) throws IOException;

    /**
     * Sets the arguments class name.
     *
     * @param argumentsClassName
     *            class name
     */
    void setArgumentsClassName(String argumentsClassName);

    void setClassName(String className);

    /**
     * Sets the end tag and related values.
     *
     * @param endTag
     *            end tag
     */
    void setEndTag(String endTag);

    /**
     * Sets the package name.
     *
     * @param packageName
     *            package name
     */
    void setPackage(String packageName);

    /**
     * Sets the start tag and related values.
     *
     * @param startTag
     *            start tag
     */
    void setStartTag(String startTag);

    /**
     * Decreases indent level. Throws an exception if the indentation level is
     * already at zero.
     *
     * @throws IOException
     */
    void unindent() throws IOException;
}
