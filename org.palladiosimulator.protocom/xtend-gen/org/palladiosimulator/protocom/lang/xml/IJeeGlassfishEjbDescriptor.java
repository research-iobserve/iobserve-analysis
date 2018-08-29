package org.palladiosimulator.protocom.lang.xml;

import java.util.HashMap;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.protocom.lang.xml.IJeeDescriptor;

@SuppressWarnings("all")
public interface IJeeGlassfishEjbDescriptor extends IJeeDescriptor {
  public abstract String jndiName();
  
  public abstract String ipAddress();
  
  public abstract HashMap<AssemblyConnector, String> requiredComponentsAndResourceContainerIPAddress();
}
