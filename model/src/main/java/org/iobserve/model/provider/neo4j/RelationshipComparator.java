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
package org.iobserve.model.provider.neo4j;

import java.util.Comparator;

import org.neo4j.graphdb.Relationship;

/**
 * Comparator for comparing two relationships by their position properties.
 *
 * @author Lars Bluemke
 *
 */
public class RelationshipComparator implements Comparator<Relationship> {

    @Override
    public int compare(final Relationship o1, final Relationship o2) {
        return (int) o1.getProperty(ModelProvider.REF_POS) - (int) o2.getProperty(ModelProvider.REF_POS);
    }

}
