package com.github.mpkorstanje.unicode.tr39confusables;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * 
 * @author mpkorstanje
 *
 */

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE, requiresOnline = true, threadSafe = true, requiresProject = true)
public class TR39ConfusablesMojo extends AbstractMojo {

	/**
	 * Specify grammar file encoding; e.g., euc-jp
	 */
	@Parameter(property = "project.build.sourceEncoding")
	protected String encoding;

	/**
	 * The current Maven project.
	 */
	@Parameter(property = "project", required = true, readonly = true)
	protected MavenProject project;

	/**
	 * The directory where the String Template Group files ({@code *.stg}) are
	 * located.
	 */
	@Parameter(defaultValue = "${basedir}/src/main/tr39confusables")
	private File sourceDirectory;
	
	/**
	 * Url for the confusables table.
	 */
	@Parameter(defaultValue="http://www.unicode.org/Public/security/latest/confusables.txt")
	private URL table;
	
	/**
	 * Specify output directory where the Java files are generated.
	 */
	@Parameter(defaultValue = "${project.build.directory}/generated-sources/tr39confusables")
	private File outputDirectory;




	public void execute() throws MojoExecutionException {
		Log log = getLog();

		if (log.isDebugEnabled()) {
			log.debug("tr39confusables: Table: " + table);
			log.debug("tr39confusables: Source: " + sourceDirectory);
			log.debug("tr39confusables: Output: " + outputDirectory);
		}

		if (!sourceDirectory.isDirectory()) {
			log.info("No templates to compile in "
					+ sourceDirectory.getAbsolutePath());
			return;
		}
		
		// Ensure that the output directory path is all in tact so that
		// we can just write into it.
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}
		
		GenerateTables generator = new GenerateTables(table,sourceDirectory,outputDirectory);
		try {
			generator.generate();
		} catch (IOException e) {
			log.error(e);
			throw new MojoExecutionException("Encountered a problem while generating confusables table", e);
		}
		
		if (project != null) {
			// Tell Maven that there are some new source files underneath the
			// output directory.
			project.addCompileSourceRoot(outputDirectory.getPath());
		}
	}
}