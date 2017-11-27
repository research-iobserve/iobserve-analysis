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
package org.iobserve.planning.data;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;

/**
 * This class transports all required data between the planning filter stages.
 *
 * @author Philipp Weimann
 * @author Tobias Pöppke
 */
public class PlanningData {

    // TODO configuration option
    public static final int POSSIBLE_REPLICAS_OFFSET = 10;
    public static final int POSSIBLE_REPLICAS_FACTOR = 1;

    private AdaptationData adaptationData;

    private URI perOpteryxDir;
    private URI lqnsDir;

    private URI originalModelDir;
    private URI processedModelDir;

    public AdaptationData getAdaptationData() {
        return this.adaptationData;
    }

    public void setAdaptationData(final AdaptationData adaptationData) {
        this.adaptationData = adaptationData;
    }

    public URI getPerOpteryxDir() {
        return this.perOpteryxDir;
    }

    public void setPerOpteryxDir(final URI perOpteryxDir) {
        this.perOpteryxDir = perOpteryxDir;
    }

    public URI getProcessedModelDir() {
        return this.processedModelDir;
    }

    public void setProcessedModelDir(final URI processedModelDir) {
        this.processedModelDir = processedModelDir;
    }

    public URI getOriginalModelDir() {
        return this.originalModelDir;
    }

    public void setOriginalModelDir(final URI originalModelDir) {
        this.originalModelDir = originalModelDir;
    }

    public URI getLqnsDir() {
        return this.lqnsDir;
    }

    public void setLqnsDir(final URI lqnsDir) {
        this.lqnsDir = lqnsDir;
    }

}
