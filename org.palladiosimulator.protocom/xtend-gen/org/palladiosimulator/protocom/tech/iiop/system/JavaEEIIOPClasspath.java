package org.palladiosimulator.protocom.tech.iiop.system;

import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.core.entity.InterfaceRequiringEntity;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPClasspathFile;

@SuppressWarnings("all")
public class JavaEEIIOPClasspath extends JavaEEIIOPClasspathFile<BasicComponent> {
  private Set<AssemblyConnector> assemblyConnectors;
  
  public JavaEEIIOPClasspath(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  public JavaEEIIOPClasspath(final BasicComponent pcmEntity, final Set<AssemblyConnector> assemblyConnectorSet) {
    super(pcmEntity);
    this.assemblyConnectors = assemblyConnectorSet;
  }
  
  @Override
  public String classPathEntries() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<classpathentry kind=\"src\" path=\"ejbModule\"/>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.7\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<attributes>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<attribute name=\"owner.project.facets\" value=\"java\"/>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</attributes>");
    _builder.newLine();
    _builder.append("</classpathentry>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"con\" path=\"oracle.eclipse.tools.glassfish.lib.system\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<attributes>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<attribute name=\"owner.project.facets\" value=\"jst.ejb\"/>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</attributes>");
    _builder.newLine();
    _builder.append("</classpathentry>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.module.container\"/>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"var\" path=\"ECLIPSE_HOME/plugins/");
    String _pluginJar = this.pluginJar("de.uka.ipd.sdq.simucomframework.variables");
    _builder.append(_pluginJar, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<classpathentry kind=\"var\" path=\"ECLIPSE_HOME/plugins/");
    String _pluginJar_1 = this.pluginJar("org.palladiosimulator.protocom.framework.java.ee");
    _builder.append(_pluginJar_1, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<classpathentry kind=\"var\" path=\"ECLIPSE_HOME/plugins/");
    String _pluginJar_2 = this.pluginJar("org.palladiosimulator.protocom.framework.java.se");
    _builder.append(_pluginJar_2, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<classpathentry kind=\"var\" path=\"ECLIPSE_HOME/plugins/");
    String _pluginJar_3 = this.pluginJar("org.palladiosimulator.protocom.resourcestrategies");
    _builder.append(_pluginJar_3, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    return ".classpath";
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEDescriptorProjectName(this.pcmEntity);
  }
  
  @Override
  public String clientClassPathEntry() {
    return JavaNames.fqnJavaEEClientDeployName(this.pcmEntity);
  }
  
  @Override
  public Set<String> requiredClientProjects() {
    final Function1<AssemblyConnector, Boolean> _function = (AssemblyConnector it) -> {
      OperationRequiredRole _requiredRole_AssemblyConnector = it.getRequiredRole_AssemblyConnector();
      InterfaceRequiringEntity _requiringEntity_RequiredRole = _requiredRole_AssemblyConnector.getRequiringEntity_RequiredRole();
      return Boolean.valueOf(_requiringEntity_RequiredRole.equals(this.pcmEntity));
    };
    final Iterable<AssemblyConnector> basicComponentAssemblyConnectors = IterableExtensions.<AssemblyConnector>filter(this.assemblyConnectors, _function);
    final HashSet<String> results = CollectionLiterals.<String>newHashSet();
    for (final AssemblyConnector assemblyConnector : basicComponentAssemblyConnectors) {
      {
        OperationProvidedRole assemblyProvidedRole = assemblyConnector.getProvidedRole_AssemblyConnector();
        InterfaceProvidingEntity _providingEntity_ProvidedRole = assemblyProvidedRole.getProvidingEntity_ProvidedRole();
        String _fqnJavaEEClientDeployName = JavaNames.fqnJavaEEClientDeployName(_providingEntity_ProvidedRole);
        Iterables.<String>addAll(results, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_fqnJavaEEClientDeployName)));
      }
    }
    return results;
  }
}
