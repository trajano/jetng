package net.trajano.mojo.jetng;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.trajano.jetng.JavaEmitterParseEventHandler;
import net.trajano.jetng.JetNgParser;
import net.trajano.jetng.ParseException;
import net.trajano.jetng.ParserContext;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Compile.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresOnline = false)
public class CompileMojo extends AbstractMojo {
	/**
	 * Resource bundle.
	 */
	private static final ResourceBundle R = ResourceBundle
			.getBundle("META-INF/Messages");
	/**
	 * Build context.
	 */
	@Component
	private BuildContext buildContext;

	/**
	 * The directory to write the processed JET files.
	 */
	@Parameter(defaultValue = "${project.build.directory}/generated-sources/jetng", required = true)
	private File destDir;

	/**
	 * A list of jet file sets to import. The default is:
	 *
	 * <pre>
	 * &lt;jetFileSets>
	 *     &lt;fileSet>
	 *         &lt;directory>${basedir}/src/main/jetng&lt;/directory>
	 *         &lt;includes>
	 *             &lt;include>**\/\*.jet&lt;/include>
	 *         &lt;/includes>
	 *         &lt;excludes>
	 *         &lt;/excludes>
	 *     &lt;/fileSet>
	 * &lt;/jetFileSets>
	 * </pre>
	 */
	@Parameter(required = false)
	private List<FileSet> jetFileSets;

	/**
	 * Maximum number of characters for tags.
	 */
	@Parameter(defaultValue = "10", required = true)
	private int maxTagSize;

	/**
	 * The Maven Project.
	 */
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	/**
	 * Performs the compile.
	 *
	 * @throws MojoExecutionException
	 *             thrown when there is a problem executing Mjo.
	 */
	@Override
	public void execute() throws MojoExecutionException {
		destDir.mkdirs();
		if (jetFileSets == null) {
			final FileSet defaultJetFileSet = new FileSet();
			defaultJetFileSet.setDirectory(new File(project.getBasedir(),
					"src/main/jetng").getPath());
			defaultJetFileSet.addInclude("**/*.jet");
			jetFileSets = Collections.singletonList(defaultJetFileSet);
		}
		final File tmpFile;
		try {
			tmpFile = File.createTempFile("jetng-maven-plugin", ".tmp"); // NOPMD
		} catch (final IOException e) {
			throw new MojoExecutionException(
					R.getString("failedtocreatetempfile"), e);

		}
		for (final FileSet fileSet : jetFileSets) {
			final String directory = fileSet.getDirectory();
			final File baseDirectory = new File(directory); // NOPMD
			if (!baseDirectory.isDirectory()) { // NOPMD
				getLog().warn(format(R.getString("missingdir"), directory));
				continue;
			}
			final Scanner scanner = buildContext.newScanner(baseDirectory);
			scanner.setIncludes(fileSet.getIncludes().toArray(new String[0])); // NOPMD
			scanner.setExcludes(fileSet.getExcludes().toArray(new String[0])); // NOPMD
			scanner.scan();
			project.addCompileSourceRoot(destDir.toString());
			for (final String includedFile : scanner.getIncludedFiles()) {
				final File inputFile = new File(baseDirectory, // NOPMD
						includedFile);
				try {
					final PrintWriter out = new PrintWriter(// NOPMD
							buildContext.newFileOutputStream(tmpFile));
					final JetNgParser parser = new JetNgParser(inputFile,
							new JavaEmitterParseEventHandler(out), maxTagSize);
					final ParserContext parseContext = parser.parse();
					out.close();
					final File targetFile = new File(destDir,
							parseContext.getTargetFile());
					targetFile.getParentFile().mkdirs();
					final OutputStream output = buildContext
							.newFileOutputStream(targetFile);
					IOUtil.copy(new FileInputStream(tmpFile), output);
					output.close();
					buildContext.refresh(targetFile);
				} catch (final ParseException e) {
					throw new MojoExecutionException(String.format(R
							.getString("parseerror"), inputFile, e.getContext()
							.getCurrentFilePosition()), e);
				} catch (final Exception e) {
					throw new MojoExecutionException(String.format(
							R.getString("failedtocompile"), inputFile), e);
				}
			}
		}
		tmpFile.delete();
	}

}
