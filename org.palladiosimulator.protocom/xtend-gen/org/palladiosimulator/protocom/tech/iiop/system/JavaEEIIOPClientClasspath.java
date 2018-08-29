package org.palladiosimulator.protocom.tech.iiop.system;

import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.system.JavaEEIIOPClasspath;

@SuppressWarnings("all")
public class JavaEEIIOPClientClasspath extends JavaEEIIOPClasspath {
  public JavaEEIIOPClientClasspath(final BasicComponent pcmEntity) {
    super(pcmEntity);
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
    _builder.append("<attribute name=\"owner.project.facets\" value=\"jst.utility\"/>");
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
    return _builder.toString();
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEOperationInterfaceProjectName(this.pcmEntity);
  }
  
  @Override
  public String clientClassPathEntry() {
    return null;
  }
  
  @Override
  public Set<String> requiredClientProjects() {
    return CollectionLiterals.<String>newHashSet();
  }
}
