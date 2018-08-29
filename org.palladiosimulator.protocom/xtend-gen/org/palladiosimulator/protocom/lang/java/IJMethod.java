package org.palladiosimulator.protocom.lang.java;

import java.util.Collection;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;

/**
 * TODO: Common IJMember for Method and Field.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public interface IJMethod {
  /**
   * The name of the return type of this method.
   * 
   * Default value is VOID.
   */
  public abstract String returnType();
  
  /**
   * The name of the method.
   */
  public abstract String name();
  
  /**
   * The flattened parameter list as a string.
   */
  public abstract String parameters();
  
  /**
   * The throw statement.
   */
  public abstract String throwsType();
  
  /**
   * Code of the method as a string.
   * 
   * If not set, the method will be treated as abstract.
   */
  public abstract String body();
  
  /**
   * The visibility modifier of this method.
   * 
   * Default value is PUBLIC.
   */
  public abstract String visibilityModifier();
  
  /**
   * The static modifier.
   * 
   * TODO: Move up.
   */
  public abstract String staticModifier();
  
  public abstract boolean isStatic();
  
  public abstract Collection<? extends IJAnnotation> annotations();
  
  public abstract String methodAnnotation();
}
