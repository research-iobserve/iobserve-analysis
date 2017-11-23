/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.planning.environment;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.analysis.model.PalladioResourceRepository;
import org.iobserve.analysis.model.PalladioResourceRepositoryImpl;
import org.iobserve.planning.environment.uriconverter.PrefixConverter;
import org.iobserve.planning.environment.uriconverter.URIConverterHandler;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractPCMWorkflowRunConfiguration;
import org.palladiosimulator.pcm.allocation.util.AllocationResourceFactoryImpl;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudprofilePackage;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.util.CloudprofileResourceFactoryImpl;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.util.ResourceenvironmentcloudResourceFactoryImpl;
import org.palladiosimulator.pcm.core.composition.util.CompositionResourceFactoryImpl;
import org.palladiosimulator.pcm.core.entity.util.EntityResourceFactoryImpl;
import org.palladiosimulator.pcm.core.util.CoreResourceFactoryImpl;
import org.palladiosimulator.pcm.parameter.util.ParameterResourceFactoryImpl;
import org.palladiosimulator.pcm.protocol.util.ProtocolResourceFactoryImpl;
import org.palladiosimulator.pcm.qosannotations.qos_performance.util.QosPerformanceResourceFactoryImpl;
import org.palladiosimulator.pcm.qosannotations.qos_reliability.util.QosReliabilityResourceFactoryImpl;
import org.palladiosimulator.pcm.qosannotations.util.QosannotationsResourceFactoryImpl;
import org.palladiosimulator.pcm.reliability.util.ReliabilityResourceFactoryImpl;
import org.palladiosimulator.pcm.repository.util.RepositoryResourceFactoryImpl;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.util.ResourcetypeResourceFactoryImpl;
import org.palladiosimulator.pcm.seff.seff_performance.util.SeffPerformanceResourceFactoryImpl;
import org.palladiosimulator.pcm.seff.seff_reliability.util.SeffReliabilityResourceFactoryImpl;
import org.palladiosimulator.pcm.seff.util.SeffResourceFactoryImpl;
import org.palladiosimulator.pcm.subsystem.util.SubsystemResourceFactoryImpl;
import org.palladiosimulator.pcm.system.util.SystemResourceFactoryImpl;
import org.palladiosimulator.pcm.usagemodel.util.UsagemodelResourceFactoryImpl;
import org.palladiosimulator.pcm.util.PcmResourceFactoryImpl;

import com.google.common.base.Throwables;

import de.uka.ipd.sdq.pcm.cost.costPackage;
import de.uka.ipd.sdq.pcm.cost.util.costResourceFactoryImpl;
import de.uka.ipd.sdq.pcm.designdecision.designdecisionPackage;
import de.uka.ipd.sdq.pcm.designdecision.gdof.gdofPackage;
import de.uka.ipd.sdq.pcm.designdecision.gdof.util.gdofResourceFactoryImpl;
import de.uka.ipd.sdq.pcm.designdecision.specific.specificPackage;
import de.uka.ipd.sdq.pcm.designdecision.specific.util.specificResourceFactoryImpl;
import de.uka.ipd.sdq.pcm.designdecision.util.designdecisionResourceFactoryImpl;

/**
 * As Palladio is executed outside an Eclipse environment, we'll have to manually provide certain
 * Eclipse-specific environment settings, that would otherwise be automatically loaded and
 * configured.
 * <p>
 * Note: By definition, the things done here are hacky, as we recreate an Eclipse similar
 * environment for Palladio to work. Any changes that are "less hacky" are very welcome!
 *
 * @author Fabian Keller
 * @author Tobias Pöppke
 */
public enum PalladioEclipseEnvironment {
    INSTANCE;

    private static final Logger LOG = LogManager.getLogger(PalladioEclipseEnvironment.class.getName());

    private boolean isSetup = false;
    private URIConverterHandler uriConverter;

    PalladioEclipseEnvironment() {
    }

    /**
     * Sets up the Palladio Eclipse environment by registering certain Palladio and Eclipse
     * functionality in the right places. This method is thread-safe and will only perform the setup
     * once, even when called multiple times.
     */
    public synchronized void setup() {
        if (this.isSetup) {
            return;
        }
        this.isSetup = true;

        PalladioEclipseEnvironment.LOG.info("Starting to set up the Palladio Eclipse environment.");
        this.registerFactories();
        this.registerUriConverter();
        this.registerPathmapConverters();
        this.registerPalladioResourceRepository();
        PalladioEclipseEnvironment.LOG.info("Finished setting up the Palladio Eclipse environment.");
    }

    /**
     * Determines whether setup of the environment has already been executed.
     */
    public boolean isSetup() {
        return this.isSetup;
    }

    /**
     * Manually register all Palladio factories for EMF.
     */
    private void registerFactories() {
        for (final EPackage ePackage : AbstractPCMWorkflowRunConfiguration.PCM_EPACKAGES) {
            Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(ePackage.getNsURI(), ePackage);
        }
        PalladioEclipseEnvironment.LOG.info("Initialized EMF factories");

        // register factories:
        // http://wiki.eclipse.org/EMF/FAQ#How_do_I_use_EMF_in_standalone_applications_.28such_as_an_ordinary_main.29.3F
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("pcm", new PcmResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("core", new CoreResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("entity", new EntityResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("composition",
                new CompositionResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("usagemodel",
                new UsagemodelResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("repository",
                new RepositoryResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("resourcetype",
                new ResourcetypeResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("protocol",
                new ProtocolResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("parameter",
                new ParameterResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("reliability",
                new ReliabilityResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("seff", new SeffResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("seffperformance",
                new SeffPerformanceResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("seffreliability",
                new SeffReliabilityResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("qosannotations",
                new QosannotationsResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("qosperformance",
                new QosPerformanceResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("qosreliability",
                new QosReliabilityResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("system", new SystemResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("resourceenvironment",
                new ResourceenvironmentcloudResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("allocation",
                new AllocationResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("subsystem",
                new SubsystemResourceFactoryImpl());

        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("designdecision",
                new designdecisionResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("specific",
                new specificResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("gdof", new gdofResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("cost", new costResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("cloudprofile",
                new CloudprofileResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

        PalladioEclipseEnvironment.LOG.info("Initialized resource factories");

        designdecisionPackage.eINSTANCE.eClass();
        specificPackage.eINSTANCE.eClass();
        gdofPackage.eINSTANCE.eClass();
        costPackage.eINSTANCE.eClass();
        CloudprofilePackage.eINSTANCE.eClass();

        PalladioEclipseEnvironment.LOG.info("Registered package instances");
    }

    /**
     * Intercept the static global Eclipse URI converter by an interceptor.
     */
    private void registerUriConverter() {
        // create converter
        this.uriConverter = new URIConverterHandler(URIConverter.INSTANCE);

        // register globally
        try {
            final Field field = ExtensibleURIConverterImpl.class.getField("INSTANCE");
            field.setAccessible(true);
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, this.uriConverter);
            PalladioEclipseEnvironment.LOG.info("Registered custom URIConverter");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            PalladioEclipseEnvironment.LOG.warn("Could not set the URIConverter globally.");
            PalladioEclipseEnvironment.LOG.warn(Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * Returns the intercepted URI converter. You may add your own interceptors.
     */
    public URIConverterHandler getUriConverter() {
        assert this.isSetup();
        return this.uriConverter;
    }

    private void registerPathmapConverters() {
        assert this.uriConverter != null;
        this.uriConverter.addInterceptor(new PrefixConverter("pathmap://PCM_MODELS/",
                this.getClass().getResource("/defaultModels/").toString()));
    }

    private void registerPalladioResourceRepository() {
        final ResourceRepository resources = new IPCMResourceSetPartitionFactory.DefaultFactory().create()
                .getResourceTypeRepository();
        assert resources != null;
        PalladioResourceRepository.INSTANCE.initResources(new PalladioResourceRepositoryImpl(resources));
    }

}
