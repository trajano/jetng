package net.trajano.jetng.internal;

public class FilePosition {
    private int col = 0;

    private int row = 0;

    public int getCol() {
        return col;
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
        col = 0;
    }

    public void setCol(final int col) {
        this.col = col;
    }

    public void setRow(final int row) {
        this.row = row;
    }
}
