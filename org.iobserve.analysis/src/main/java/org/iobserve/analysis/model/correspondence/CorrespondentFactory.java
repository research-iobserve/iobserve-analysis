/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.model.correspondence;

/**
 * Factory to create {@link Correspondent} objects.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class CorrespondentFactory {

    /**
     * Protect class from instantiation.
     */
    private CorrespondentFactory() {
        // nothing here
    }

    /**
     * Create new {@link Correspondent} object.
     * 
     * @param pcmEntityName
     *            entity name.
     * @param pcmEntityId
     *            entity id.
     * @param pcmOperationName
     *            operation name.
     * @param pcmOperationId
     *            operation id.
     * @return new correspondent object
     */
    public static Correspondent newInstance(final String pcmEntityName, final String pcmEntityId,
            final String pcmOperationName, final String pcmOperationId) {
        return new Correspondent(pcmEntityName, pcmEntityId, pcmOperationName, pcmOperationId);
    }
}
