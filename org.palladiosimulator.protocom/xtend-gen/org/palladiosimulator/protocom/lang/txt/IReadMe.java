package org.palladiosimulator.protocom.lang.txt;

import com.google.common.collect.ArrayListMultimap;
import java.util.HashMap;
import org.palladiosimulator.protocom.lang.ICompilationUnit;

@SuppressWarnings("all")
public interface IReadMe extends ICompilationUnit {
  public abstract HashMap<String, String> basicComponentClassName();
  
  public abstract ArrayListMultimap<String, String> basicComponentPortClassName();
}
