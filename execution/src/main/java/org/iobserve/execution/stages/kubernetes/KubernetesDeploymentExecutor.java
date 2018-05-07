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
package org.iobserve.execution.stages.kubernetes;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.execution.stages.IExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.DoneablePod;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodStatus;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.fabric8.openshift.api.model.Template;

/**
 * Deployment via kubernetes.
 *
 * @author Lars Bluemke
 *
 */
public class KubernetesDeploymentExecutor extends AbstractKubernetesExecutor
        implements IExecutor<DeployComponentAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesDeploymentExecutor.class);

    public KubernetesDeploymentExecutor(final String ip, final String port) {
        super(ip, port);
    }

    @Override
    public void execute(final DeployComponentAction deployComponentAction) {
        final String nodeName = deployComponentAction.getTargetAllocationContext()
                .getResourceContainer_AllocationContext().getEntityName();

        this.createPod(null, "podJsonFileName", "????", nodeName);
    }

    public void createPod(final String[] args, final String fileName, final String oAuthToken, final String nodeName,
            final String componentName) {
        if (args.length == 0) {
            System.out.println("Usage: podJsonFileName <token> <namespace>");
            return;
        }
        // final String fileName = args[0];
        // String namespace = null;
        // if (args.length > 2) {
        // namespace = args[2];
        // }

        final File file = new File(fileName);
        if (!file.exists() || !file.isFile()) {
            System.err.println("File does not exist: " + fileName);
            return;
        }

        final ConfigBuilder builder = new ConfigBuilder();
        // if (args.length > 1) {
        builder.withOauthToken(oAuthToken);
        // }

        //final Config config = builder.build();
        try (final KubernetesClient client = this.getConnection()/*new DefaultKubernetesClient(config)*/) {
            // if (namespace == null) {
            final String namespace = client.getNamespace();
            // }

            //////// Me trying to configure the pod programmatically /////////////
            // These attributes can usually be found in the pod config .yaml or .json file
            // The attributes here are hard coded for the JPetStore experiment for now

            final ObjectMeta metadata = new ObjectMeta();
            final Map<String, String> annotations = new HashMap<>();
            annotations.put("kompose.cmd", "kompose convert -v -f docker-compose.yml");
            annotations.put("kompose.version", "1.12.0 (0ab07be)");
            metadata.setAnnotations(annotations);
            metadata.setCreationTimestamp("null");
            final Map<String, String> labels = new HashMap<>();
            labels.put("io.kompose.service", componentName);
            metadata.setLabels(labels);
            metadata.setName(componentName); // componentName = account | catalog | frontend

            final PodSpec spec = new PodSpec();
            spec.setAdditionalProperty("replicas", "1");
            spec.setAdditionalProperty("strategy", "{}");

            final ObjectMeta metadata2 = new ObjectMeta();
            metadata2.setCreationTimestamp("null");
            final Map<String, String> labels2 = new HashMap<>();
            labels2.put("io.kompose.service", componentName);
            metadata2.setLabels(labels);
            final PodSpec spec2 = new PodSpec();
            spec2.setContainers(containers);

            final Template template = new Template();
            template.setMetadata(metadata2);
            final Map<String, String> nodeSelector = new HashMap<>();
            nodeSelector.put("node", nodeName);
            spec.setNodeSelector(nodeSelector);

            spec.set

            final PodStatus status = new PodStatus();
            // nothing to add here

            final Pod myPod = new Pod("v1", "Pod", metadata, spec, status);

            ///////////////////////////////////////////////////////////////////////

            final List<HasMetadata> resources = client.load(new FileInputStream(fileName)).get();
            if (resources.isEmpty()) {
                System.err.println("No resources loaded from file: " + fileName);
                return;
            }
            final HasMetadata resource = resources.get(0);
            if (resource instanceof Pod) {
                final Pod pod = (Pod) resource;
                System.out.println("Creating pod in namespace " + namespace);
                final NonNamespaceOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>> pods = client
                        .pods().inNamespace(namespace);
                final Pod result = pods.create(pod);
                System.out.println("Created pod " + result.getMetadata().getName());
            } else {
                System.err.println("Loaded resource is not a Pod! " + resource);
            }
        } catch (final KubernetesClientException e) {
            KubernetesDeploymentExecutor.logger.error(e.getMessage(), e);
        } catch (final Exception e) {
            KubernetesDeploymentExecutor.logger.error(e.getMessage(), e);
        }
    }

}
