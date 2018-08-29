package org.palladiosimulator.protocom.traverse.jsestub.repository;

import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.lang.java.impl.JInterface;
import org.palladiosimulator.protocom.tech.pojo.repository.PojoComposedStructurePortClass;
import org.palladiosimulator.protocom.tech.pojo.repository.PojoCompositeComponentClass;
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextClass;
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextInterface;
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XCompositeComponent;

/**
 * An CompositeComponent translates into the following Java compilation units:
 * <ul>
 * 	<li> a class used to setup the assembly (a CompositeComponent is a ComposedStructure),
 * 	<li> an interface for this component's class,
 * 	<li> a context class for assembly,
 * 	<li> an interface for the context class,
 *  <li> a class for each component's port. TODO: Move to traverse
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class JseStubCompositeComponent extends XCompositeComponent {
  @Override
  public void generate() {
    JInterface _instance = this.injector.<JInterface>getInstance(JInterface.class);
    PojoComposedStructureInterface _pojoComposedStructureInterface = new PojoComposedStructureInterface(this.entity);
    GeneratedFile<IJInterface> _createFor = _instance.createFor(_pojoComposedStructureInterface);
    this.generatedFiles.add(_createFor);
    JClass _instance_1 = this.injector.<JClass>getInstance(JClass.class);
    PojoCompositeComponentClass _pojoCompositeComponentClass = new PojoCompositeComponentClass(this.entity);
    GeneratedFile<IJClass> _createFor_1 = _instance_1.createFor(_pojoCompositeComponentClass);
    this.generatedFiles.add(_createFor_1);
    JClass _instance_2 = this.injector.<JClass>getInstance(JClass.class);
    PojoComposedStructureContextClass _pojoComposedStructureContextClass = new PojoComposedStructureContextClass(this.entity);
    GeneratedFile<IJClass> _createFor_2 = _instance_2.createFor(_pojoComposedStructureContextClass);
    this.generatedFiles.add(_createFor_2);
    JInterface _instance_3 = this.injector.<JInterface>getInstance(JInterface.class);
    PojoComposedStructureContextInterface _pojoComposedStructureContextInterface = new PojoComposedStructureContextInterface(this.entity);
    GeneratedFile<IJInterface> _createFor_3 = _instance_3.createFor(_pojoComposedStructureContextInterface);
    this.generatedFiles.add(_createFor_3);
    EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.entity.getProvidedRoles_InterfaceProvidingEntity();
    final Consumer<ProvidedRole> _function = (ProvidedRole it) -> {
      JClass _instance_4 = this.injector.<JClass>getInstance(JClass.class);
      PojoComposedStructurePortClass _pojoComposedStructurePortClass = new PojoComposedStructurePortClass(it);
      GeneratedFile<IJClass> _createFor_4 = _instance_4.createFor(_pojoComposedStructurePortClass);
      this.generatedFiles.add(_createFor_4);
    };
    _providedRoles_InterfaceProvidingEntity.forEach(_function);
  }
}
