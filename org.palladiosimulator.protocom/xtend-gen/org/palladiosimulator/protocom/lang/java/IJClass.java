package org.palladiosimulator.protocom.lang.java;

import java.util.Collection;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJCompilationUnit;
import org.palladiosimulator.protocom.lang.java.IJMethod;

@SuppressWarnings("all")
public interface IJClass extends IJCompilationUnit {
  /**
   * Inherited class name.
   */
  public abstract String superClass();
  
  /**
   * Constructors of this class.
   * 
   * FIXME: JMethod is ok'ish, but not entirely correct.
   */
  public abstract Collection<? extends IJMethod> constructors();
  
  public abstract Collection<? extends IJAnnotation> annotations();
}
