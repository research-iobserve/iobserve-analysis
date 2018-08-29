package org.palladiosimulator.protocom.lang.xml;

import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface IJeeComponentFile extends ICompilationUnit {
  public abstract String wbModuleDeployName();
  
  public abstract String wbResource();
  
  public abstract String property();
}
