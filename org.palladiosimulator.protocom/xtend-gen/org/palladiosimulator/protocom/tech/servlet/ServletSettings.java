package org.palladiosimulator.protocom.tech.servlet;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.xml.IJeeSettings;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class ServletSettings extends ConceptMapping<org.palladiosimulator.pcm.system.System> implements IJeeSettings {
  private final String contentId;
  
  public ServletSettings(final org.palladiosimulator.pcm.system.System pcmEntity, final String contentId) {
    super(pcmEntity);
    this.contentId = contentId;
  }
  
  @Override
  public String content() {
    final String contentId = this.contentId;
    switch (contentId) {
      case ".jsdtscope":
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _builder.newLine();
        _builder.append("<classpath>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<classpathentry kind=\"src\" path=\"WebContent\"/>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.JRE_CONTAINER\"/>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.WebProject\">");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("<attributes>");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("<attribute name=\"hide\" value=\"true\"/>");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("</attributes>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("</classpathentry>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<classpathentry kind=\"con\" path=\"org.eclipse.wst.jsdt.launching.baseBrowserLibrary\"/>");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("<classpathentry kind=\"output\" path=\"\"/>");
        _builder.newLine();
        _builder.append("</classpath>");
        _builder.newLine();
        return _builder.toString();
      case "org.eclipse.jdt.core.prefs":
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("eclipse.preferences.version=1");
        _builder_1.newLine();
        _builder_1.append("org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled");
        _builder_1.newLine();
        _builder_1.append("org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.7");
        _builder_1.newLine();
        _builder_1.append("org.eclipse.jdt.core.compiler.compliance=1.7");
        _builder_1.newLine();
        _builder_1.append("org.eclipse.jdt.core.compiler.problem.assertIdentifier=error");
        _builder_1.newLine();
        _builder_1.append("org.eclipse.jdt.core.compiler.problem.enumIdentifier=error");
        _builder_1.newLine();
        _builder_1.append("org.eclipse.jdt.core.compiler.source=1.7");
        _builder_1.newLine();
        return _builder_1.toString();
      case "org.eclipse.wst.common.component":
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _builder_2.newLine();
        _builder_2.append("<project-modules id=\"moduleCoreId\" project-version=\"1.5.0\">");
        _builder_2.newLine();
        _builder_2.append("\t");
        _builder_2.append("<wb-module deploy-name=\"org.palladiosimulator.temporary\">");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("<wb-resource deploy-path=\"/\" source-path=\"/WebContent\" tag=\"defaultRootSource\"/>");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("<wb-resource deploy-path=\"/WEB-INF/classes\" source-path=\"/src\"/>");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("<property name=\"context-root\" value=\"org.palladiosimulator.temporary\"/>");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("<property name=\"java-output-path\" value=\"/org.palladiosimulator.temporary/build/classes\"/>");
        _builder_2.newLine();
        _builder_2.append("\t");
        _builder_2.append("</wb-module>");
        _builder_2.newLine();
        _builder_2.append("</project-modules>");
        _builder_2.newLine();
        return _builder_2.toString();
      case "org.eclipse.wst.common.project.facet.core.xml":
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        _builder_3.newLine();
        _builder_3.append("<faceted-project>");
        _builder_3.newLine();
        _builder_3.append("\t");
        _builder_3.append("<runtime name=\"Java Web Tomcat 7\"/>");
        _builder_3.newLine();
        _builder_3.append("\t");
        _builder_3.append("<fixed facet=\"java\"/>");
        _builder_3.newLine();
        _builder_3.append("\t");
        _builder_3.append("<fixed facet=\"wst.jsdt.web\"/>");
        _builder_3.newLine();
        _builder_3.append("\t");
        _builder_3.append("<fixed facet=\"jst.web\"/>");
        _builder_3.newLine();
        _builder_3.append("\t");
        _builder_3.append("<installed facet=\"java\" version=\"1.7\"/>");
        _builder_3.newLine();
        _builder_3.append("\t");
        _builder_3.append("<installed facet=\"jst.web\" version=\"3.0\"/>");
        _builder_3.newLine();
        _builder_3.append("\t");
        _builder_3.append("<installed facet=\"wst.jsdt.web\" version=\"1.0\"/>");
        _builder_3.newLine();
        _builder_3.append("</faceted-project>");
        _builder_3.newLine();
        return _builder_3.toString();
      case "org.eclipse.wst.jsdt.ui.superType.container":
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("org.eclipse.wst.jsdt.launching.baseBrowserLibrary");
        return _builder_4.toString();
      case "org.eclipse.wst.jsdt.ui.superType.name":
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append("Window");
        return _builder_5.toString();
    }
    return null;
  }
  
  @Override
  public String filePath() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(".settings/");
    _builder.append(this.contentId, "");
    return _builder.toString();
  }
  
  @Override
  public String projectName() {
    return null;
  }
}
