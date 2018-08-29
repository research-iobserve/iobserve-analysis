package org.palladiosimulator.protocom.traverse.jeeservlet.repository;

import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.lang.java.impl.JInterface;
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentClass;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentContextClass;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentContextInterface;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletBasicComponentPortClass;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletComponentClassInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XBasicComponent;

/**
 * @author Christian Klaussner
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class JeeServletBasicComponent extends XBasicComponent {
  @Override
  protected void generate() {
    final BasicComponentAdapter adapter = new BasicComponentAdapter(this.entity);
    JInterface _instance = this.injector.<JInterface>getInstance(JInterface.class);
    ServletComponentClassInterface _servletComponentClassInterface = new ServletComponentClassInterface(this.entity);
    GeneratedFile<IJInterface> _createFor = _instance.createFor(_servletComponentClassInterface);
    this.generatedFiles.add(_createFor);
    JClass _instance_1 = this.injector.<JClass>getInstance(JClass.class);
    ServletBasicComponentClass _servletBasicComponentClass = new ServletBasicComponentClass(adapter, this.entity);
    GeneratedFile<IJClass> _createFor_1 = _instance_1.createFor(_servletBasicComponentClass);
    this.generatedFiles.add(_createFor_1);
    JInterface _instance_2 = this.injector.<JInterface>getInstance(JInterface.class);
    ServletBasicComponentContextInterface _servletBasicComponentContextInterface = new ServletBasicComponentContextInterface(this.entity);
    GeneratedFile<IJInterface> _createFor_2 = _instance_2.createFor(_servletBasicComponentContextInterface);
    this.generatedFiles.add(_createFor_2);
    JClass _instance_3 = this.injector.<JClass>getInstance(JClass.class);
    ServletBasicComponentContextClass _servletBasicComponentContextClass = new ServletBasicComponentContextClass(adapter, this.entity);
    GeneratedFile<IJClass> _createFor_3 = _instance_3.createFor(_servletBasicComponentContextClass);
    this.generatedFiles.add(_createFor_3);
    EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.entity.getProvidedRoles_InterfaceProvidingEntity();
    final Consumer<ProvidedRole> _function = (ProvidedRole it) -> {
      JClass _instance_4 = this.injector.<JClass>getInstance(JClass.class);
      ServletBasicComponentPortClass _servletBasicComponentPortClass = new ServletBasicComponentPortClass(it);
      GeneratedFile<IJClass> _createFor_4 = _instance_4.createFor(_servletBasicComponentPortClass);
      this.generatedFiles.add(_createFor_4);
    };
    _providedRoles_InterfaceProvidingEntity.forEach(_function);
  }
}
