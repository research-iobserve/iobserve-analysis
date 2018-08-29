package org.palladiosimulator.protocom.lang;

/**
 * This interface is the smallest common subset of attributes for generated
 * compilation units of all languages, e.g., Java classes or interfaces.
 * 
 * TODO: File or something else.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public interface ICompilationUnit {
  /**
   * Path and name of the generated file.
   */
  public abstract String filePath();
  
  public abstract String projectName();
}
