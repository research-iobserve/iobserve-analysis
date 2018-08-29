package org.palladiosimulator.protocom.lang.java;

import java.util.Collection;
import org.palladiosimulator.protocom.lang.ICompilationUnit;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;

/**
 * Common attributes of Java compilation units.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public interface IJCompilationUnit extends ICompilationUnit {
  /**
   * Package name of this compilation unit.
   */
  public abstract String packageName();
  
  /**
   * Name of the compilation unit.
   */
  public abstract String compilationUnitName();
  
  /**
   * Collection of interface names which are either implemented or extended.
   */
  public abstract Collection<String> interfaces();
  
  /**
   * Methods (or signatures) defined by this compilation unit.
   */
  public abstract Collection<? extends IJMethod> methods();
  
  /**
   * Fields of this compilation unit.
   */
  public abstract Collection<? extends IJField> fields();
}
