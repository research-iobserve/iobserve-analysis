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
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ModelGenerationFilter;

/**
 * factory class for creating a filter for CoCoME data
 *
 * @author Christoph Dornieden
 *
 */
public class CoCoMEModelGenerationFilterFactory implements IModelGenerationFilterFactory {

    @Override
    public ModelGenerationFilter createFilter() {
        final ModelGenerationFilter modelGenerationFilter = new ModelGenerationFilter(false);
        modelGenerationFilter.addFilterRule( // allow all operations of the package
                                             // org.cocome.cloud.logic.webservice.cashdeskline.cashdesk
                                             // except init operations
                "(p\\w* )(\\w*\\.)*\\w* org\\.cocome\\.cloud\\.logic\\.webservice\\.cashdeskline\\.cashdesk\\.(\\w*\\.)*\\w*\\(.*");

        modelGenerationFilter.addFilterRule(// allow all operations of the package
                                            // except init operations
                                            // org.cocome.cloud.logic.webservice.store
                "(p\\w* )(\\w*\\.)*\\w* org\\.cocome\\.cloud\\.logic\\.webservice\\.store\\.(\\w*\\.)*\\w*\\(.*");

        modelGenerationFilter.addFilterRule(// allow all operations of the package
                // except init operations
                // org.cocome.cloud.logic.webservice.store
                ".*queryStore.(\\w*\\.)*\\w*\\(.*");

        return modelGenerationFilter;
    }

}
