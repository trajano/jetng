package net.trajano.jetng.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.trajano.jetng.ContextNotReadyException;
import net.trajano.jetng.JavaEmitterParseEventHandler;
import net.trajano.jetng.JetNgParser;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests the parser.
 */
public class EmitterTest {
    private void doTestFile(final String file) throws Exception {
        final StringWriter w = new StringWriter();
        IOUtils.copy(new InputStreamReader(Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(file + ".java")),
                w);

        final String verify = w.toString().replace("\r\n", "\n")
                .replace("\r", "\n");
        final StringWriter target = new StringWriter(verify.length());
        final PrintWriter out = new PrintWriter(target);
        final JetNgParser parser = new JetNgParser(new File(Thread
                .currentThread().getContextClassLoader()
                .getResource(file + ".jet").toURI()),
                new JavaEmitterParseEventHandler(out), 6);
        parser.parse();
        out.close();
        // assertEquals(verify.length(), target.toString().length());
        assertEquals(verify,
                target.toString().replace("\r\n", "\n").replace("\r", "\n"));

    }

    private void outputTestFile(final String file) throws Exception {
        final PrintWriter out = new PrintWriter(System.out);
        final JetNgParser parser = new JetNgParser(new File(Thread
                .currentThread().getContextClassLoader()
                .getResource(file + ".jet").toURI()),
                new JavaEmitterParseEventHandler(out), 6);
        parser.parse();
        out.flush();
    }

    /**
     * Tests the blog post example.
     */
    @Test
    public void testBlogPost() throws Exception {
        doTestFile("BlogPost");
    }

    /**
     * Tests the module method.
     */
    @Test(expected = ContextNotReadyException.class)
    public void testDirectiveOnly() throws Exception {
        outputTestFile("DirectiveOnly");
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testFull() throws Exception {
        doTestFile("TableModuleGenerator");
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testFullNg() throws Exception {
        doTestFile("TableModuleGeneratorNg");
    }
}