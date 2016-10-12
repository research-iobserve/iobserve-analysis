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
package org.iobserve.analysis.model.correspondence;

import java.util.function.BiFunction;

import org.palladiosimulator.pcm.repository.OperationSignature;

import org.iobserve.analysis.protocom.PcmCorrespondentMethod;
import org.iobserve.analysis.protocom.PcmOperationSignature;
import org.iobserve.analysis.utils.IToBoolBiFunction;

/**
 * Function used to select {@link OperationSignature} instances based on the given
 * {@link PcmCorrespondentMethod}.
 *
 * @see BiFunction
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
@FunctionalInterface
public interface IOperationSignatureSelector extends IToBoolBiFunction<PcmCorrespondentMethod, PcmOperationSignature> {

    /**
     * Default select method to select the given operation signature.
     *
     * @param method
     *            method
     * @param operation
     *            operation
     * @return true if the given method correspond to the given operation
     */
    default boolean select(final PcmCorrespondentMethod method, final PcmOperationSignature operation) {
        return this.applyAsBool(method, operation);
    }
}
