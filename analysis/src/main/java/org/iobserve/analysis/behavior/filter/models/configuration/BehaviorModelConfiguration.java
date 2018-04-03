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
package org.iobserve.analysis.behavior.filter.models.configuration;

import org.iobserve.analysis.behavior.IVectorQuantizationClustering;
import org.iobserve.analysis.behavior.filter.models.configuration.examples.FirstCallInformationCodeStrategy;
import org.iobserve.analysis.behavior.filter.xmeans.XMeansClustering;

import weka.core.ManhattanDistance;

/**
 * Configuration for the behavior model filter.
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModelConfiguration {

    private String behaviorModelNamePrefix;

    // table generation configuration
    private EntryCallFilterRules modelGenerationFilter;

    private IRepresentativeStrategy representativeStrategy;

    private ISignatureCreationStrategy signatureCreationStrategy;

    // clustering configuration
    private IVectorQuantizationClustering clustering;

    private String visualizationUrl;

    // empty transitions?
    private boolean keepEmptyTransitions;

    /**
     * Default constructor.
     */
    public BehaviorModelConfiguration() {
        this.behaviorModelNamePrefix = "BehaviorModel";
        this.visualizationUrl = "localhost:8080";
        this.keepEmptyTransitions = true;
        this.modelGenerationFilter = new EntryCallFilterRules(false).addFilterRule(".*");
        this.representativeStrategy = new FirstCallInformationCodeStrategy();
        this.signatureCreationStrategy = new GetLastXSignatureStrategy(Integer.MAX_VALUE);
        this.clustering = new XMeansClustering(1, 1, new ManhattanDistance());
    }

    public EntryCallFilterRules getModelGenerationFilter() {
        return this.modelGenerationFilter;
    }

    public IRepresentativeStrategy getRepresentativeStrategy() {
        return this.representativeStrategy;
    }

    public IVectorQuantizationClustering getClustering() {
        return this.clustering;
    }

    public ISignatureCreationStrategy getSignatureCreationStrategy() {
        return this.signatureCreationStrategy;
    }

    public String getNamePrefix() {
        return this.behaviorModelNamePrefix;

    }

    public String getBehaviorModelNamePrefix() {
        return this.behaviorModelNamePrefix;
    }

    public String getVisualizationUrl() {
        return this.visualizationUrl;
    }

    /**
     * Configuration value indicating whether empty transaction should be kept.
     *
     * @return returns true when empty transaction should be kept
     */
    public boolean isKeepEmptyTransitions() {
        return this.keepEmptyTransitions;
    }

    public void setBehaviorModelNamePrefix(final String behaviorModelNamePrefix) {
        this.behaviorModelNamePrefix = behaviorModelNamePrefix;
    }

    public void setModelGenerationFilter(final EntryCallFilterRules modelGenerationFilter) {
        this.modelGenerationFilter = modelGenerationFilter;
    }

    public void setRepresentativeStrategy(final IRepresentativeStrategy representativeStrategy) {
        this.representativeStrategy = representativeStrategy;
    }

    public void setSignatureCreationStrategy(final ISignatureCreationStrategy signatureCreationStrategy) {
        this.signatureCreationStrategy = signatureCreationStrategy;
    }

    public void setClustering(final IVectorQuantizationClustering clustering) {
        this.clustering = clustering;
    }

    public void setVisualizationUrl(final String visualizationUrl) {
        this.visualizationUrl = visualizationUrl;
    }

    /**
     * Set whether empty transitions should be kept.
     *
     * @param keepEmptyTransitions
     *            the keepEmptyTransitions to set
     */
    public void setKeepEmptyTransitions(final boolean keepEmptyTransitions) {
        this.keepEmptyTransitions = keepEmptyTransitions;
    }

}
