/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import teetime.framework.test.StageTester;

import org.iobserve.analysis.deployment.DeployPCMMapper;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.factory.ResourceEnvironmentModelFactory;
import org.iobserve.model.test.data.CorrespondenceModelDataFactory;
import org.iobserve.model.test.data.ImplementationLevelDataFactory;
import org.iobserve.model.test.data.ModelLevelData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Reiner Jung
 *
 */
@RunWith(PowerMockRunner.class) // NOCS
// write all final classes here
@PrepareForTest(ResourceEnvironmentModelFactory.class)
public class DeployPCMMapperTest {

    @Mock
    private static ICorrespondence mockedCorrespondence;

    private static List<IDeployedEvent> inputEvents = new ArrayList<>();

    private static List<PCMDeployedEvent> pcmDeployedEvents = new ArrayList<>();

    /**
     * Setup configuration.
     */
    @BeforeClass
    public static void setUp() {
        /** mock for correspondence model */
        DeployPCMMapperTest.mockedCorrespondence = Mockito.mock(ICorrespondence.class);

        DeployPCMMapperTest.inputEvents.add(ImplementationLevelDataFactory.SERVLET_DEPLOYED_EVENT);
        DeployPCMMapperTest.inputEvents.add(ImplementationLevelDataFactory.EJB_DEPLOYED_EVENT);

        DeployPCMMapperTest.pcmDeployedEvents.add(ModelLevelData.PCM_DEPLOYED_EVENT);
        DeployPCMMapperTest.pcmDeployedEvents.add(ModelLevelData.PCM_DEPLOYED_EVENT);
    }

    /**
     * Define the test situation in which the needed {@link ResourceContainer} does not exist in the
     * given {@link ResourceEnvironment} model.
     */
    @Before
    public void stubMocksNoServletResourceContainer() {
        Mockito.when(DeployPCMMapperTest.mockedCorrespondence.getCorrespondent(ImplementationLevelDataFactory.CONTEXT))
                .thenReturn(Optional.of(CorrespondenceModelDataFactory.CORRESPONDENT));
    }

    /**
     * Test method for
     * {@link org.iobserve.analysis.deployment.DeployPCMMapper#execute(org.iobserve.common.record.IDeployed)}.
     */
    @Test
    public void testExecuteIDeployed() {
        final DeployPCMMapper mapper = new DeployPCMMapper(DeployPCMMapperTest.mockedCorrespondence);

        final List<PCMDeployedEvent> deploymentEvents = new ArrayList<>();

        StageTester.test(mapper).and().send(DeployPCMMapperTest.inputEvents).to(mapper.getInputPort()).and()
                .receive(deploymentEvents).from(mapper.getOutputPort()).start();

        Assert.assertEquals("Number of events differ.", deploymentEvents.size(),
                DeployPCMMapperTest.pcmDeployedEvents.size());
        Assert.assertEquals("Different events.", deploymentEvents.get(0).getService(),
                ModelLevelData.PCM_DEPLOYED_EVENT.getService());
    }

}
