package org.palladiosimulator.protocom.tech.iiop.system;

import com.google.common.collect.ArrayListMultimap;
import java.util.HashMap;
import java.util.LinkedList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPReadMeFile;

@SuppressWarnings("all")
public class JavaEEIIOPReadMe extends JavaEEIIOPReadMeFile<org.palladiosimulator.pcm.system.System> {
  public JavaEEIIOPReadMe(final org.palladiosimulator.pcm.system.System pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String filePath() {
    return "ReadMe.txt";
  }
  
  @Override
  public String projectName() {
    return ".ReadMe";
  }
  
  @Override
  public HashMap<String, String> basicComponentClassName() {
    final HashMap<String, String> basicComponentClasses = CollectionLiterals.<String, String>newHashMap();
    EList<AssemblyContext> _assemblyContexts__ComposedStructure = this.pcmEntity.getAssemblyContexts__ComposedStructure();
    for (final AssemblyContext assemblyContext : _assemblyContexts__ComposedStructure) {
      RepositoryComponent _encapsulatedComponent__AssemblyContext = assemblyContext.getEncapsulatedComponent__AssemblyContext();
      String _fqnJavaEEBasicComponentProjectName = JavaNames.fqnJavaEEBasicComponentProjectName(_encapsulatedComponent__AssemblyContext);
      RepositoryComponent _encapsulatedComponent__AssemblyContext_1 = assemblyContext.getEncapsulatedComponent__AssemblyContext();
      String _fqnJavaEEBasicComponentClassName = JavaNames.fqnJavaEEBasicComponentClassName(_encapsulatedComponent__AssemblyContext_1);
      basicComponentClasses.put(_fqnJavaEEBasicComponentProjectName, _fqnJavaEEBasicComponentClassName);
    }
    return basicComponentClasses;
  }
  
  @Override
  public ArrayListMultimap<String, String> basicComponentPortClassName() {
    final ArrayListMultimap<String, String> basicComponentPortClasses = ArrayListMultimap.<String, String>create();
    EList<AssemblyContext> _assemblyContexts__ComposedStructure = this.pcmEntity.getAssemblyContexts__ComposedStructure();
    for (final AssemblyContext assemblyContext : _assemblyContexts__ComposedStructure) {
      {
        RepositoryComponent _encapsulatedComponent__AssemblyContext = assemblyContext.getEncapsulatedComponent__AssemblyContext();
        EList<ProvidedRole> providedRoles = _encapsulatedComponent__AssemblyContext.getProvidedRoles_InterfaceProvidingEntity();
        LinkedList<String> portNames = CollectionLiterals.<String>newLinkedList();
        for (final ProvidedRole providedRole : providedRoles) {
          String _portClassName = JavaNames.portClassName(providedRole);
          portNames.add(_portClassName);
        }
        RepositoryComponent _encapsulatedComponent__AssemblyContext_1 = assemblyContext.getEncapsulatedComponent__AssemblyContext();
        String _fqnJavaEEBasicComponentProjectName = JavaNames.fqnJavaEEBasicComponentProjectName(_encapsulatedComponent__AssemblyContext_1);
        basicComponentPortClasses.putAll(_fqnJavaEEBasicComponentProjectName, portNames);
      }
    }
    return basicComponentPortClasses;
  }
}
