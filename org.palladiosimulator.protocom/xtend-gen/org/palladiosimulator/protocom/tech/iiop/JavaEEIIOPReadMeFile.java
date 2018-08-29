package org.palladiosimulator.protocom.tech.iiop;

import com.google.common.collect.ArrayListMultimap;
import java.util.HashMap;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.txt.IReadMe;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPReadMeFile<E extends Entity> extends ConceptMapping<E> implements IReadMe {
  public JavaEEIIOPReadMeFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String filePath() {
    return null;
  }
  
  @Override
  public String projectName() {
    return null;
  }
  
  @Override
  public HashMap<String, String> basicComponentClassName() {
    return CollectionLiterals.<String, String>newHashMap();
  }
  
  @Override
  public ArrayListMultimap<String, String> basicComponentPortClassName() {
    return ArrayListMultimap.<String, String>create();
  }
}
