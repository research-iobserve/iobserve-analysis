package org.palladiosimulator.protocom.traverse.jeeservlet;

import org.palladiosimulator.protocom.traverse.framework.CommonConfigurationModule;
import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation;
import org.palladiosimulator.protocom.traverse.framework.repository.XBasicComponent;
import org.palladiosimulator.protocom.traverse.framework.repository.XInfrastructureInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XOperationInterface;
import org.palladiosimulator.protocom.traverse.framework.resourceenvironment.XResourceEnvironment;
import org.palladiosimulator.protocom.traverse.framework.system.XSystem;
import org.palladiosimulator.protocom.traverse.framework.usage.XUsageScenario;
import org.palladiosimulator.protocom.traverse.jeeservlet.allocation.JeeServletAllocation;
import org.palladiosimulator.protocom.traverse.jeeservlet.repository.JeeServletBasicComponent;
import org.palladiosimulator.protocom.traverse.jeeservlet.repository.JeeServletInfrastructureInterface;
import org.palladiosimulator.protocom.traverse.jeeservlet.repository.JeeServletOperationInterface;
import org.palladiosimulator.protocom.traverse.jeeservlet.resourceenvironment.JeeServletResourceEnvironment;
import org.palladiosimulator.protocom.traverse.jeeservlet.system.JeeServletSystem;
import org.palladiosimulator.protocom.traverse.jeeservlet.usage.JeeServletUsageScenario;

/**
 *
 *
 * @author Christian Klaussner
 * @author Sebastian Lehrig
 */
public class JeeServletConfigurationModule extends CommonConfigurationModule {

    @Override
    protected void configure() {
        super.configure();

        bind(XBasicComponent.class).to(JeeServletBasicComponent.class);
        // TODO how to handle composite components?
        bind(XOperationInterface.class).to(JeeServletOperationInterface.class);
        bind(XInfrastructureInterface.class).to(JeeServletInfrastructureInterface.class);
        bind(XSystem.class).to(JeeServletSystem.class);
        bind(XAllocation.class).to(JeeServletAllocation.class);
        bind(XResourceEnvironment.class).to(JeeServletResourceEnvironment.class);
        bind(XUsageScenario.class).to(JeeServletUsageScenario.class);
    }
}
