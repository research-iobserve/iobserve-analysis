package org.palladiosimulator.protocom.tech.servlet;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.xml.IClasspath;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class ServletClasspath<E extends Entity> extends ConceptMapping<E> implements IClasspath {
  public ServletClasspath(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String classPathEntries() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<classpathentry kind=\"src\" path=\"src\"/>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"con\" path=\"");
    Path _path = new Path(JavaRuntime.JRE_CONTAINER);
    _builder.append(_path, "");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
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
    _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.jst.server.core.container/com.sap.core.javaweb.tomcat.classpath/Java Web Tomcat 7\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<attributes>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<attribute name=\"owner.project.facets\" value=\"jst.web\"/>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</attributes>");
    _builder.newLine();
    _builder.append("</classpathentry>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.web.container\"/>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.jst.j2ee.internal.module.container\"/>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"output\" path=\"build/classes\"/>");
    _builder.newLine();
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    return ".classpath";
  }
  
  @Override
  public String projectName() {
    return null;
  }
}
