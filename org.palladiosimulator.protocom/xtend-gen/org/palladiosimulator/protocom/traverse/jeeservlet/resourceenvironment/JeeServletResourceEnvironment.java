package org.palladiosimulator.protocom.traverse.jeeservlet.resourceenvironment;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceEnvironmentAdapter;
import org.palladiosimulator.protocom.tech.servlet.resourceenvironment.ServletResourceEnvironment;
import org.palladiosimulator.protocom.traverse.framework.resourceenvironment.XResourceEnvironment;

@SuppressWarnings("all")
public class JeeServletResourceEnvironment extends XResourceEnvironment {
  @Override
  protected void generate() {
    JClass _instance = this.injector.<JClass>getInstance(JClass.class);
    ResourceEnvironmentAdapter _resourceEnvironmentAdapter = new ResourceEnvironmentAdapter(this.entity);
    ServletResourceEnvironment _servletResourceEnvironment = new ServletResourceEnvironment(_resourceEnvironmentAdapter);
    GeneratedFile<IJClass> _createFor = _instance.createFor(_servletResourceEnvironment);
    this.generatedFiles.add(_createFor);
  }
}
