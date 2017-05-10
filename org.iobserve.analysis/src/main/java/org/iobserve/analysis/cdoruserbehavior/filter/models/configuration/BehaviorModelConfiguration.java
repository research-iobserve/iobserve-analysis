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

/**
 * configuration for the behavior model filter
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModelConfiguration {

    private final String behaviorModelNamePrefix;

    // table generation configuration
    private final ModelGenerationFilter modelGenerationFilter;

    private final IRepresentativeStrategy representativeStrategy;

    private final ISignatureCreationStrategy signatureCreationStrategy;

    // clustering configuration
    private final IClustering clustering;

    /**
     * constructor
     *
     * @param modelGenerationFilter
     *            modelGenerationFilter
     * @param representativeStrategy
     *            representative Strategy
     * @param clustering
     *            clustering
     */
    public BehaviorModelConfiguration(final String namePrefix, final ModelGenerationFilter modelGenerationFilter,
            final IRepresentativeStrategy representativeStrategy,
            final ISignatureCreationStrategy signatureCreationStrategy, final IClustering clustering) {
        super();
        this.behaviorModelNamePrefix = namePrefix;
        this.signatureCreationStrategy = signatureCreationStrategy;
        this.modelGenerationFilter = modelGenerationFilter;
        this.representativeStrategy = representativeStrategy;
        this.clustering = clustering;
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
     * @return nameprefix
     */
    public String getNamePrefix() {
        return this.behaviorModelNamePrefix;

    }

}
