package org.palladiosimulator.protocom.lang.xml;

import java.util.Set;
import org.palladiosimulator.protocom.lang.xml.IClasspath;

@SuppressWarnings("all")
public interface IJeeClasspath extends IClasspath {
  public abstract String clientClassPathEntry();
  
  public abstract Set<String> requiredClientProjects();
}
