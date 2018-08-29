package org.palladiosimulator.protocom.traverse.jeeservlet.allocation;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.model.allocation.AllocationAdapter;
import org.palladiosimulator.protocom.tech.servlet.allocation.ServletAllocationStorage;
import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation;

@SuppressWarnings("all")
public class JeeServletAllocation extends XAllocation {
  @Override
  protected void generate() {
    final AllocationAdapter adapter = new AllocationAdapter(this.entity);
    JClass _instance = this.injector.<JClass>getInstance(JClass.class);
    ServletAllocationStorage _servletAllocationStorage = new ServletAllocationStorage(adapter, this.entity);
    GeneratedFile<IJClass> _createFor = _instance.createFor(_servletAllocationStorage);
    this.generatedFiles.add(_createFor);
  }
}
