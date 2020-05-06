package org.palladiosimulator.protocom.traverse.jsestub;

import org.palladiosimulator.protocom.traverse.framework.CommonConfigurationModule;
import org.palladiosimulator.protocom.traverse.framework.repository.XBasicComponent;
import org.palladiosimulator.protocom.traverse.framework.repository.XCollectionDataType;
import org.palladiosimulator.protocom.traverse.framework.repository.XCompositeComponent;
import org.palladiosimulator.protocom.traverse.framework.repository.XCompositeDataType;
import org.palladiosimulator.protocom.traverse.framework.repository.XInfrastructureInterface;
import org.palladiosimulator.protocom.traverse.framework.repository.XOperationInterface;
import org.palladiosimulator.protocom.traverse.framework.system.XSystem;
import org.palladiosimulator.protocom.traverse.jsestub.repository.JseStubBasicComponent;
import org.palladiosimulator.protocom.traverse.jsestub.repository.JseStubCollectionDataType;
import org.palladiosimulator.protocom.traverse.jsestub.repository.JseStubCompositeComponent;
import org.palladiosimulator.protocom.traverse.jsestub.repository.JseStubCompositeDataType;
import org.palladiosimulator.protocom.traverse.jsestub.repository.JseStubInfrastructureInterface;
import org.palladiosimulator.protocom.traverse.jsestub.repository.JseStubOperationInterface;
import org.palladiosimulator.protocom.traverse.jsestub.system.JseStubSystem;

/**
 * Google Guice binding for Java Standard Edition Protocom Stubs. This class is not using Xtend as
 * its superclass it not working properly with it.
 * 
 * @author Sebastian Lehrig
 */
public class JseStubConfigurationModule extends CommonConfigurationModule {

    @Override
    protected void configure() {
        super.configure();

        // Repository
        bind(XBasicComponent.class).to(JseStubBasicComponent.class);
        bind(XCompositeComponent.class).to(JseStubCompositeComponent.class);
        bind(XOperationInterface.class).to(JseStubOperationInterface.class);
        bind(XInfrastructureInterface.class).to(JseStubInfrastructureInterface.class);
        bind(XBasicComponent.class).to(JseStubBasicComponent.class);
        bind(XCollectionDataType.class).to(JseStubCollectionDataType.class);
        bind(XCompositeDataType.class).to(JseStubCompositeDataType.class);

        // System
        bind(XSystem.class).to(JseStubSystem.class);
    }

}
