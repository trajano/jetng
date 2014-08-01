package net.trajano.jetng.test;

import java.util.Arrays;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

public class IndentStringBenchmark {
    private static String indentArrayMethod(final int level) {
        final char[] carray = new char[4 * level];
        Arrays.fill(carray, ' ');
        return new String(carray);
    }

    private static String indentStringBuilderMethod(final int level) {
        final StringBuilder b = new StringBuilder(level * 4);
        for (int i = level - 1; i >= 0; --i) {
            b.append("    ");
        }
        return b.toString();
    }

    private static String indentStringMethod(final int level) {
        String s = "";
        for (int i = level - 1; i >= 0; --i) {
            s += "    ";
        }
        return s;
    }

    public static void main(final String[] args) {
        CaliperMain.main(IndentStringBenchmark.class, args);
    }

    @Benchmark
    public void timeArrayMethod(final int reps) {
        for (int i = 0; i < reps; i++) {
            indentArrayMethod(20);
        }
    }

    @Benchmark
    public void timeStringBufferMethod(final int reps) {
        for (int i = 0; i < reps; i++) {
            indentStringBuilderMethod(20);
        }
    }

    @Benchmark
    public void timeStringMethod(final int reps) {
        for (int i = 0; i < reps; i++) {
            indentStringMethod(20);
        }
    }
}
