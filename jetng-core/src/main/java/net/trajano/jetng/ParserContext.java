package net.trajano.jetng;

/**
 * Configuration for the JET parser.
 *
 * @author Archimedes Trajano
 */
public class ParserContext {
    /**
     * End comment tag. This is "--" followed by the end tag.
     */
    private String endCommentTag;

    /**
     * End tag.
     */
    private String endTag;

    /**
     * Start tag.
     */
    private String startTag;

    /**
     * Creates the configuration with the default start and end tags.
     */
    public ParserContext() {
        setStartTag("<%");
        setEndTag("%>");
    }

    public String getEndCommentTag() {
        return endCommentTag;
    }

    public String getEndTag() {
        return endTag;
    }

    public String getStartTag() {
        return startTag;
    }

    /**
     * Sets the end tag and related values.
     *
     * @param endTag
     *            end tag
     */
    public void setEndTag(final String endTag) {
        this.endTag = endTag;
        endCommentTag = "--" + endTag;
    }

    /**
     * Sets the start tag and related values.
     *
     * @param startTag
     *            start tag
     */
    public void setStartTag(final String startTag) {
        this.startTag = startTag;
    }
}
