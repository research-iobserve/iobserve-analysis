package org.palladiosimulator.protocom.tech.iiop;

import java.util.Collection;
import java.util.HashMap;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.xml.IJeeEjbDescriptor;
import org.palladiosimulator.protocom.lang.xml.IJeeGlassfishEjbDescriptor;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPDescriptor<E extends Entity> extends ConceptMapping<E> implements IJeeEjbDescriptor, IJeeGlassfishEjbDescriptor {
  public JavaEEIIOPDescriptor(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String ejbName() {
    return null;
  }
  
  @Override
  public Collection<String> ejbRefName() {
    return CollectionLiterals.<String>newLinkedList();
  }
  
  @Override
  public String displayName() {
    return null;
  }
  
  @Override
  public String filePath() {
    return null;
  }
  
  @Override
  public String ejbClientJar() {
    return null;
  }
  
  @Override
  public String jndiName() {
    return null;
  }
  
  @Override
  public String projectName() {
    return null;
  }
  
  @Override
  public String ipAddress() {
    return null;
  }
  
  @Override
  public HashMap<AssemblyConnector, String> requiredComponentsAndResourceContainerIPAddress() {
    return null;
  }
}
