package org.palladiosimulator.protocom.model.system;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.protocom.model.allocation.AssemblyContextAdapter;
import org.palladiosimulator.protocom.model.repository.InterfaceProvidingRequiringEntityAdapter;

/**
 * Adapter for PCM System entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class SystemAdapter extends InterfaceProvidingRequiringEntityAdapter<org.palladiosimulator.pcm.system.System> {
  public SystemAdapter(final org.palladiosimulator.pcm.system.System entity) {
    super(entity);
  }
  
  public List<AssemblyContextAdapter> getAssemblyContexts() {
    EList<AssemblyContext> _assemblyContexts__ComposedStructure = this.entity.getAssemblyContexts__ComposedStructure();
    final Function1<AssemblyContext, AssemblyContextAdapter> _function = (AssemblyContext it) -> {
      return new AssemblyContextAdapter(it);
    };
    return ListExtensions.<AssemblyContext, AssemblyContextAdapter>map(_assemblyContexts__ComposedStructure, _function);
  }
  
  public String getInterfaceName() {
    String _safeName = this.getSafeName(this.entity);
    return ("I" + _safeName);
  }
}
