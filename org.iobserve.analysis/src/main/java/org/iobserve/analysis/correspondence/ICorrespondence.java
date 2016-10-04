/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.correspondence;

import java.util.Optional;

/**
 * This is the interface for to query the correspondence model. Input is a pair of operation and
 * class signature which are then used to find the corresponding NamedElement (may be this can be
 * narrowed a bit, as NamedElement can be every element with a name and we only require Component,
 * Interface, Method etc.).
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public interface ICorrespondence {

    /**
     * Empty Correspondence.
     **/
    Optional<Correspondent> NULL_CORRESPONDENZ = Optional.empty();

    /**
     * Get the correspondent object which contains all the information needed to get the actual
     * model object from a model provider.
     *
     * @param classSig
     *            class signature
     * @param functionSig
     *            method signature
     * 
     * @return option on a {@link Correspondent} object.
     */
    Optional<Correspondent> getCorrespondent(String classSig, String functionSig);

    /**
     * Get the correspondent model component based on the class signature.
     * 
     * @param classSig
     *            full qualified java class name.
     * @return option of a correspondent in model
     */
    Optional<Correspondent> getCorrespondent(String classSig);
}
