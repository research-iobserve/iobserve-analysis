package org.palladiosimulator.protocom.traverse.jeeservlet.system;

import com.google.common.base.Objects;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.framework.java.ee.webcontent.FileProvider;
import org.palladiosimulator.protocom.framework.java.ee.webcontent.FrameworkFile;
import org.palladiosimulator.protocom.lang.CopiedFile;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.lang.java.impl.JInterface;
import org.palladiosimulator.protocom.lang.xml.IClasspath;
import org.palladiosimulator.protocom.lang.xml.IJeeSettings;
import org.palladiosimulator.protocom.lang.xml.impl.Classpath;
import org.palladiosimulator.protocom.lang.xml.impl.JeeSettings;
import org.palladiosimulator.protocom.model.system.SystemAdapter;
import org.palladiosimulator.protocom.tech.servlet.ServletClasspath;
import org.palladiosimulator.protocom.tech.servlet.ServletDeploymentDescriptor;
import org.palladiosimulator.protocom.tech.servlet.ServletSettings;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletComposedStructureInterface;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletComposedStructurePortClass;
import org.palladiosimulator.protocom.tech.servlet.system.ServletSystemClass;
import org.palladiosimulator.protocom.tech.servlet.system.ServletSystemMain;
import org.palladiosimulator.protocom.traverse.framework.system.XSystem;

@SuppressWarnings("all")
public class JeeServletSystem extends XSystem {
  private final FileProvider fileProvider = new FileProvider();
  
  private boolean generateSettingsFile(final String contentId) {
    JeeSettings _instance = this.injector.<JeeSettings>getInstance(JeeSettings.class);
    ServletSettings _servletSettings = new ServletSettings(this.entity, contentId);
    GeneratedFile<IJeeSettings> _createFor = _instance.createFor(_servletSettings);
    return this.generatedFiles.add(_createFor);
  }
  
  @Override
  protected void generate() {
    final SystemAdapter adapter = new SystemAdapter(this.entity);
    JInterface _instance = this.injector.<JInterface>getInstance(JInterface.class);
    ServletComposedStructureInterface _servletComposedStructureInterface = new ServletComposedStructureInterface(this.entity);
    GeneratedFile<IJInterface> _createFor = _instance.createFor(_servletComposedStructureInterface);
    this.generatedFiles.add(_createFor);
    JClass _instance_1 = this.injector.<JClass>getInstance(JClass.class);
    ServletSystemClass<org.palladiosimulator.pcm.system.System> _servletSystemClass = new ServletSystemClass<org.palladiosimulator.pcm.system.System>(adapter, this.entity);
    GeneratedFile<IJClass> _createFor_1 = _instance_1.createFor(_servletSystemClass);
    this.generatedFiles.add(_createFor_1);
    EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.entity.getProvidedRoles_InterfaceProvidingEntity();
    final Consumer<ProvidedRole> _function = (ProvidedRole it) -> {
      JClass _instance_2 = this.injector.<JClass>getInstance(JClass.class);
      ServletComposedStructurePortClass _servletComposedStructurePortClass = new ServletComposedStructurePortClass(it);
      GeneratedFile<IJClass> _createFor_2 = _instance_2.createFor(_servletComposedStructurePortClass);
      this.generatedFiles.add(_createFor_2);
    };
    _providedRoles_InterfaceProvidingEntity.forEach(_function);
    this.generateSettingsFile(".jsdtscope");
    this.generateSettingsFile("org.eclipse.jdt.core.prefs");
    this.generateSettingsFile("org.eclipse.wst.common.component");
    this.generateSettingsFile("org.eclipse.wst.common.project.facet.core.xml");
    this.generateSettingsFile("org.eclipse.wst.jsdt.ui.superType.container");
    this.generateSettingsFile("org.eclipse.wst.jsdt.ui.superType.name");
    Classpath _instance_2 = this.injector.<Classpath>getInstance(Classpath.class);
    ServletClasspath<org.palladiosimulator.pcm.system.System> _servletClasspath = new ServletClasspath<org.palladiosimulator.pcm.system.System>(this.entity);
    GeneratedFile<IClasspath> _createFor_2 = _instance_2.createFor(_servletClasspath);
    this.generatedFiles.add(_createFor_2);
    ServletDeploymentDescriptor _instance_3 = this.injector.<ServletDeploymentDescriptor>getInstance(ServletDeploymentDescriptor.class);
    this.generatedFiles.add(_instance_3);
    List<FrameworkFile> files = this.fileProvider.getFrameworkFiles();
    final Consumer<FrameworkFile> _function_1 = (FrameworkFile it) -> {
      File _inputFile = it.getInputFile();
      boolean _equals = Objects.equal(_inputFile, null);
      if (_equals) {
        CopiedFile _instance_4 = this.injector.<CopiedFile>getInstance(CopiedFile.class);
        String _path = it.getPath();
        String _plus = ("WebContent/" + _path);
        URL _inputUrl = it.getInputUrl();
        CopiedFile _build = _instance_4.build(_plus, _inputUrl);
        this.copiedFiles.add(_build);
      } else {
        CopiedFile _instance_5 = this.injector.<CopiedFile>getInstance(CopiedFile.class);
        String _path_1 = it.getPath();
        String _plus_1 = ("WebContent/" + _path_1);
        File _inputFile_1 = it.getInputFile();
        CopiedFile _build_1 = _instance_5.build(_plus_1, _inputFile_1);
        this.copiedFiles.add(_build_1);
      }
    };
    files.forEach(_function_1);
    JClass _instance_4 = this.injector.<JClass>getInstance(JClass.class);
    ServletSystemMain _servletSystemMain = new ServletSystemMain(this.entity);
    GeneratedFile<IJClass> _createFor_3 = _instance_4.createFor(_servletSystemMain);
    this.generatedFiles.add(_createFor_3);
  }
}
