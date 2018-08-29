package org.palladiosimulator.protocom.tech.iiop;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.prefs.IJeePreferences;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPPreferencesFile<E extends Entity> extends ConceptMapping<E> implements IJeePreferences {
  public JavaEEIIOPPreferencesFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String eclipsePreferencesVersion() {
    return null;
  }
  
  @Override
  public String codegenInlineJsrBytecode() {
    return null;
  }
  
  @Override
  public String codegenTargetPlatform() {
    return null;
  }
  
  @Override
  public String compliance() {
    return null;
  }
  
  @Override
  public String problemAssertIdentifier() {
    return null;
  }
  
  @Override
  public String problemEnumIdentifier() {
    return null;
  }
  
  @Override
  public String source() {
    return null;
  }
  
  @Override
  public String filePath() {
    return null;
  }
  
  @Override
  public String projectName() {
    return null;
  }
}
