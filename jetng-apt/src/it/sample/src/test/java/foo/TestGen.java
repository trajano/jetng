package foo;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestGen {
    @Test
    public void testGenerate() {
        List<String> l = Arrays.asList("hello", "world");
        assertEquals("Hello world" + System.getProperty("line.separator") + 2
                + System.getProperty("line.separator"), new Bar().generate(l));
    }

    @Test
    public void testMetaInf() throws Exception {
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        blah.SampleProcessor.class
                                .getResourceAsStream("/META-INF/services/javax.annotation.processing.Processor")));
//        assertEquals("blah.SampleProcessor", reader.readLine());
        reader.close();
    }
}