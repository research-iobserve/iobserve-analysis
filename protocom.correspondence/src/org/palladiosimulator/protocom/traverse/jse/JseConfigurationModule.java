package org.palladiosimulator.protocom.traverse.jse;

import org.palladiosimulator.protocom.traverse.framework.CommonConfigurationModule;
import org.palladiosimulator.protocom.traverse.framework.allocation.XAllocation;
import org.palladiosimulator.protocom.traverse.framework.repository.XBasicComponent;
import org.palladiosimulator.protocom.traverse.framework.repository.XCompositeComponent;
import org.palladiosimulator.protocom.traverse.framework.repository.XInfrastructureInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XOperationInterface;
import org.palladiosimulator.protocom.traverse.framework.resourceenvironment.XResourceEnvironment;
import org.palladiosimulator.protocom.traverse.framework.system.XSystem;
import org.palladiosimulator.protocom.traverse.framework.usage.XUsageScenario;
import org.palladiosimulator.protocom.traverse.jse.allocation.JseAllocation;
import org.palladiosimulator.protocom.traverse.jse.repository.JseBasicComponent;
import org.palladiosimulator.protocom.traverse.jse.repository.JseCompositeComponent;
import org.palladiosimulator.protocom.traverse.jse.repository.JseInfrastructureInterface;
import org.palladiosimulator.protocom.traverse.jse.repository.JseOperationInterface;
import org.palladiosimulator.protocom.traverse.jse.resourceenvironment.JseResourceEnvironment;
import org.palladiosimulator.protocom.traverse.jse.system.JseSystem;
import org.palladiosimulator.protocom.traverse.jse.usage.JseUsageScenario;

/**
 * Google Guice binding for Java Standard Edition Protocom. This class is not using Xtend as its
 * superclass it not working properly with it.
 * 
 * @author Thomas Zolynski
 */
public class JseConfigurationModule extends CommonConfigurationModule {

    @Override
    protected void configure() {
        super.configure();

        bind(XBasicComponent.class).to(JseBasicComponent.class);
        bind(XCompositeComponent.class).to(JseCompositeComponent.class);
        bind(XOperationInterface.class).to(JseOperationInterface.class);
        bind(XInfrastructureInterface.class).to(JseInfrastructureInterface.class);
        bind(XSystem.class).to(JseSystem.class);
        bind(XAllocation.class).to(JseAllocation.class);
        bind(XResourceEnvironment.class).to(JseResourceEnvironment.class);
        bind(XUsageScenario.class).to(JseUsageScenario.class);
    }
}
