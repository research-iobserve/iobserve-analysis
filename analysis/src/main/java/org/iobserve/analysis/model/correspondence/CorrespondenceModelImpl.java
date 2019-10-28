/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.protocom.PcmCorrespondentMethod;
import org.iobserve.analysis.protocom.PcmEntity;
import org.iobserve.analysis.protocom.PcmEntityCorrespondent;
import org.iobserve.analysis.protocom.PcmMapping;
import org.iobserve.analysis.protocom.PcmOperationSignature;
import org.iobserve.analysis.utils.StringUtils;

/**
 * Implementation of {@link ICorrespondence}.
 *
 * @author Robert Heinrich
 * @author Nicolas Boltz
 * @author Alessandro Giusa
 *
 */
class CorrespondenceModelImpl implements ICorrespondence {

    private static final Logger LOGGER = LogManager.getLogger(CorrespondenceModelImpl.class);

    /** cache for already mapped correspondences. */
    private final Map<String, Correspondent> cachedCorrespondents = new HashMap<>();

    /** raw mapping objects created during ProtoCom artifacts generation. */
    private final PcmMapping rawMapping;

    /** fast access map for class-signature to object. */
    private Map<String, PcmEntityCorrespondent> mapping;

    /** namespace of current palladio framework. */
    // private static final String PROTOCOM_BASE_PACKAGE_NAME = "org.palladiosimulator.protocom";

    /**
     * Create correspondence model.
     *
     * @param theMapping
     *            mapping instance
     * @param mapper
     *            selector
     */
    public CorrespondenceModelImpl(final PcmMapping theMapping) {
        this.rawMapping = theMapping;
    }

    /**
     * Create the correspondence model.
     *
     * @param mappingFile
     *            input stream of mapping file
     * @param mapper
     *            selector
     */
    public CorrespondenceModelImpl(final InputStream mappingFile) {
        this.rawMapping = JAXB.unmarshal(mappingFile, PcmMapping.class);
        this.initMapping();
    }

    /**
     * Init mapping.
     * <ul>
     * <li>Create Map for fast access</li>
     * <li>Set parent references on {@link PcmMapping} objects</li>
     * </ul>
     */
    public void initMapping() {
        this.mapping = new HashMap<>();

        for (final PcmEntity pcmEntity : this.rawMapping.getEntities()) {
            pcmEntity.setParent(this.rawMapping);

            // set the parent reference
            for (final PcmOperationSignature entityOperation : pcmEntity.getOperationSigs()) {
                entityOperation.setParent(pcmEntity);
            }

            // set parent reference and convert the mapping
            for (final PcmEntityCorrespondent entityCorrespondent : pcmEntity.getCorrespondents()) {
                entityCorrespondent.setParent(pcmEntity);

                final String qualifiedName = (entityCorrespondent.getPackageName() + "."
                        + entityCorrespondent.getUnitName()).trim().replaceAll(" ", "");
                this.mapping.put(qualifiedName, entityCorrespondent);

                // set parent reference
                for (final PcmCorrespondentMethod correspondentMethod : entityCorrespondent.getMethods()) {
                    correspondentMethod.setParent(entityCorrespondent);
                }

                entityCorrespondent.initMethodMap();
            }

            pcmEntity.initOperationMap();
        }
    }

    // ********************************************************************
    // * MAPPING
    // ********************************************************************

    /**
     * Returns whether or not a correspondent is contained in the model.
     */
    @Override
    public boolean containsCorrespondent(final String classSig, final String operationSig) {
        return this.getCorrespondent(classSig, operationSig).isPresent();
    }

    @Override
    public Optional<Correspondent> getCorrespondent(final String classSig, final String operationSig) {
        // TODO debug print, remove later
        // System.out.print(String.format("Try to get correspondence for classSig=%s,
        // operationSig=%s...",
        // classSig, operationSig));

        // assert parameters are not null
        if ((classSig == null) || (operationSig == null)) {
            return ICorrespondence.NULL_CORRESPONDENZ;
        }

        // create the request key for searching in the cache
        final String requestKey = classSig.trim().replaceAll(" ", "") + operationSig.trim().replaceAll(" ", "");

        // try to get the correspondent from the cache
        Correspondent correspondent = this.cachedCorrespondents.get(requestKey);

        // in case the correspondent is not available it has to be mapped.
        // This case should never occur in normal analysis, since the correspondents are created and cached in 
        // TEntryCallSequence by calling this method.
        if (correspondent == null) {
            final PcmEntityCorrespondent pcmEntityCorrespondent = this.getPcmEntityCorrespondent(classSig);
            if (pcmEntityCorrespondent == null) {
                CorrespondenceModelImpl.LOGGER.info("Mapping not available for class signature: " + classSig);
                return ICorrespondence.NULL_CORRESPONDENZ;
            }

            final PcmOperationSignature pcmOperationSignature = this.getPcmOperationSignature(pcmEntityCorrespondent,
                    operationSig);
            if (pcmOperationSignature == null) {
                CorrespondenceModelImpl.LOGGER.info("Mapping not available for operation signature: " + operationSig);

                return ICorrespondence.NULL_CORRESPONDENZ;
            }

            // create correspondent object
            correspondent = CorrespondentFactory.newInstance(pcmEntityCorrespondent.getParent().getName(),
                    pcmEntityCorrespondent.getParent().getId(), pcmOperationSignature.getName(),
                    pcmOperationSignature.getId());

            // put into cache for next time
            this.cachedCorrespondents.put(requestKey, correspondent);
        }

        return Optional.of(correspondent);
    }

    @Override
    public Optional<Correspondent> getCorrespondent(final String classSig) {

        CorrespondenceModelImpl.LOGGER.debug(String.format("Try to get correspondence for classSig=%s ...", classSig));

        // assert parameters are not null
        if (classSig == null) {
            return ICorrespondence.NULL_CORRESPONDENZ;
        }

        // create the request key for searching in the cache
        final String requestKey = classSig.trim().replaceAll(" ", "");

        // try to get the correspondent from the cache
        Correspondent correspondent = this.cachedCorrespondents.get(requestKey);

        // in case the correspondent is not available it has to be mapped
        if (correspondent == null) {
            final PcmEntityCorrespondent pcmEntityCorrespondent = this.getPcmEntityCorrespondent(classSig);
            if (pcmEntityCorrespondent == null) {
                return ICorrespondence.NULL_CORRESPONDENZ; // or something else
            }

            // create correspondent object
            correspondent = CorrespondentFactory.newInstance(pcmEntityCorrespondent.getParent().getName(),
                    pcmEntityCorrespondent.getParent().getId(), null, null);

            // put into cache for next time
            this.cachedCorrespondents.put(requestKey, correspondent);
        }

        return Optional.of(correspondent);
    }

    /**
     * Get the {@link PcmEntity} based on the qualified class name.
     *
     * @param classSig
     *            class signature
     *
     * @return null if not available
     */
    private PcmEntityCorrespondent getPcmEntityCorrespondent(final String classSig) {
        final PcmEntityCorrespondent pcmEntityCorrespondent = this.mapping.get(classSig.trim().replaceAll(" ", ""));
        return pcmEntityCorrespondent;
    }

    /**
     * Get the corresponding operation signature based the given operation signature.
     *
     * @param pcmEntityCorrespondent
     *            pcm entity correspondence
     * @param operationSig
     *            operation signature
     * @return pcm operation signature or null if operation signature not available
     */
    private PcmOperationSignature getPcmOperationSignature(final PcmEntityCorrespondent pcmEntityCorrespondent,
            final String operationSig) {
        String localOperationSig = operationSig.replaceFirst("static", "");
        if (localOperationSig.contains("throws")) {
            localOperationSig = localOperationSig.substring(0, localOperationSig.indexOf("throws"));
        }

        final PcmCorrespondentMethod correspondentMethod = pcmEntityCorrespondent
                .getMethod(localOperationSig.replaceAll(" ", ""));
        if (correspondentMethod != null) {
            return this.mapOperationSignature(correspondentMethod);
        } else {
            return null;
        }
    }

    /**
     * Map the given method to the correspondent operation signature based on the name. The
     * comparison is done by searching the operation signature name which is contained in the given
     * method name.
     *
     * @param method
     *            method
     *
     * @return null if not found
     */
    private PcmOperationSignature mapOperationSignature(final PcmCorrespondentMethod method) {
        final PcmEntity pcmEntity = method.getParent().getParent();
        PcmOperationSignature opSig = pcmEntity
                .getOperationSig(StringUtils.modifyForOperationSigMatching(method.getName()).get());
        if (opSig == null) {
            opSig = pcmEntity
                    .getOperationSig(StringUtils.modifyForOperationSigMatching(method.getParent().getUnitName()).get());
        }

        return opSig;
    }

    /**
     * Test method to print all mappings.
     */
    @SuppressWarnings("unused")
    private void printAllMappings() {
        for (final String nextMappingKey : this.mapping.keySet()) {
            System.out.println(nextMappingKey);
            final PcmEntityCorrespondent correspondent = this.mapping.get(nextMappingKey);
            System.out.println(correspondent);
        }
    }
}
