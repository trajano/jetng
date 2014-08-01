package net.trajano.jetng.test;

import static org.junit.Assert.assertEquals;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import net.trajano.jetng.JetNgParser;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests the parser.
 */
public class ParserTest {
    private void doTestFile(final String file) throws Exception {
        final StringWriter w = new StringWriter();
        IOUtils.copy(new InputStreamReader(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(file + ".txt")), w);

        final String verify = w.toString().replace("\r\n", "\n")
                .replace("\r", "\n");
        final StringWriter target = new StringWriter(verify.length());
        final Reader r = new InputStreamReader(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(file + ".jet"));
        final PrintWriter out = new PrintWriter(target);
        final JetNgParser parser = new JetNgParser(r, new PrintWriterHandler(
                out));
        parser.parse();
        out.close();
        // assertEquals(verify.length(), target.toString().length());
        assertEquals(verify,
                target.toString().replace("\r\n", "\n").replace("\r", "\n"));

    }

    /**
     * Tests the module method.
     */
    @Test
    public void testDirectiveOnly() throws Exception {
        doTestFile("DirectiveOnly");
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testFull() throws Exception {
        doTestFile("TableModuleGenerator");
    }
}