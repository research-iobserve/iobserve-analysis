package org.palladiosimulator.protocom.traverse.jsestub.system;

import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.lang.java.impl.JInterface;
import org.palladiosimulator.protocom.lang.manifest.IJseManifest;
import org.palladiosimulator.protocom.lang.manifest.impl.JseManifest;
import org.palladiosimulator.protocom.lang.properties.IBuildProperties;
import org.palladiosimulator.protocom.lang.properties.impl.BuildProperties;
import org.palladiosimulator.protocom.lang.xml.IClasspath;
import org.palladiosimulator.protocom.lang.xml.IPluginXml;
import org.palladiosimulator.protocom.lang.xml.impl.Classpath;
import org.palladiosimulator.protocom.lang.xml.impl.PluginXml;
import org.palladiosimulator.protocom.tech.pojo.repository.PojoComposedStructurePortClass;
import org.palladiosimulator.protocom.tech.pojo.system.PojoBuildProperties;
import org.palladiosimulator.protocom.tech.pojo.system.PojoManifest;
import org.palladiosimulator.protocom.tech.pojo.system.PojoPluginXml;
import org.palladiosimulator.protocom.tech.pojo.system.PojoSystemClass;
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextClass;
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureContextInterface;
import org.palladiosimulator.protocom.tech.rmi.repository.PojoComposedStructureInterface;
import org.palladiosimulator.protocom.tech.rmi.system.PojoClasspath;
import org.palladiosimulator.protocom.traverse.framework.system.XSystem;

/**
 * An System translates into the following Java compilation units:
 * <ul>
 * 	<li> a class used to setup the assembly (a System is a Composed Structure),
 * 	<li> an interface for this component's class,
 * 	<li> a context class for assembly (basically unused, can be removed?),
 * 	<li> an interface for the context class,
 *  <li> a class for each component's port, used by the Usage Scenario. TODO: Move to traverse
 * </ul>
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class JseStubSystem extends XSystem {
  @Override
  public void generate() {
    JInterface _instance = this.injector.<JInterface>getInstance(JInterface.class);
    PojoComposedStructureInterface _pojoComposedStructureInterface = new PojoComposedStructureInterface(this.entity);
    GeneratedFile<IJInterface> _createFor = _instance.createFor(_pojoComposedStructureInterface);
    this.generatedFiles.add(_createFor);
    JClass _instance_1 = this.injector.<JClass>getInstance(JClass.class);
    PojoSystemClass _pojoSystemClass = new PojoSystemClass(this.entity);
    GeneratedFile<IJClass> _createFor_1 = _instance_1.createFor(_pojoSystemClass);
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
    JseManifest _instance_4 = this.injector.<JseManifest>getInstance(JseManifest.class);
    PojoManifest _pojoManifest = new PojoManifest(this.entity);
    GeneratedFile<IJseManifest> _createFor_4 = _instance_4.createFor(_pojoManifest);
    this.generatedFiles.add(_createFor_4);
    PluginXml _instance_5 = this.injector.<PluginXml>getInstance(PluginXml.class);
    PojoPluginXml _pojoPluginXml = new PojoPluginXml(this.entity);
    GeneratedFile<IPluginXml> _createFor_5 = _instance_5.createFor(_pojoPluginXml);
    this.generatedFiles.add(_createFor_5);
    BuildProperties _instance_6 = this.injector.<BuildProperties>getInstance(BuildProperties.class);
    PojoBuildProperties _pojoBuildProperties = new PojoBuildProperties(this.entity);
    GeneratedFile<IBuildProperties> _createFor_6 = _instance_6.createFor(_pojoBuildProperties);
    this.generatedFiles.add(_createFor_6);
    Classpath _instance_7 = this.injector.<Classpath>getInstance(Classpath.class);
    PojoClasspath _pojoClasspath = new PojoClasspath(this.entity);
    GeneratedFile<IClasspath> _createFor_7 = _instance_7.createFor(_pojoClasspath);
    this.generatedFiles.add(_createFor_7);
  }
}
