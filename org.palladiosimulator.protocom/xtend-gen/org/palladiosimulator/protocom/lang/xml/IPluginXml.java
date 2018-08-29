package org.palladiosimulator.protocom.lang.xml;

import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface IPluginXml extends ICompilationUnit {
  public abstract String extensionPoint();
  
  public abstract String actionDelegateClass();
  
  public abstract String actionDelegateId();
}
