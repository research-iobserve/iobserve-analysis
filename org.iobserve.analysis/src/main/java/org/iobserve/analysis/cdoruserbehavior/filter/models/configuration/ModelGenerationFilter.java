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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christoph Dornieden
 *
 */
public class ModelGenerationFilter {
    private final List<String> filterList;
    private final boolean blackListMode;

    /**
     * constructor
     *
     * @param blackListMode
     *            blackListMode
     */
    public ModelGenerationFilter(boolean blackListMode) {
        this.filterList = new ArrayList<>();
        this.blackListMode = blackListMode;
    }

    /**
     * constructor
     *
     * @param filterList
     *            filterList
     * @param blackListMode
     *            blackListMode
     */
    public ModelGenerationFilter(final List<String> filterList, final boolean blackListMode) {
        this.filterList = filterList;
        this.blackListMode = blackListMode;
    }

    /**
     * Checks wether a signature is allowed or not
     *
     * @param signature
     *            signature
     * @return true if signature is allowed, false else
     */
    public boolean isAllowed(String signature) {
        boolean isAllowed = true;

        for (final String filterRule : this.filterList) {

            final boolean isMatch = signature.matches(filterRule);
            isAllowed = isAllowed && this.blackListMode ? !isMatch : isMatch;
        }
        return isAllowed;
    }

    /**
     * add filter rule
     *
     * @param regex
     *            regex matching signatures
     * @return true if regex could be added, false else
     */
    public boolean addFilterRule(String regex) {
        return this.filterList.add(regex);
    }

    /**
     * getter
     *
     * @return the filterList
     */
    public List<String> getFilterList() {
        return this.filterList;
    }

    /**
     * getter
     *
     * @return the blackListMode
     */
    public boolean isBlackListMode() {
        return this.blackListMode;
    }

}
