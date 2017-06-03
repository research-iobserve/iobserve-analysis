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
package org.iobserve.analysis.cdoruserbehavior.filter.models.configuration;

import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.examples.JPetstoreStrategy;

import weka.core.ManhattanDistance;

/**
 * configuration for the behavior model filter
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModelConfiguration {

    private String behaviorModelNamePrefix;

    // table generation configuration
    private ModelGenerationFilter modelGenerationFilter;

    private IRepresentativeStrategy representativeStrategy;

    private ISignatureCreationStrategy signatureCreationStrategy;

    // clustering configuration
    private IClustering clustering;

    private String visualizationUrl;

    // empty transitions?
    private boolean keepEmptyTransitions = true;

    /**
     * default constructor
     */
    public BehaviorModelConfiguration() {
        this.behaviorModelNamePrefix = "BehaviorModel";
        this.visualizationUrl = null;
        this.modelGenerationFilter = new ModelGenerationFilter(false);
        this.modelGenerationFilter.addFilterRule(".*");
        this.representativeStrategy = new JPetstoreStrategy();
        this.signatureCreationStrategy = new GetLastXSignatureStrategy(Integer.MAX_VALUE);

        this.clustering = new XMeansClustering(1, 1, new ManhattanDistance());

    }

    /**
     * getter
     *
     * @return the modelGenerationFilter
     */
    public ModelGenerationFilter getModelGenerationFilter() {
        return this.modelGenerationFilter;
    }

    /**
     * getter
     *
     * @return the representativeStrategy
     */
    public IRepresentativeStrategy getRepresentativeStrategy() {
        return this.representativeStrategy;
    }

    /**
     * getter
     *
     * @return the clustering
     */
    public IClustering getClustering() {
        return this.clustering;
    }

    /**
     * getter
     *
     * @return the signatureCreationStrategy
     */
    public ISignatureCreationStrategy getSignatureCreationStrategy() {
        return this.signatureCreationStrategy;
    }

    /**
     * getter
     *
     * @return the nameprefix
     */
    public String getNamePrefix() {
        return this.behaviorModelNamePrefix;

    }

    /**
     * getter
     *
     * @return the behaviorModelNamePrefix
     */
    public String getBehaviorModelNamePrefix() {
        return this.behaviorModelNamePrefix;
    }

    /**
     * getter
     *
     * @return the uBMUrl
     */
    public String getVisualizationUrl() {
        return this.visualizationUrl;
    }

    /**
     * getter
     *
     * @return the keepEmptyTransitions
     */
    public boolean keepEmptyTransitions() {
        return this.keepEmptyTransitions;
    }

    /**
     * setter
     *
     * @param behaviorModelNamePrefix
     *            the behaviorModelNamePrefix to set
     */
    public void setBehaviorModelNamePrefix(final String behaviorModelNamePrefix) {
        this.behaviorModelNamePrefix = behaviorModelNamePrefix;
    }

    /**
     * setter
     *
     * @param modelGenerationFilter
     *            the modelGenerationFilter to set
     */
    public void setModelGenerationFilter(final ModelGenerationFilter modelGenerationFilter) {
        this.modelGenerationFilter = modelGenerationFilter;
    }

    /**
     * setter
     *
     * @param representativeStrategy
     *            the representativeStrategy to set
     */
    public void setRepresentativeStrategy(final IRepresentativeStrategy representativeStrategy) {
        this.representativeStrategy = representativeStrategy;
    }

    /**
     * setter
     *
     * @param signatureCreationStrategy
     *            the signatureCreationStrategy to set
     */
    public void setSignatureCreationStrategy(final ISignatureCreationStrategy signatureCreationStrategy) {
        this.signatureCreationStrategy = signatureCreationStrategy;
    }

    /**
     * setter
     *
     * @param clustering
     *            the clustering to set
     */
    public void setClustering(final IClustering clustering) {
        this.clustering = clustering;
    }

    /**
     * setter
     *
     * @param uBMUrl
     *            the uBMUrl to set
     */
    public void setVisualizationUrl(final String visualizationUrl) {
        this.visualizationUrl = visualizationUrl;
    }

    /**
     * setter
     *
     * @param keepEmptyTransitions
     *            the keepEmptyTransitions to set
     */
    public void setKeepEmptyTransitions(final boolean keepEmptyTransitions) {
        this.keepEmptyTransitions = keepEmptyTransitions;
    }

}
