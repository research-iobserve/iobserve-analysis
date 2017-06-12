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
package org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.examples;

import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.IModelGenerationFilterFactory;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.EntryCallFilterRules;

/**
 * factory for creating a filter for JPetStore call events
 *
 * @author Christoph Dornieden
 *
 */
public class JPetStoreEntryCallRulesFactory implements IModelGenerationFilterFactory {

    @Override
    public EntryCallFilterRules createFilter() {
        EntryCallFilterRules modelGenerationFilter;
        modelGenerationFilter = new EntryCallFilterRules(true);
        modelGenerationFilter.addFilterRule("(\\w*\\.)*images.*");
        modelGenerationFilter.addFilterRule("(\\w*\\.)*css.*");

        return modelGenerationFilter;
    }

}
