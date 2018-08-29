package org.palladiosimulator.protocom.model.resourceenvironment;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceContainerAdapter;

/**
 * Adapter class for PCM ResourceEnvironment entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ResourceEnvironmentAdapter extends ModelAdapter<ResourceEnvironment> {
  public ResourceEnvironmentAdapter(final ResourceEnvironment entity) {
    super(entity);
  }
  
  /**
   * Gets the resource containers.
   * @return a list of adapters for the resource containers
   */
  public List<ResourceContainerAdapter> getResourceContainers() {
    EList<ResourceContainer> _resourceContainer_ResourceEnvironment = this.entity.getResourceContainer_ResourceEnvironment();
    final Function1<ResourceContainer, ResourceContainerAdapter> _function = (ResourceContainer it) -> {
      return new ResourceContainerAdapter(it);
    };
    return ListExtensions.<ResourceContainer, ResourceContainerAdapter>map(_resourceContainer_ResourceEnvironment, _function);
  }
}
