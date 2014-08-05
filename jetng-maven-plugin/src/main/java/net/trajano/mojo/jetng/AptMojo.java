package net.trajano.mojo.jetng;

import static java.lang.String.format;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.trajano.schema.emitter.Emitter;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * This processes ".apt.xml" files to generate annotation processors that uses
 * JETNG.
 */
@Mojo(name = "apt", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresOnline = false)
public class AptMojo extends AbstractMojo {
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
	 * The directory to write the annotation processor code.
	 */
	@Parameter(defaultValue = "${project.build.directory}/generated-sources/jetng-apt", required = true)
	private File destDir;

	/**
	 * A list of jet file sets to import. The default is:
	 *
	 * <pre>
	 * &lt;jetFileSets>
	 *     &lt;fileSet>
	 *         &lt;directory>${basedir}/src/main/jetng&lt;/directory>
	 *         &lt;includes>
	 *             &lt;include>**\/\*.apt.xml&lt;/include>
	 *         &lt;/includes>
	 *         &lt;excludes>
	 *         &lt;/excludes>
	 *     &lt;/fileSet>
	 * &lt;/jetFileSets>
	 * </pre>
	 */
	@Parameter(required = false)
	private List<FileSet> aptXmlFileSets;

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
	 *             thrown when there is a problem executing mojo.
	 */
	@Override
	public void execute() throws MojoExecutionException {
		final Unmarshaller um;
		try {
			final JAXBContext jc = JAXBContext.newInstance(Emitter.class);
			um = jc.createUnmarshaller();
		} catch (final JAXBException e) {
			throw new MojoExecutionException(R.getString("jaxbissue"));
		}
		destDir.mkdirs();
		if (aptXmlFileSets == null) {
			final FileSet defaultJetFileSet = new FileSet();
			defaultJetFileSet.setDirectory(new File(project.getBasedir(),
					"src/main/jetng").getPath());
			defaultJetFileSet.addInclude("**/*.apt.xml");
			aptXmlFileSets = Collections.singletonList(defaultJetFileSet);
		}
		for (final FileSet fileSet : aptXmlFileSets) {
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
					final Emitter emitter = (Emitter) um.unmarshal(inputFile);
				} catch (final Exception e) {
					throw new MojoExecutionException(String.format(
							R.getString("failedtocompile"), inputFile), e);
				}
			}
		}
	}
}
