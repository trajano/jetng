package net.trajano.mojo.jetng;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Compile.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresOnline = false)
public class CompileMojo extends AbstractMojo {
	/**
	 * Performs the compile.
	 *
	 * @throws MojoExecutionException
	 *             thrown when there is a problem executing Mjo.
	 */
	@Override
	public void execute() throws MojoExecutionException {
	}
}
