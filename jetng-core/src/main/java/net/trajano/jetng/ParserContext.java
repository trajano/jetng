package net.trajano.jetng;

import java.io.IOException;
import java.util.Collection;

/**
 * Context for the JET parser.
 *
 * @author Archimedes Trajano
 */
public interface ParserContext {
    void addImports(String... imports);

    /**
     * Gets the class name.
     *
     * @return class name
     */
    String getClassName();

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

    /**
     * Gets the object class name.
     *
     * @return object class name
     */
    String getObjectClassName();

    String getPackage();

    /**
     * Get start tag.
     *
     * @return start tag
     */
    String getStartTag();

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

    void setClassName(String className);

    /**
     * Sets the end tag and related values.
     *
     * @param endTag
     *            end tag
     */
    void setEndTag(String endTag);

    void setObjectClassName(String objectClassName);

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
