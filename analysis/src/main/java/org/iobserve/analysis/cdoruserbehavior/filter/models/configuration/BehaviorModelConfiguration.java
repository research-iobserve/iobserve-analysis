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

import org.iobserve.analysis.cdoruserbehavior.clustering.EAggregationType;
import org.iobserve.analysis.cdoruserbehavior.clustering.EOutputMode;
import org.iobserve.analysis.cdoruserbehavior.clustering.IVectorQuantizationClustering;
import org.iobserve.analysis.cdoruserbehavior.clustering.XMeansClustering;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.examples.DefaultStrategy;

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
    private EntryCallFilterRules modelGenerationFilter;

    private IRepresentativeStrategy representativeStrategy;

    private ISignatureCreationStrategy signatureCreationStrategy;

    // clustering configuration
    private IVectorQuantizationClustering clustering;

    private String visualizationUrl;

    // empty transitions?
    private boolean keepEmptyTransitions;

	private EAggregationType aggregationType;

	private EOutputMode outputMode;

    /**
     * default constructor
     */
    public BehaviorModelConfiguration() {
        this.behaviorModelNamePrefix = "BehaviorModel";
        this.visualizationUrl = "localhost:8080";
        this.keepEmptyTransitions = true;
        this.modelGenerationFilter = new EntryCallFilterRules(false).addFilterRule(".*");
        this.representativeStrategy = new DefaultStrategy();
        this.signatureCreationStrategy = new GetLastXSignatureStrategy(Integer.MAX_VALUE);
        this.clustering = new XMeansClustering(1, 1, new ManhattanDistance());
    }

    /**
     * getter
     *
     * @return the modelGenerationFilter
     */
    public EntryCallFilterRules getModelGenerationFilter() {
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
    public IVectorQuantizationClustering getClustering() {
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
    public void setModelGenerationFilter(final EntryCallFilterRules modelGenerationFilter) {
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
    public void setClustering(final IVectorQuantizationClustering clustering) {
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

    public void setAggregationType(EAggregationType aggregationType) {
    	this.aggregationType = aggregationType;
    }
    
	public EAggregationType getAggregationType() {
		return this.aggregationType;
	}

	public void setOutputMode(EOutputMode outputMode) {
		this.outputMode = outputMode;
	}
	
	public EOutputMode getOutputMode() {
		return this.outputMode;
	}

}
