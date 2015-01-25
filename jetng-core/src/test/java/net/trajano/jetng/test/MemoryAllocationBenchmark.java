package net.trajano.jetng.test;

import java.io.IOException;

import net.trajano.jetng.ParserContext;
import net.trajano.jetng.internal.DefaultParserContext;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

/**
 * This is a microbenchmark to see if allocating and deallocating a class
 * instance to invoke a method is faster vs. reusing an existing class is faster
 * or not.
 *
 * @author Archimedes
 *
 */
public class MemoryAllocationBenchmark {

    public static void main(final String[] args) {
        CaliperMain.main(MemoryAllocationBenchmark.class, args);
    }

    @Benchmark
    public void timeLongLived(final int reps) throws IOException {
        final ParserContext context = new DefaultParserContext();
        for (int i = 0; i < reps; i++) {
            context.setStartTag("AAAA");
        }
    }

    @Benchmark
    public void timeShortLivedAllocation(final int reps) throws IOException {
        for (int i = 0; i < reps; i++) {
            final ParserContext context = new DefaultParserContext();
            context.setStartTag("AAAA");
        }
    }
}
