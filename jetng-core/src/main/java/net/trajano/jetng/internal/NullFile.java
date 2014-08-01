package net.trajano.jetng.internal;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class NullFile extends File {

    private static NullFile INSTANCE = new NullFile();

    public static NullFile get() {
        return INSTANCE;
    }

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
