package org.palladiosimulator.protocom;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.xtext.generator.AbstractFileSystemAccess2;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.palladiosimulator.protocom.PCMEclipseResourceFileSystemAccess2;

/**
 * Google Guice provider for FileSystemAccess. It configures and
 * injects into FSA objects.
 * 
 * TODO: Class is unfinished. Read configuration from wizard pages.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public class FSAProvider implements Provider<AbstractFileSystemAccess2> {
  @Inject
  private Injector injector;
  
  @Override
  public AbstractFileSystemAccess2 get() {
    PCMEclipseResourceFileSystemAccess2 _xblockexpression = null;
    {
      final PCMEclipseResourceFileSystemAccess2 fsa = this.injector.<PCMEclipseResourceFileSystemAccess2>getInstance(PCMEclipseResourceFileSystemAccess2.class);
      Map<String, OutputConfiguration> _defaultConfig = this.defaultConfig();
      fsa.setOutputConfigurations(_defaultConfig);
      NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
      fsa.setMonitor(_nullProgressMonitor);
      _xblockexpression = fsa;
    }
    return _xblockexpression;
  }
  
  public Map<String, OutputConfiguration> defaultConfig() {
    Map<String, OutputConfiguration> _xblockexpression = null;
    {
      final OutputConfiguration defaultOutput = new OutputConfiguration("PCM");
      defaultOutput.setDescription("Output Folder");
      defaultOutput.setOutputDirectory(".");
      defaultOutput.setOverrideExistingResources(true);
      defaultOutput.setCreateOutputDirectory(true);
      defaultOutput.setCleanUpDerivedResources(true);
      defaultOutput.setSetDerivedProperty(true);
      final Map<String, OutputConfiguration> map = new HashMap<String, OutputConfiguration>();
      map.put("PCM", defaultOutput);
      _xblockexpression = map;
    }
    return _xblockexpression;
  }
}
