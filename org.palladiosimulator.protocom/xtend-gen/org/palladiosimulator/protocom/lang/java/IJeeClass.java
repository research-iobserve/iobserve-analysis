package org.palladiosimulator.protocom.lang.java;

import java.util.Collection;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJField;

@SuppressWarnings("all")
public interface IJeeClass extends IJClass {
  public abstract String jeeClassStatelessAnnotation();
  
  public abstract String jeeClassDependencyInjectionAnnotation();
  
  public abstract Collection<? extends IJField> jeeClassDependencyInjection();
}
