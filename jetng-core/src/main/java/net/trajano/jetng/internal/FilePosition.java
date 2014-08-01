package net.trajano.jetng.internal;

public class FilePosition {
    /**
     * Column.
     */
    private int col;

    /**
     * File name.
     */
    private final String fileName;

    /**
     * Row.
     */
    private int row;

    /**
     * Constructs the file position with the col and row at zero.
     *
     * @param fileName
     *            file name.
     */
    public FilePosition(final String fileName) {
        this.fileName = fileName;
        col = 1;
        row = 1;
    }

    public int getCol() {
        return col;
    }

    public String getFileName() {
        return fileName;
    }

    public int getRow() {
        return row;
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

    @Override
    public String toString() {
        return String.format("%s[%d,%d]", fileName, row, col);
    }
}
