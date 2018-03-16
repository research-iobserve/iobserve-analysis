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
package org.iobserve.rac.creator.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.model.protocom.PcmCorrespondentMethod;
import org.iobserve.model.protocom.PcmEntity;
import org.iobserve.model.protocom.PcmEntityCorrespondent;
import org.iobserve.rac.creator.ModelMappingReader;
import org.iobserve.rac.creator.RepositoryFileReader;
import org.iobserve.rac.creator.data.ClassAndMethod;
import org.xml.sax.SAXException;

// TODO This is a filter containing the core functionality. It can be divided into smaller filters.
/**
 *
 * Presently, it is used to support incremental refactoring.
 *
 * @author Reiner Jung
 *
 */
public class DoAllFilter extends AbstractConsumerStage<ClassAndMethod> {

    /** output port for unmapped classes. */
    private final OutputPort<String> unmappedOutputPort = this.createOutputPort();
    /** output port for mapped classes. */
    private final OutputPort<String> mappedOutputPort = this.createOutputPort();
    /** output port for rac elements. */
    private final OutputPort<Map<String, PcmEntity>> racOutputPort = this.createOutputPort();

    private final Map<String, PcmEntityCorrespondent> correspondentMapping = new HashMap<>();

    private final Map<String, PcmEntity> repositoryMapping;
    private final Map<String, String> modelMapping;

    /**
     * Create filter.
     *
     * @param repositoryFileReader
     *            PCM repository file reader
     * @param modelMappingReader
     *            readef for the model mapping
     * @throws ParserConfigurationException
     *             on broken XML
     * @throws SAXException
     *             on broken XMl
     * @throws IOException
     *             on read errors
     */
    public DoAllFilter(final RepositoryFileReader repositoryFileReader, final ModelMappingReader modelMappingReader)
            throws ParserConfigurationException, SAXException, IOException {
        this.repositoryMapping = repositoryFileReader.computeMapping();
        this.modelMapping = modelMappingReader.readModelMapping();
    }

    public OutputPort<Map<String, PcmEntity>> getRACOutputPort() {
        return this.racOutputPort;
    }

    public OutputPort<String> getMappedOutputPort() {
        return this.mappedOutputPort;
    }

    public OutputPort<String> getUnmappedOutputPort() {
        return this.unmappedOutputPort;
    }

    /**
     * Main execution method.
     *
     * @param element
     *            one class and method object passed to this filter.
     */
    @Override
    protected void execute(final ClassAndMethod element) {
        final PcmEntityCorrespondent correspondent = this.getOrCreateCorrespondent(element.getClassSignature(),
                this.correspondentMapping);
        /** only add method if it does not exist in the correspondent. */
        this.addMethodUnique(correspondent, element.getMethod());
        this.checkIfMappingExists(element.getClassSignature(), correspondent);

        this.racOutputPort.send(this.repositoryMapping);
    }

    // TODO this has a nasty side effect.

    /**
     * Returns a correspondent for the given name, if not already contained it is created.
     *
     * @param classSignature
     *            class signature to be looked up
     * @param newCorrespondentMapping
     *            new set of mappings
     * @return the correspondent for the given class signature
     */
    private PcmEntityCorrespondent getOrCreateCorrespondent(final String classSignature,
            final Map<String, PcmEntityCorrespondent> newCorrespondentMapping) {
        /** TODO this filters test whether a certain class signature already exists. */
        if (!newCorrespondentMapping.containsKey(classSignature)) {
            final PcmEntityCorrespondent correspondent = new PcmEntityCorrespondent();
            correspondent.setFilePath("No Path");
            correspondent.setProjectName("No Project");

            correspondent.setPackageName(classSignature.substring(0, classSignature.lastIndexOf('.')));
            correspondent.setUnitName(classSignature.substring(classSignature.lastIndexOf('.') + 1));

            newCorrespondentMapping.put(classSignature, correspondent);
        }

        return newCorrespondentMapping.get(classSignature);
    }

    /**
     * Adds the method to the correspondent if the correspondent does not already.
     *
     * @param method
     *            pcm method
     * @param correspondent
     *            container for the method
     */
    private void addMethodUnique(final PcmEntityCorrespondent correspondent, final PcmCorrespondentMethod method) {
        for (final PcmCorrespondentMethod corrMethod : correspondent.getMethods()) {
            if (corrMethod.getName().contentEquals(method.getName())) {
                return; // Method already existent;
            }
        }

        correspondent.getMethods().add(method);
    }

    /**
     * Check whether a mapping exists or not.
     *
     * @param classSignature
     *            the lookup key for the correspondent
     * @param correspondentValue
     *            the correspondent itself
     * @param unmappedCorrespondents
     *            output adds a unmapped element to this list
     */
    private void checkIfMappingExists(final String classSignature, final PcmEntityCorrespondent correspondentValue) {
        String key = classSignature;

        if (this.modelMapping.containsKey(classSignature)) {
            key = this.modelMapping.get(classSignature);
        }

        if (this.repositoryMapping.containsKey(key)) {
            // TODO remaining code should go to a mapper stage. However, it also modifies the data
            // model.
            final PcmEntity entity = this.repositoryMapping.get(key);
            entity.getCorrespondents().add(correspondentValue);
            this.mappedOutputPort.send(key);
        } else {
            this.unmappedOutputPort.send(key);
        }

    }

}
