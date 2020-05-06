package org.palladiosimulator.protocom.lang

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.resources.IProject
import org.palladiosimulator.protocom.ProtoComProjectFactory
import com.google.inject.name.Named
import com.google.inject.Inject
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import java.net.URL
import java.io.File
import org.eclipse.core.resources.IResource
import org.eclipse.ant.core.AntRunner
import org.palladiosimulator.commons.eclipseutils.FileHelper

/**
 * @author Sebastian Lehrig
 */
class CopiedFile {	
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	String destinationPath
	File inputFile
	URL inputUrl
	
	def build(String destinationPath, File inputFile) {
		if (inputFile === null) {
			throw new RuntimeException(
				"No file for path " + destinationPath +"!"
			)
		}

		this.destinationPath = destinationPath
		this.inputFile = inputFile
		
		this
	}
	
	def build(String destinationPath, URL inputUrl) {
		if (inputUrl === null) {
			throw new RuntimeException(
				"No URL for path " + destinationPath +"!"
			)
		}

		this.destinationPath = destinationPath
		this.inputUrl = inputUrl
		
		this
	}
	
	/**
	 * In case we are a developer and plug-ins are checked-out in a developer workspace, we have no access to pre-packed
	 * JAR files of such plug-ins. Instead, only the plug-in folder (with sources, class files, etc.) is available. In
	 * this case, this method packages this folder into a JAR file to be delivered along with a prototype. We assessed
	 * the following 3 options for that; we finally chose option 3 for the given reasons.
	 * 
	 * 1) Package JAR with Eclipse's default JAR exporter
	 * This approach only works when for workspace resources. Therefore, we cannot use it for plug-in folder (that are
	 * stored within the Eclipse platform).
	 * @see http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Fguide%2Fjdt_api_write_jar_file.htm
	 * 
	 * 2) Package JAR with Java API
	 * This approach is generally possible, however, requires some coding. We tried and failed as we did not manage to
	 * get a package structure recognized by Eclipse within generated JARs. This was despite the fact that we correctly
	 * created a corresponding folder structure and copied the class files there.
	 * @see http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file 
	 * 
	 * 3) Run an ANT script to package a JAR
	 * The needed code is quite short (both Java and ANT script) and easy to understand. Plus it actually works :) 
	 * @see http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2Fant_running_buildfiles_programmatically.htm
	 */
	def store() {
		// prepare target
		var IProject project = ProtoComProjectFactory.getProject(projectURI, destinationPath).IProject;
		var IFile file = project.getFile(destinationPath);
		createFolders(file.parent as IFolder);
		
		// store
		if (inputFile !== null && inputFile.isDirectory) {
			// see http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2Fant_running_buildfiles_programmatically.htm
			var buildScript = FileHelper.getFile("platform:/plugin/org.palladiosimulator.protocom/buildfiles/build.xml").absoluteFile.toString;
			var antArguments = "-Dmessage=Building -verbose -DpluginName="+inputFile.name+" -DtargetFile="+file.location.toString+ " -DsourceFile="+inputFile+"/bin";
			
			var AntRunner runner = new AntRunner();
			runner.setBuildFileLocation(buildScript);
			runner.setArguments(antArguments);
			runner.run(new NullProgressMonitor());
		} else {
			if (inputUrl !== null) {
				file.create(inputUrl.openStream, false, new NullProgressMonitor);
			} else if (inputFile !== null) {
				file.create(inputFile.toURI.toURL.openStream, false, new NullProgressMonitor);
			} else {
				throw new RuntimeException("Unexpected error when exporting to JAR");
			}
		}
		
		// @see http://stackoverflow.com/questions/20208039/in-eclipse-plug-in-programmatically-save-new-java-files-to-an-existing-jar-fil
		file.refreshLocal(IResource.DEPTH_ONE, null);
	}
	
	private def void createFolders(IFolder folder) {
		if (!folder.exists) {
			if (folder.parent instanceof IFolder) createFolders(folder.parent as IFolder)
			folder.create(false, false, null)
		}
	}
}
