package org.palladiosimulator.protocom.lang.xml;

import java.util.Collection;
import java.util.HashMap;
import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface IJeeFacetCoreXml extends ICompilationUnit {
  public abstract String runtimeName();
  
  public abstract Collection<String> fixedFacet();
  
  public abstract HashMap<String, String> installedFacet();
}
