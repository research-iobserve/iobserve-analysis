package org.palladiosimulator.protocom.lang.java;

import java.util.List;

@SuppressWarnings("all")
public interface IJAnnotation {
  public abstract String name();
  
  public abstract List<String> values();
  
  public abstract String generate();
}
