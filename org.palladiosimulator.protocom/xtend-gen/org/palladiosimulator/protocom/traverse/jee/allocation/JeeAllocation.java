package org.palladiosimulator.protocom.traverse.jee.allocation;

import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IJeeGlassfishEjbDescriptor;
import org.palladiosimulator.protocom.lang.xml.impl.JeeGlassfishEjbDescriptor;
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPGlassfishEjbDescriptor;
import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation;

@SuppressWarnings("all")
public class JeeAllocation extends XAllocation {
  @Override
  public void generate() {
    EList<AllocationContext> _allocationContexts_Allocation = this.entity.getAllocationContexts_Allocation();
    final Consumer<AllocationContext> _function = (AllocationContext it) -> {
      JeeGlassfishEjbDescriptor _instance = this.injector.<JeeGlassfishEjbDescriptor>getInstance(JeeGlassfishEjbDescriptor.class);
      JavaEEIIOPGlassfishEjbDescriptor _javaEEIIOPGlassfishEjbDescriptor = new JavaEEIIOPGlassfishEjbDescriptor(it);
      GeneratedFile<IJeeGlassfishEjbDescriptor> _createFor = _instance.createFor(_javaEEIIOPGlassfishEjbDescriptor);
      this.generatedFiles.add(_createFor);
    };
    _allocationContexts_Allocation.forEach(_function);
  }
}
