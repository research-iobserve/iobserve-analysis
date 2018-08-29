package org.palladiosimulator.protocom.traverse.jeeservlet.repository;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.impl.JInterface;
import org.palladiosimulator.protocom.tech.servlet.repository.ServletInfrastructureInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XInfrastructureInterface;

/**
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class JeeServletInfrastructureInterface extends XInfrastructureInterface {
  @Override
  protected void generate() {
    JInterface _instance = this.injector.<JInterface>getInstance(JInterface.class);
    ServletInfrastructureInterface _servletInfrastructureInterface = new ServletInfrastructureInterface(this.entity);
    GeneratedFile<IJInterface> _createFor = _instance.createFor(_servletInfrastructureInterface);
    this.generatedFiles.add(_createFor);
  }
}
