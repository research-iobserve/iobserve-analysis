package org.palladiosimulator.protocom.lang.java;

@SuppressWarnings("all")
public interface IJField {
  /**
   * The name of the field.
   */
  public abstract String name();
  
  /**
   * Type of the field.
   */
  public abstract String type();
  
  /**
   * The visibility modifier of this field.
   * 
   * Default value is PROTECTED.
   */
  public abstract String visibility();
  
  public abstract boolean staticModifier();
  
  public abstract boolean finalModifier();
  
  public abstract String initialization();
}
