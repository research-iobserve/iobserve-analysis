/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.correspondence;

import java.io.FileInputStream;

import javax.xml.bind.JAXB;

import org.iobserve.model.protocom.PcmMapping;
import org.iobserve.model.utils.StringUtils;

/**
 * Factory to create correspondence model instances according to RAC in paper <i>Run-time
 * Architecture Models for Dynamic Adaptation and Evolution of Cloud Applications</i>.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 * @version 1.0
 *
 */
public final class CorrespondeceModelFactory {

    /** singleton instance of factory. */
    public static final CorrespondeceModelFactory INSTANCE = new CorrespondeceModelFactory();

    /**
     * Check the name of the method against the operation signature. Return true if the method name
     * contains the operation signature name.
     */
    public static final IOperationSignatureSelector DEFAULT_OPERATION_SIGNATURE_MAPPER = (method,
            operationSignature) -> StringUtils.trimAndRemoveSpaces(method.getName()).get()
                    .contains(StringUtils.trimAndRemoveSpaces(operationSignature.getName()).get());

    /**
     * Check the name of the method or the class against the operation signature. Return true if the
     * method name or the class name contains the operation signature name.
     */
    public static final IOperationSignatureSelector DEFAULT_OPERATION_SIGNATURE_MAPPER_2 = (method,
            operationSignature) -> CorrespondeceModelFactory.DEFAULT_OPERATION_SIGNATURE_MAPPER.select(method,
                    operationSignature)
                    || method.getParent().getUnitName().toLowerCase()
                            .contains(operationSignature.getName().toLowerCase());

    /**
     * Simple constructor does nothing.
     */
    private CorrespondeceModelFactory() {
        // do nothing
    }

    /**
     *
     * @param pathMappingFile
     *            path to the mapping file generated by Protocom. It provides the information how
     *            Protocom generated source code out of PCM components.
     * @return a {@link Correspondent} instance representing the mapping file
     */
    public ICorrespondence createCorrespondenceModel(final String pathMappingFile) {
        final PcmMapping mapping = this.getMapping(pathMappingFile);
        final CorrespondenceModelImpl rac = new CorrespondenceModelImpl(mapping);
        rac.initMapping();
        return rac;
    }

    /**
     * Read the mapping file and return the mapping model.
     *
     * @param path
     *            path
     * @return pcm mapping instance
     */
    private PcmMapping getMapping(final String path) {
        PcmMapping mapping = null;
        try {
            final FileInputStream input = new FileInputStream(path);
            mapping = JAXB.unmarshal(input, PcmMapping.class);
        } catch (final Exception e) { // NOCS NOPMD
            e.printStackTrace();
        }
        return mapping;
    }

}
