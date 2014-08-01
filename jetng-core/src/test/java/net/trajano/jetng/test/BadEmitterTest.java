package net.trajano.jetng.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.PrintWriter;

import net.trajano.jetng.ContextNotReadyException;
import net.trajano.jetng.JavaEmitterParseEventHandler;
import net.trajano.jetng.JetNgParser;
import net.trajano.jetng.ParseException;
import net.trajano.jetng.internal.FilePosition;

import org.junit.Test;

/**
 * Tests the parser.
 */
public class BadEmitterTest {
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
     * Tests bad JET attribute
     */
    @Test
    public void testBadJetAttribute() throws Exception {
        try {
            outputTestFile("BadJetAttribute");
            fail();
        } catch (final ParseException e) {
            final FilePosition currentFilePosition = e.getContext()
                    .getCurrentFilePosition();
            assertEquals("BadJetAttribute.jet", currentFilePosition.getFile()
                    .getName());
            assertEquals(6, currentFilePosition.getRow());
            assertEquals(75, currentFilePosition.getCol());
        }
    }

    /**
     * Tests the module method.
     */
    @Test(expected = ContextNotReadyException.class)
    public void testDirectiveOnly() throws Exception {
        outputTestFile("DirectiveOnly");
    }

    /**
     * Tests the double directive.
     */
    @Test
    public void testDoubleDirective() throws Exception {
        try {
            outputTestFile("BadDoubleDirective");
            fail();
        } catch (final ParseException e) {
            final FilePosition currentFilePosition = e.getContext()
                    .getCurrentFilePosition();
            assertEquals("BadDoubleDirective.jet", currentFilePosition
                    .getFile().getName());
            assertEquals(2, currentFilePosition.getRow());
            assertEquals(93, currentFilePosition.getCol());
        }
    }
}