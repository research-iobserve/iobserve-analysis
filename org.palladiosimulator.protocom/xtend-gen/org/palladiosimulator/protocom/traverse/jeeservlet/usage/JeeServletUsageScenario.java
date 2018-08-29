package org.palladiosimulator.protocom.traverse.jeeservlet.usage;

import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.impl.JClass;
import org.palladiosimulator.protocom.lang.xml.ITestPlan;
import org.palladiosimulator.protocom.lang.xml.impl.TestPlan;
import org.palladiosimulator.protocom.model.usage.UsageScenarioAdapter;
import org.palladiosimulator.protocom.tech.servlet.usage.ServletTestPlan;
import org.palladiosimulator.protocom.tech.servlet.usage.ServletUsageScenario;
import org.palladiosimulator.protocom.traverse.framework.usage.XUsageScenario;

@SuppressWarnings("all")
public class JeeServletUsageScenario extends XUsageScenario {
  @Override
  protected void generate() {
    final UsageScenarioAdapter adapter = new UsageScenarioAdapter(this.entity);
    JClass _instance = this.injector.<JClass>getInstance(JClass.class);
    ServletUsageScenario _servletUsageScenario = new ServletUsageScenario(adapter, this.entity);
    GeneratedFile<IJClass> _createFor = _instance.createFor(_servletUsageScenario);
    this.generatedFiles.add(_createFor);
    TestPlan _instance_1 = this.injector.<TestPlan>getInstance(TestPlan.class);
    ServletTestPlan _servletTestPlan = new ServletTestPlan(adapter, this.entity);
    GeneratedFile<ITestPlan> _createFor_1 = _instance_1.createFor(_servletTestPlan);
    this.generatedFiles.add(_createFor_1);
  }
}
