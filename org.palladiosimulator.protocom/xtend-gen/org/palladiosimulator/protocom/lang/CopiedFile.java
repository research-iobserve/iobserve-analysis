package org.palladiosimulator.protocom.lang;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.palladiosimulator.commons.eclipseutils.FileHelper;
import org.palladiosimulator.protocom.ProtoComProject;
import org.palladiosimulator.protocom.ProtoComProjectFactory;

/**
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class CopiedFile {
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
  private String destinationPath;
  
  private File inputFile;
  
  private URL inputUrl;
  
  public CopiedFile build(final String destinationPath, final File inputFile) {
    CopiedFile _xblockexpression = null;
    {
      boolean _equals = Objects.equal(inputFile, null);
      if (_equals) {
        throw new RuntimeException(
          (("No file for path " + destinationPath) + "!"));
      }
      this.destinationPath = destinationPath;
      this.inputFile = inputFile;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  public CopiedFile build(final String destinationPath, final URL inputUrl) {
    CopiedFile _xblockexpression = null;
    {
      boolean _equals = Objects.equal(inputUrl, null);
      if (_equals) {
        throw new RuntimeException(
          (("No URL for path " + destinationPath) + "!"));
      }
      this.destinationPath = destinationPath;
      this.inputUrl = inputUrl;
      _xblockexpression = this;
    }
    return _xblockexpression;
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
  public void store() {
    try {
      ProtoComProject _project = ProtoComProjectFactory.getProject(this.projectURI, this.destinationPath);
      IProject project = _project.getIProject();
      IFile file = project.getFile(this.destinationPath);
      IContainer _parent = file.getParent();
      this.createFolders(((IFolder) _parent));
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(this.inputFile, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _isDirectory = this.inputFile.isDirectory();
        _and = _isDirectory;
      }
      if (_and) {
        File _file = FileHelper.getFile("platform:/plugin/org.palladiosimulator.protocom/buildfiles/build.xml");
        File _absoluteFile = _file.getAbsoluteFile();
        String buildScript = _absoluteFile.toString();
        String _name = this.inputFile.getName();
        String _plus = ("-Dmessage=Building -verbose -DpluginName=" + _name);
        String _plus_1 = (_plus + " -DtargetFile=");
        IPath _location = file.getLocation();
        String _string = _location.toString();
        String _plus_2 = (_plus_1 + _string);
        String _plus_3 = (_plus_2 + " -DsourceFile=");
        String _plus_4 = (_plus_3 + this.inputFile);
        String antArguments = (_plus_4 + "/bin");
        AntRunner runner = new AntRunner();
        runner.setBuildFileLocation(buildScript);
        runner.setArguments(antArguments);
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        runner.run(_nullProgressMonitor);
      } else {
        boolean _notEquals_1 = (!Objects.equal(this.inputUrl, null));
        if (_notEquals_1) {
          InputStream _openStream = this.inputUrl.openStream();
          NullProgressMonitor _nullProgressMonitor_1 = new NullProgressMonitor();
          file.create(_openStream, false, _nullProgressMonitor_1);
        } else {
          boolean _notEquals_2 = (!Objects.equal(this.inputFile, null));
          if (_notEquals_2) {
            URI _uRI = this.inputFile.toURI();
            URL _uRL = _uRI.toURL();
            InputStream _openStream_1 = _uRL.openStream();
            NullProgressMonitor _nullProgressMonitor_2 = new NullProgressMonitor();
            file.create(_openStream_1, false, _nullProgressMonitor_2);
          } else {
            throw new RuntimeException("Unexpected error when exporting to JAR");
          }
        }
      }
      file.refreshLocal(IResource.DEPTH_ONE, null);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void createFolders(final IFolder folder) {
    try {
      boolean _exists = folder.exists();
      boolean _not = (!_exists);
      if (_not) {
        IContainer _parent = folder.getParent();
        if ((_parent instanceof IFolder)) {
          IContainer _parent_1 = folder.getParent();
          this.createFolders(((IFolder) _parent_1));
        }
        folder.create(false, false, null);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
