package org.palladiosimulator.protocom.lang.xml;

import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface ITestPlan extends ICompilationUnit {
  public abstract String content();
  
  public abstract int population();
  
  public abstract int thinkTime();
  
  public abstract String scenarioName();
}
