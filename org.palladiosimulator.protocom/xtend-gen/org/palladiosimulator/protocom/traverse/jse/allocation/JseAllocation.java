package org.palladiosimulator.protocom.traverse.jse.allocation;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.tech.rmi.allocation.PojoAllocationStorage;
import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation;

/**
 * An Allocation for JSE translates into one additional container information file.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public class JseAllocation extends XAllocation {
  @Override
  public void generate() {
    JClass _instance = this.injector.<JClass>getInstance(JClass.class);
    PojoAllocationStorage _pojoAllocationStorage = new PojoAllocationStorage(this.entity);
    GeneratedFile<IJClass> _createFor = _instance.createFor(_pojoAllocationStorage);
    this.generatedFiles.add(_createFor);
  }
}
