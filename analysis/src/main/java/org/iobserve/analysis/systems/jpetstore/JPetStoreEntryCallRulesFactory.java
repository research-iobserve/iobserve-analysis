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
package org.iobserve.analysis.systems.jpetstore;

import org.iobserve.analysis.behavior.models.data.configuration.EntryCallFilterRules;
import org.iobserve.analysis.behavior.models.data.configuration.IModelGenerationFilterFactory;

/**
 * Factory for creating a filter for JPetStore call events.
 *
 * @author Christoph Dornieden
 *
 */
public final class JPetStoreEntryCallRulesFactory implements IModelGenerationFilterFactory {

    public JPetStoreEntryCallRulesFactory() {
        // empty constructor
    }

    /**
     * Create an entry call filter rule for the JPetStore.
     *
     * @return return the filter
     */
    @Override
    public EntryCallFilterRules createFilter() {
        final EntryCallFilterRules modelGenerationFilter;
        modelGenerationFilter = new EntryCallFilterRules(true);
        modelGenerationFilter.addFilterRule("(\\w*\\.)*images.*");
        modelGenerationFilter.addFilterRule("(\\w*\\.)*css.*");

        return modelGenerationFilter;
    }

}
