package org.palladiosimulator.protocom.lang.prefs;

import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface IJeePreferences extends ICompilationUnit {
  public abstract String eclipsePreferencesVersion();
  
  public abstract String codegenInlineJsrBytecode();
  
  public abstract String codegenTargetPlatform();
  
  public abstract String compliance();
  
  public abstract String problemAssertIdentifier();
  
  public abstract String problemEnumIdentifier();
  
  public abstract String source();
}
