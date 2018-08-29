package org.palladiosimulator.protocom.traverse.jsestub.repository;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.impl.JInterface;
import org.palladiosimulator.protocom.tech.pojo.repository.PojoOperationInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XOperationInterface;

/**
 * An Operation Interface translates into the following Java compilation units:
 * <ul>
 * 	<li> an interface.
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class JseStubOperationInterface extends XOperationInterface {
  @Override
  public void generate() {
    JInterface _instance = this.injector.<JInterface>getInstance(JInterface.class);
    PojoOperationInterface _pojoOperationInterface = new PojoOperationInterface(this.entity);
    GeneratedFile<IJInterface> _createFor = _instance.createFor(_pojoOperationInterface);
    this.generatedFiles.add(_createFor);
  }
}
