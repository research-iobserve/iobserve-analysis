package org.palladiosimulator.protocom.lang.xml;

import java.util.Collection;
import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface IJeeDescriptor extends ICompilationUnit {
  public abstract String ejbName();
  
  public abstract Collection<String> ejbRefName();
  
  public abstract String displayName();
}
