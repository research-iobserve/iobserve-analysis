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
 * factory class for creating a filter for CoCoME data
 *
 * @author Christoph Dornieden
 *
 */
public class CoCoMEEntryCallRulesFactory implements IModelGenerationFilterFactory {

    @Override
    public EntryCallFilterRules createFilter() {
        final EntryCallFilterRules modelGenerationFilter = new EntryCallFilterRules(false);
        modelGenerationFilter.addFilterRule( // allow all operations of the package
                // org.cocome.cloud.logic.webservice.cashdeskline.cashdesk.*
                "p\\w+ (\\w*\\.)*\\w* (\\w*\\.)*(cashdeskservice|cashdesk)\\.(\\w*\\.)*\\w*\\(.*");

        // modelGenerationFilter.addFilterRule( // allow all operations of the package
        // // org.cocome.cloud.webservice.enterprise.*
        // "p\\w+ (\\w*\\.)*\\w* (\\w*\\.)*(enterprise)\\.(\\w*\\.)*\\w*\\(.*");
        // modelGenerationFilter.addFilterRule(// allow all operations of the package
        // // except init operations
        // // org.cocome.cloud.logic.webservice.store
        // ".*queryStore.(\\w*\\.)*\\w*\\(.*");

        return modelGenerationFilter;
    }

}
