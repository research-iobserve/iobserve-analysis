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

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 *
 * @author Lars Bluemke
 *
 */
public abstract class AbstractKubernetesExecutor {
    private final String ip;
    private final String port;

    public AbstractKubernetesExecutor(final String ip, final String port) {
        this.ip = ip;
        this.port = port;
    }

    public KubernetesClient getConnection() {
        final String masterUrl = "http://" + this.ip + ":" + this.port;
        final Config config = new ConfigBuilder().withMasterUrl(masterUrl).build();
        return new DefaultKubernetesClient(config);
    }

}
