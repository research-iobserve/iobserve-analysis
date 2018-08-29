package org.palladiosimulator.protocom.traverse.jse.usage;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.tech.rmi.system.PojoSystemMain;
import org.palladiosimulator.protocom.tech.rmi.usage.PojoUsageClosedScenarioThread;
import org.palladiosimulator.protocom.tech.rmi.usage.PojoUsageScenario;
import org.palladiosimulator.protocom.traverse.framework.usage.XUsageScenario;

@SuppressWarnings("all")
public class JseUsageScenario extends XUsageScenario {
  @Override
  public void generate() {
    JClass _instance = this.injector.<JClass>getInstance(JClass.class);
    PojoUsageScenario _pojoUsageScenario = new PojoUsageScenario(this.entity);
    GeneratedFile<IJClass> _createFor = _instance.createFor(_pojoUsageScenario);
    this.generatedFiles.add(_createFor);
    JClass _instance_1 = this.injector.<JClass>getInstance(JClass.class);
    PojoUsageClosedScenarioThread _pojoUsageClosedScenarioThread = new PojoUsageClosedScenarioThread(this.entity);
    GeneratedFile<IJClass> _createFor_1 = _instance_1.createFor(_pojoUsageClosedScenarioThread);
    this.generatedFiles.add(_createFor_1);
    JClass _instance_2 = this.injector.<JClass>getInstance(JClass.class);
    PojoSystemMain _pojoSystemMain = new PojoSystemMain(this.entity);
    GeneratedFile<IJClass> _createFor_2 = _instance_2.createFor(_pojoSystemMain);
    this.generatedFiles.add(_createFor_2);
  }
}
