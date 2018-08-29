package org.palladiosimulator.protocom.lang.properties;

import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface IBuildProperties extends ICompilationUnit {
  public abstract String output();
  
  public abstract String source();
  
  public abstract String binIncludes();
}
