package org.palladiosimulator.protocom.traverse.jse.resourceenvironment;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.tech.rmi.resourceenvironment.PojoResourceEnvironment;
import org.palladiosimulator.protocom.traverse.framework.resourceenvironment.XResourceEnvironment;

/**
 * Resource Environments for JSE are a config file used for calibration of active resources.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public class JseResourceEnvironment extends XResourceEnvironment {
  @Override
  public void generate() {
    JClass _instance = this.injector.<JClass>getInstance(JClass.class);
    PojoResourceEnvironment _pojoResourceEnvironment = new PojoResourceEnvironment(this.entity);
    GeneratedFile<IJClass> _createFor = _instance.createFor(_pojoResourceEnvironment);
    this.generatedFiles.add(_createFor);
  }
}
