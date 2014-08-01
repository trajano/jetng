package net.trajano.jetng.internal;

import java.io.File;
import java.io.IOException;

/**
 * Null object for File.
 *
 * @author Archimedes
 */
@SuppressWarnings("serial")
public final class NullFile extends File {

    /**
     * Instance.
     */
    private static final NullFile INSTANCE = new NullFile();

    /**
     * Gets instance of the null file.
     *
     * @return intance
     */
    public static NullFile get() {
        return INSTANCE;
    }

    /**
     * Prevent construction of the object. Use {@link #get()} to get the
     * instance.
     */
    private NullFile() {
        super("");
    }

    @Override
    public boolean createNewFile() throws IOException {
        throw new IllegalStateException();
    }

    @Override
    public boolean delete() {
        throw new IllegalStateException();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean mkdir() {
        throw new IllegalStateException();
    }

    @Override
    public boolean mkdirs() {
        throw new IllegalStateException();
    }
}
