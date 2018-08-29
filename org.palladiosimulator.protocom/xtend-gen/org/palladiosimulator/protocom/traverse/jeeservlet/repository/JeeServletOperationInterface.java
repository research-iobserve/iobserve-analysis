package org.palladiosimulator.protocom.traverse.jeeservlet.repository;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.impl.JInterface;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletOperationInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XOperationInterface;

@SuppressWarnings("all")
public class JeeServletOperationInterface extends XOperationInterface {
  @Override
  protected void generate() {
    JInterface _instance = this.injector.<JInterface>getInstance(JInterface.class);
    ServletOperationInterface _servletOperationInterface = new ServletOperationInterface(this.entity);
    GeneratedFile<IJInterface> _createFor = _instance.createFor(_servletOperationInterface);
    this.generatedFiles.add(_createFor);
  }
}
