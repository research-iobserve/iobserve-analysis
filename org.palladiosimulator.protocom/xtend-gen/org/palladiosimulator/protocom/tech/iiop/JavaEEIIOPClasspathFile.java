package org.palladiosimulator.protocom.tech.iiop;

import java.io.File;
import java.util.Set;
import org.palladiosimulator.commons.eclipseutils.FileHelper;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.xml.IJeeClasspath;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPClasspathFile<E extends Entity> extends ConceptMapping<E> implements IJeeClasspath {
  public JavaEEIIOPClasspathFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String classPathEntries() {
    return null;
  }
  
  @Override
  public String filePath() {
    return null;
  }
  
  @Override
  public String projectName() {
    return null;
  }
  
  @Override
  public String clientClassPathEntry() {
    return null;
  }
  
  @Override
  public Set<String> requiredClientProjects() {
    return null;
  }
  
  protected String pluginJar(final String source) {
    File _pluginJarFile = FileHelper.getPluginJarFile(source);
    return _pluginJarFile.getName();
  }
}
