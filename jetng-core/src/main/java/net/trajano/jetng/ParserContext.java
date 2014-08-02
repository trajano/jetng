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
    /**
     * Add imports that are specified in the <code>jet</code> directive.
     *
     * @param imports
     *            imports to add
     */
    void addImports(String... imports);

    /**
     * Gets the arguments class name that is configured in the <code>jet</code>
     * directive. This is by default "Object".
     *
     * @return arguments class name
     */
    String getArgumentsClassName();

    /**
     * Gets the class name that is configured in the <code>jet</code> directive.
     *
     * @return class name
     */
    String getClassName();

    /**
     * Gets the current file position.
     *
     * @return current file position
     */
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
     * @return imports
     */
    Collection<String> getImports();

    /**
     * Gets the current indent level.
     *
     * @return indent level
     */
    int getIndentLevel();

    /**
     * Gets the package name that is configured in the <code>jet</code>
     * directive.
     *
     * @return package name
     */
    String getPackageName();

    /**
     * Get start tag.
     *
     * @return start tag
     */
    String getStartTag();

    /**
     * Provides the target file name. This should be the package name with the
     * dots as slashes and the class name followed by .java.
     *
     * @return target file name.
     */
    String getTargetFile();

    /**
     * Increments the column count on the current file.
     */
    void inc();

    /**
     * Increases indent level.
     */
    void indent();

    /**
     * Dealing with the topmost file.
     *
     * @return true if dealing with the topmost file.
     */
    boolean isTopFile();

    /**
     * Incrementes the row count of the current file and resets the column count
     * back to 1.
     */
    void nl();

    /**
     * Pops file from the file stack.
     */
    void popFile();

    /**
     * Pushes a file to the file stack.
     *
     * @param file
     *            file to push into the stack
     * @throws IOException
     */
    void pushFile(File file) throws IOException;

    /**
     * Pushes a file to the file stack.
     *
     * @param filename
     *            relative to the current file.
     * @throws IOException
     */
    void pushFile(String filename) throws IOException;

    /**
     * Sets the arguments class name.
     *
     * @param argumentsClassName
     *            class name
     */
    void setArgumentsClassName(String argumentsClassName);

    /**
     * Sets the class name.
     *
     * @param className
     *            class name
     */
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
    void setPackageName(String packageName);

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
