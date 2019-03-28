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
package org.iobserve.service.privacy.violation.test.filter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import teetime.framework.test.StageTester;

import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.service.privacy.violation.filter.ProbeMapper;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * @author Marc Adolf
 *
 */
public class ProbeMapperTest {
    private ProbeMapper probeMapper;
    // private final int port = 5791;

    @Test
    public void receiveUninitializedDataTest() {
        this.probeMapper = new ProbeMapper(Mockito.mock(Neo4JModelResource.class), Mockito.mock(Neo4JModelResource.class),
                Mockito.mock(Neo4JModelResource.class), Mockito.mock(Neo4JModelResource.class),
                Mockito.mock(Neo4JModelResource.class));
        final ProbeManagementData data = new ProbeManagementData(null, null);

        final List<ProbeManagementData> input = new LinkedList<>();
        input.add(data);

        final List<AbstractTcpControlEvent> output = new LinkedList<>();

        StageTester.test(this.probeMapper).and().send(input).to(this.probeMapper.getInputPort()).receive(output)
                .from(this.probeMapper.getOutputPort()).start();

        Assert.assertTrue(output.isEmpty());

    }

    @Test
    public void receiveEmptyDataTest() {
        this.probeMapper = new ProbeMapper(Mockito.mock(Neo4JModelResource.class), Mockito.mock(Neo4JModelResource.class),
                Mockito.mock(Neo4JModelResource.class), Mockito.mock(Neo4JModelResource.class),
                Mockito.mock(Neo4JModelResource.class));
        final ProbeManagementData data = new ProbeManagementData(
                new HashMap<AllocationContext, Set<OperationSignature>>(),
                new HashMap<AllocationContext, Set<OperationSignature>>());

        final List<ProbeManagementData> input = new LinkedList<>();
        input.add(data);

        final List<AbstractTcpControlEvent> output = new LinkedList<>();

        StageTester.test(this.probeMapper).and().send(input).to(this.probeMapper.getInputPort()).receive(output)
                .from(this.probeMapper.getOutputPort()).start();

        Assert.assertTrue(output.isEmpty());

    }

    @Test
    public void receiveDataTest() {

        // TODO mocking fails to test the internal computation of this class, use real test data
        // instead

        // this.probeMapper = new ProbeMapper(Mockito.mock(ModelResource.class),
        // Mockito.mock(ModelResource.class),
        // Mockito.mock(ModelResource.class),Mockito.mock(ModelResource.class));
        // final String modifier = "public";
        // final String parameterString = "*";
        //
        // final String returnType1 = "String";
        // final String testMethod1 = "testMethod1";
        // final String componentIdentifier1 = "componentIdentifier1";
        // final String hostname1 = "hostname1";
        // final String ip1 = "111.111.111.111";
        //
        // final String returnType2 = "int";
        // final String testMethod2 = "testMethod2";
        // final String componentIdentifier2 = "componentIdentifier2";
        // final String hostname2 = "hostname2";
        // final String ip2 = "222.222.222.222";
        //
        // final DataType datatype1 = Mockito.mock(DataType.class);
        // final DataType datatype2 = Mockito.mock(DataType.class);
        //
        // final OperationSignature operationSignature1 = Mockito.mock(OperationSignature.class);
        // Mockito.when(operationSignature1.getReturnType__OperationSignature()).thenReturn(datatype1);
        // Mockito.when(datatype1.toString()).thenReturn(returnType1);
        // Mockito.when(operationSignature1.getEntityName()).thenReturn(testMethod1);
        //
        // final OperationSignature operationSignature2 = Mockito.mock(OperationSignature.class);
        // Mockito.when(operationSignature2.getReturnType__OperationSignature()).thenReturn(datatype2);
        // Mockito.when(datatype2.toString()).thenReturn(returnType2);
        // Mockito.when(operationSignature2.getEntityName()).thenReturn(testMethod2);
        //
        // final AllocationContext allocationContext1 = Mockito.mock(AllocationContext.class);
        // final ResourceContainer resourceContainer1 = Mockito.mock(ResourceContainer.class);
        // final AssemblyContext assemblyContext1 = Mockito.mock(AssemblyContext.class);
        // final RepositoryComponent repositoryComponent1 = Mockito.mock(RepositoryComponent.class);
        // final Repository repository1 = Mockito.mock(Repository.class);
        //
        // Mockito.when(allocationContext1.getEntityName()).thenReturn(hostname1);
        // Mockito.when(allocationContext1.getAssemblyContext_AllocationContext()).thenReturn(assemblyContext1);
        // Mockito.when(assemblyContext1.getEncapsulatedComponent__AssemblyContext()).thenReturn(repositoryComponent1);
        // Mockito.when(repositoryComponent1.getRepository__RepositoryComponent()).thenReturn(repository1);
        // Mockito.when(repository1.getEntityName()).thenReturn(componentIdentifier1);
        // Mockito.when(allocationContext1.getResourceContainer_AllocationContext()).thenReturn(resourceContainer1);
        // Mockito.when(resourceContainer1.getEntityName()).thenReturn(ip1);
        //
        // final AllocationContext allocationContext2 = Mockito.mock(AllocationContext.class);
        // final ResourceContainer resourceContainer2 = Mockito.mock(ResourceContainer.class);
        // final AssemblyContext assemblyContext2 = Mockito.mock(AssemblyContext.class);
        // final RepositoryComponent repositoryComponent2 = Mockito.mock(RepositoryComponent.class);
        // final Repository repository2 = Mockito.mock(Repository.class);
        //
        // Mockito.when(allocationContext2.getEntityName()).thenReturn(hostname2);
        // Mockito.when(allocationContext2.getAssemblyContext_AllocationContext()).thenReturn(assemblyContext2);
        // Mockito.when(assemblyContext2.getEncapsulatedComponent__AssemblyContext()).thenReturn(repositoryComponent2);
        // Mockito.when(repositoryComponent2.getRepository__RepositoryComponent()).thenReturn(repository2);
        // Mockito.when(repository2.getEntityName()).thenReturn(componentIdentifier2);
        // Mockito.when(allocationContext2.getResourceContainer_AllocationContext()).thenReturn(resourceContainer2);
        // Mockito.when(resourceContainer2.getEntityName()).thenReturn(ip2);
        //
        // final Map<AllocationContext, Set<OperationSignature>> methodsToActivate = new
        // LinkedHashMap<>();
        //
        // final Set<OperationSignature> methodSet1 = new LinkedHashSet<>();
        // methodSet1.add(operationSignature1);
        // methodsToActivate.put(allocationContext1, methodSet1);
        //
        // final Set<OperationSignature> methodSet2 = new LinkedHashSet<>();
        // methodSet2.add(operationSignature2);
        // methodsToActivate.put(allocationContext2, methodSet2);
        //
        // final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate = new
        // LinkedHashMap<>();
        // methodsToDeactivate.put(allocationContext1, methodSet1);
        //
        // final ProbeManagementData data1 = new ProbeManagementData(methodsToActivate, null);
        // final ProbeManagementData data2 = new ProbeManagementData(methodsToActivate,
        // methodsToDeactivate);
        //
        // final List<ProbeManagementData> input = new LinkedList<>();
        // input.add(data1);
        // input.add(data2);
        //
        // final List<AbstractTcpControlEvent> output = new LinkedList<>();
        // final TcpActivationControlEvent activationEvent1 = new TcpActivationControlEvent(ip1,
        // this.port, hostname1,
        // modifier + " " + returnType1 + " " + componentIdentifier1 + "." + testMethod1 + "(" +
        // parameterString
        // + ")");
        // final TcpActivationControlEvent activationEvent2 = new TcpActivationControlEvent(ip2,
        // this.port, hostname2,
        // modifier + " " + returnType2 + " " + componentIdentifier2 + "." + testMethod2 + "(" +
        // parameterString
        // + ")");
        // final TcpDeactivationControlEvent deactivationEvent1 = new
        // TcpDeactivationControlEvent(ip1, this.port,
        // hostname1, modifier + " " + returnType1 + " " + componentIdentifier1 + "." + testMethod1
        // + "("
        // + parameterString + ")");
        //
        // final List<AbstractTcpControlEvent> expectedOutput = new LinkedList<>();
        //
        // // 1. input
        // expectedOutput.add(activationEvent1);
        // expectedOutput.add(activationEvent2);
        // // 2.input
        // expectedOutput.add(activationEvent1);
        // expectedOutput.add(activationEvent2);
        // expectedOutput.add(deactivationEvent1);
        //
        // StageTester.test(this.probeMapper).and().send(input).to(this.probeMapper.getInputPort()).receive(output)
        // .from(this.probeMapper.getOutputPort()).start();
        //
        // Assert.assertTrue(this.areListsEqual(output, expectedOutput));

    }

    @Test
    public void receiveParameterDataTest() {

        // TODO mocking fails to test the internal computation of this class, use real test data
        // instead

        // this.probeMapper = new ProbeMapper(Mockito.mock(ModelResource.class),
        // Mockito.mock(ModelResource.class),
        // Mockito.mock(ModelResource.class),Mockito.mock(ModelResource.class));
        //
        // final String modifier = "public";
        // final String parameterString = "*";
        //
        // final String returnType = "String";
        // final String testMethod = "testMethod";
        // final String componentIdentifier = "componentIdentifier";
        // final String hostname = "hostname";
        // final String ip = "111.111.111.111";
        //
        // final List<String> whitelist = new LinkedList<>();
        // whitelist.add("111.111.111.111");
        // whitelist.add("222.222.222.222");
        // whitelist.add("333.333.333.333");
        //
        // final DataType datatype = Mockito.mock(DataType.class);
        //
        // final OperationSignature operationSignature = Mockito.mock(OperationSignature.class);
        // Mockito.when(operationSignature.getReturnType__OperationSignature()).thenReturn(datatype);
        // Mockito.when(datatype.toString()).thenReturn(returnType);
        // Mockito.when(operationSignature.getEntityName()).thenReturn(testMethod);
        //
        // final AllocationContext allocationContext = Mockito.mock(AllocationContext.class);
        // final ResourceContainer resourceContainer = Mockito.mock(ResourceContainer.class);
        // final AssemblyContext assemblyContext = Mockito.mock(AssemblyContext.class);
        // final RepositoryComponent repositoryComponent = Mockito.mock(RepositoryComponent.class);
        // final Repository repository = Mockito.mock(Repository.class);
        //
        // Mockito.when(allocationContext.getEntityName()).thenReturn(hostname);
        // Mockito.when(allocationContext.getAssemblyContext_AllocationContext()).thenReturn(assemblyContext);
        // Mockito.when(assemblyContext.getEncapsulatedComponent__AssemblyContext()).thenReturn(repositoryComponent);
        // Mockito.when(repositoryComponent.getRepository__RepositoryComponent()).thenReturn(repository);
        // Mockito.when(repository.getEntityName()).thenReturn(componentIdentifier);
        // Mockito.when(allocationContext.getResourceContainer_AllocationContext()).thenReturn(resourceContainer);
        // Mockito.when(resourceContainer.getEntityName()).thenReturn(ip);
        //
        // final Map<AllocationContext, Set<OperationSignature>> methodsToActivate = new
        // LinkedHashMap<>();
        //
        // final Set<OperationSignature> methodSet = new LinkedHashSet<>();
        // methodSet.add(operationSignature);
        // methodsToActivate.put(allocationContext, methodSet);
        //
        // final Map<String, List<String>> parameters = new HashMap<>();
        // parameters.put("whitelist", whitelist);
        //
        // final ProbeManagementData data = new ProbeManagementData(methodsToActivate, null);
        // data.setWhitelist(whitelist);
        //
        // final List<ProbeManagementData> input = new LinkedList<>();
        // input.add(data);
        //
        // final List<AbstractTcpControlEvent> output = new LinkedList<>();
        //
        // final List<AbstractTcpControlEvent> expectedOutput = new LinkedList<>();
        // expectedOutput.add(new TcpActivationParameterControlEvent(ip, this.port, hostname,
        // modifier + " " + returnType
        // + " " + componentIdentifier + "." + testMethod + "(" + parameterString + ")",
        // parameters));
        //
        // StageTester.test(this.probeMapper).and().send(input).to(this.probeMapper.getInputPort()).receive(output)
        // .from(this.probeMapper.getOutputPort()).start();
        //
        // Assert.assertTrue(this.areListsEqual(output, expectedOutput));

    }

    private Boolean areListsEqual(final List<AbstractTcpControlEvent> list1,
            final List<AbstractTcpControlEvent> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (!this.areEventsEqual(list1.get(i), list2.get(i))) {
                return false;
            }
        }

        return true;
    }

    private Boolean areEventsEqual(final AbstractTcpControlEvent event1, final AbstractTcpControlEvent event2) {
        if (event1.getHostname().equals(event2.getHostname()) && event1.getPattern().equals(event2.getPattern())
                && event1.getIp() == event2.getIp() && event1.getPort() == event2.getPort()) {
            return true;
        }

        return false;
    }
}
