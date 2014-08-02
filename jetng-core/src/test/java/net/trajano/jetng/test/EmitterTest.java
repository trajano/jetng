package net.trajano.jetng.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.trajano.jetng.JavaEmitterParseEventHandler;
import net.trajano.jetng.JetNgParser;
import net.trajano.jetng.ParserContext;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests the parser.
 */
public class EmitterTest {
    private void doTestFile(final String file, final String targetFile)
            throws Exception {
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
        final ParserContext context = parser.parse();
        out.close();
        // assertEquals(verify.length(), target.toString().length());
        assertEquals(verify,
                target.toString().replace("\r\n", "\n").replace("\r", "\n"));
        assertEquals(targetFile, context.getTargetFile());

    }

    /**
     * Tests the blog post example.
     */
    @Test
    public void testArgumentsBlogPost() throws Exception {
        doTestFile("ArgumentsBlogPost", "foo/Bar.java");
    }

    /**
     * Tests the blog post example.
     */
    @Test
    public void testBlogPost() throws Exception {
        doTestFile("BlogPost", "foo/Bar.java");
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testFull() throws Exception {
        doTestFile("TableModuleGenerator",
                "net/trajano/apt/jpa/internal/TableModuleGenerator.java");
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testFullNg() throws Exception {
        doTestFile("TableModuleGeneratorNg",
                "net/trajano/apt/jpa/internal/TableModuleGenerator.java");
    }

    /**
     * Tests the include directive.
     */
    @Test
    public void testInclude() throws Exception {
        doTestFile("Include",
                "net/trajano/apt/jpa/internal/TableModuleGenerator.java");
    }

    /**
     * Tests the indentation example.
     */
    @Test
    public void testIndentBlogPost() throws Exception {
        doTestFile("IndentBlogPost", "foo/Bar.java");
    }

    /**
     * Tests the module method.
     */
    @Test
    public void testSpaceCheck() throws Exception {
        doTestFile("SpaceCheck",
                "net/trajano/apt/jpa/internal/TableModuleGenerator.java");
    }
}