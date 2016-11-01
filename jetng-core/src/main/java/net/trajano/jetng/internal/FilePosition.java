package net.trajano.jetng.internal;

import java.io.File;
import java.io.Serializable;

/**
 * Represents the current file position.
 */
public class FilePosition implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8110109550315429528L;

	/**
     * Column.
     */
    private int col;

    /**
     * End tag.
     */
    private String endTag;

    /**
     * File.
     */
    private final File file;

    /**
     * Row.
     */
    private int row;
    /**
     * Start tag.
     */
    private String startTag;

    /**
     * Constructs the file position with the col and row at zero.
     *
     * @param file
     *            file.
     */
    public FilePosition(final File file) {
        this.file = file;
        col = 1;
        row = 1;
    }

    public int getCol() {
        return col;
    }

    public String getEndTag() {
        return endTag;
    }

    public File getFile() {
        return file;
    }

    public int getRow() {
        return row;
    }

    public String getStartTag() {
        return startTag;
    }

    /**
     * Increment column count.
     */
    public void inc() {
        ++col;
    }

    /**
     * Increment row count and reset column count.
     */
    public void nl() {
        ++row;
        col = 1;
    }

    public void setCol(final int col) {
        this.col = col;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    /**
     * Set tags.
     *
     * @param start
     *            start tag
     * @param end
     *            end tag
     */
    public void setTags(final String start, final String end) {
        startTag = start;
        endTag = end;
    }

    @Override
    public String toString() {
        return String.format("%s[%d,%d]", file, row, col);
    }
}
