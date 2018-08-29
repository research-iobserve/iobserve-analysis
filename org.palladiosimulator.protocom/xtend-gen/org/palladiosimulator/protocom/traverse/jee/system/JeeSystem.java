package org.palladiosimulator.protocom.traverse.jee.system;

import com.google.common.collect.Iterables;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJeeClass;
import org.palladiosimulator.protocom.lang.java.impl.JeeClass;
import org.palladiosimulator.protocom.lang.txt.IReadMe;
import org.palladiosimulator.protocom.lang.txt.impl.JeeReadMe;
import org.palladiosimulator.protocom.lang.xml.IJeeClasspath;
import org.palladiosimulator.protocom.lang.xml.impl.JeeClasspath;
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPBasicComponentClass;
import org.palladiosimulator.protocom.tech.iiop.system.JavaEEIIOPClasspath;
import org.palladiosimulator.protocom.tech.iiop.system.JavaEEIIOPClientClasspath;
import org.palladiosimulator.protocom.tech.iiop.system.JavaEEIIOPReadMe;
import org.palladiosimulator.protocom.traverse.framework.system.XSystem;

@SuppressWarnings("all")
public class JeeSystem extends XSystem {
  @Override
  public void generate() {
    EList<Connector> _connectors__ComposedStructure = this.entity.getConnectors__ComposedStructure();
    Iterable<AssemblyConnector> _filter = Iterables.<AssemblyConnector>filter(_connectors__ComposedStructure, AssemblyConnector.class);
    final Set<AssemblyConnector> assemblyConnectorSet = IterableExtensions.<AssemblyConnector>toSet(_filter);
    final EList<AssemblyContext> repositoryComponentList = this.entity.getAssemblyContexts__ComposedStructure();
    final Function1<AssemblyContext, Boolean> _function = (AssemblyContext it) -> {
      RepositoryComponent _encapsulatedComponent__AssemblyContext = it.getEncapsulatedComponent__AssemblyContext();
      return Boolean.valueOf(BasicComponent.class.isInstance(_encapsulatedComponent__AssemblyContext));
    };
    Iterable<AssemblyContext> _filter_1 = IterableExtensions.<AssemblyContext>filter(repositoryComponentList, _function);
    final Function1<AssemblyContext, BasicComponent> _function_1 = (AssemblyContext it) -> {
      RepositoryComponent _encapsulatedComponent__AssemblyContext = it.getEncapsulatedComponent__AssemblyContext();
      return ((BasicComponent) _encapsulatedComponent__AssemblyContext);
    };
    Iterable<BasicComponent> _map = IterableExtensions.<AssemblyContext, BasicComponent>map(_filter_1, _function_1);
    final Consumer<BasicComponent> _function_2 = (BasicComponent it) -> {
      JeeClasspath _instance = this.injector.<JeeClasspath>getInstance(JeeClasspath.class);
      JavaEEIIOPClasspath _javaEEIIOPClasspath = new JavaEEIIOPClasspath(it, assemblyConnectorSet);
      GeneratedFile<IJeeClasspath> _createFor = _instance.createFor(_javaEEIIOPClasspath);
      this.generatedFiles.add(_createFor);
    };
    _map.forEach(_function_2);
    final Function1<AssemblyContext, Boolean> _function_3 = (AssemblyContext it) -> {
      RepositoryComponent _encapsulatedComponent__AssemblyContext = it.getEncapsulatedComponent__AssemblyContext();
      return Boolean.valueOf(BasicComponent.class.isInstance(_encapsulatedComponent__AssemblyContext));
    };
    Iterable<AssemblyContext> _filter_2 = IterableExtensions.<AssemblyContext>filter(repositoryComponentList, _function_3);
    final Function1<AssemblyContext, BasicComponent> _function_4 = (AssemblyContext it) -> {
      RepositoryComponent _encapsulatedComponent__AssemblyContext = it.getEncapsulatedComponent__AssemblyContext();
      return ((BasicComponent) _encapsulatedComponent__AssemblyContext);
    };
    Iterable<BasicComponent> _map_1 = IterableExtensions.<AssemblyContext, BasicComponent>map(_filter_2, _function_4);
    final Consumer<BasicComponent> _function_5 = (BasicComponent it) -> {
      JeeClasspath _instance = this.injector.<JeeClasspath>getInstance(JeeClasspath.class);
      JavaEEIIOPClientClasspath _javaEEIIOPClientClasspath = new JavaEEIIOPClientClasspath(it);
      GeneratedFile<IJeeClasspath> _createFor = _instance.createFor(_javaEEIIOPClientClasspath);
      this.generatedFiles.add(_createFor);
    };
    _map_1.forEach(_function_5);
    final Function1<AssemblyContext, Boolean> _function_6 = (AssemblyContext it) -> {
      RepositoryComponent _encapsulatedComponent__AssemblyContext = it.getEncapsulatedComponent__AssemblyContext();
      return Boolean.valueOf(BasicComponent.class.isInstance(_encapsulatedComponent__AssemblyContext));
    };
    Iterable<AssemblyContext> _filter_3 = IterableExtensions.<AssemblyContext>filter(repositoryComponentList, _function_6);
    final Function1<AssemblyContext, BasicComponent> _function_7 = (AssemblyContext it) -> {
      RepositoryComponent _encapsulatedComponent__AssemblyContext = it.getEncapsulatedComponent__AssemblyContext();
      return ((BasicComponent) _encapsulatedComponent__AssemblyContext);
    };
    Iterable<BasicComponent> _map_2 = IterableExtensions.<AssemblyContext, BasicComponent>map(_filter_3, _function_7);
    final Consumer<BasicComponent> _function_8 = (BasicComponent it) -> {
      JeeClass _instance = this.injector.<JeeClass>getInstance(JeeClass.class);
      JavaEEIIOPBasicComponentClass _javaEEIIOPBasicComponentClass = new JavaEEIIOPBasicComponentClass(it, assemblyConnectorSet);
      GeneratedFile<IJeeClass> _createFor = _instance.createFor(_javaEEIIOPBasicComponentClass);
      this.generatedFiles.add(_createFor);
    };
    _map_2.forEach(_function_8);
    JeeReadMe _instance = this.injector.<JeeReadMe>getInstance(JeeReadMe.class);
    JavaEEIIOPReadMe _javaEEIIOPReadMe = new JavaEEIIOPReadMe(this.entity);
    GeneratedFile<IReadMe> _createFor = _instance.createFor(_javaEEIIOPReadMe);
    this.generatedFiles.add(_createFor);
  }
}
