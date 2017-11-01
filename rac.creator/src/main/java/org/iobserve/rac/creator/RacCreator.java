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
package org.iobserve.rac.creator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;
import javax.xml.parsers.ParserConfigurationException;

import org.iobserve.analysis.protocom.PcmCorrespondentMethod;
import org.iobserve.analysis.protocom.PcmEntity;
import org.iobserve.analysis.protocom.PcmEntityCorrespondent;
import org.iobserve.analysis.protocom.PcmMapping;
import org.iobserve.analysis.protocom.PcmOperationSignature;
import org.xml.sax.SAXException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**************************************************************************************************
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Important!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * This application is only for testing purposes and should never be used in working code!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Important!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 * The RacCreator creates a rac file in the context of the provided pcm repository model, monitoring
 * data and system mapping.
 *
 * For the rac to be constructed right it is necessary that the names of the entities in the
 * repository model match with the names of the classes called in the monitoring data. Exceptions
 * from this convention have to be explicitly declared in the system mapping like this: Name/Path in
 * monitoring data - Name/Path in repository
 *
 * @author Nicolas Boltz
 *
 *************************************************************************************************/
public class RacCreator {
    private static final Log LOG = LogFactory.getLog(RacCreator.class);

    private static final String RAC_FILENAME = "mapping.rac";
    private static final String MAPPED_CLASSES_FILE = "mapped.txt";
    private static final String UNMAPPED_CLASSES_FILE = "unmapped.txt";
    private static final String KIEKER_INPUT_DAT = "kieker-input.dat";

    // private final List<String> unmappedCorrespondents;

    private final File outputPath;
    private final File inputPath;
    private final File repositoryFile;
    private final File mappingFile;

    /**
     * Default RAC creator constructor.
     *
     * @param inputPath
     *            path to the input directory
     * @param repositoryFile
     *            repository file
     * @param mappingFile
     *            mapping file
     * @param outputPath
     *            output path
     */
    public RacCreator(final File inputPath, final File repositoryFile, final File mappingFile, final File outputPath) {
        this.inputPath = inputPath;
        this.repositoryFile = repositoryFile;
        this.mappingFile = mappingFile;
        this.outputPath = outputPath;
    }

    /**
     * Main execution routine.
     *
     * @throws IOException
     *             on IO error
     * @throws ParserConfigurationException
     *             on parser configuration error
     * @throws SAXException
     *             on parsing error
     */
    public void execute() throws IOException, ParserConfigurationException, SAXException {
        /** read input. */
        final RepositoryFileReader repositoryFileReader = new RepositoryFileReader(this.repositoryFile);
        final ModelMappingReader modelMappingReader = new ModelMappingReader(this.mappingFile);
        final Map<Integer, Set<List<String>>> monitoringData = this.readMonitoringData(this.inputPath);

        final Map<String, PcmEntity> repositoryMapping = repositoryFileReader.computeMapping();
        final Map<String, String> modelMapping = modelMappingReader.readModelMapping();

        /** computation. */
        final Map<String, PcmEntityCorrespondent> correspondentMapping = this
                .createCorrespondentMapping(monitoringData);

        final List<String> unmappedCorrespondents = this.mapCorrespondentsToEntities(modelMapping, repositoryMapping,
                correspondentMapping);

        /** output. */
        this.createRac(RacCreator.RAC_FILENAME, repositoryMapping);
        this.createMappedFile(RacCreator.MAPPED_CLASSES_FILE, repositoryMapping);
        this.createUnmappedFile(RacCreator.UNMAPPED_CLASSES_FILE, unmappedCorrespondents);
    }

    /**
     * Read monitoring data. It puts the records of the same type in on of 11 buckets.
     *
     * @return returns a map of sets of records
     * @throws IOException
     */
    private Map<Integer, Set<List<String>>> readMonitoringData(final File inputPath) throws IOException {
        final String filePath = inputPath.getCanonicalPath() + File.separator + RacCreator.KIEKER_INPUT_DAT;
        final Map<Integer, Set<List<String>>> localMonitoringData = new HashMap<>();

        for (int i = 0; i < 11; i++) {
            localMonitoringData.put(i, new HashSet<List<String>>());
        }

        try {
            final FileReader reader = new FileReader(filePath);
            final BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            while (line != null) {
                final int start = Integer.parseInt(line.substring(1, line.indexOf(';')));
                localMonitoringData.get(start).add(Arrays.asList(line.split(";")));

                line = in.readLine();
            }
            in.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return localMonitoringData;
    }

    private Map<String, PcmEntityCorrespondent> createCorrespondentMapping(
            final Map<Integer, Set<List<String>>> localMonitoringData) throws IOException {
        final Map<String, PcmEntityCorrespondent> correspondentMapping = new HashMap<>();

        /** select the type 4,5 and 6. */
        this.createCorrespondents(localMonitoringData, correspondentMapping, 4);
        this.createCorrespondents(localMonitoringData, correspondentMapping, 5);
        this.createCorrespondents(localMonitoringData, correspondentMapping, 6);

        return correspondentMapping;
    }

    /**
     * Modifies repositoryMapping by adding the correspondent to an entity. TODO This should be
     * converted to a filter with two output ports. One for unmapped elements and one for mapped
     * elements.
     *
     * @param systemMapping
     * @param repositoryMapping
     * @param correspondentMapping
     * @return
     */
    private List<String> mapCorrespondentsToEntities(final Map<String, String> systemMapping,
            final Map<String, PcmEntity> repositoryMapping,
            final Map<String, PcmEntityCorrespondent> correspondentMapping) {

        final List<String> unmappedCorrespondents = new ArrayList<>();

        for (final String correspondent : correspondentMapping.keySet()) {
            this.mapCorrespondent(systemMapping, repositoryMapping, correspondent,
                    correspondentMapping.get(correspondent), unmappedCorrespondents);
        }

        return unmappedCorrespondents;
    }

    private void mapCorrespondent(final Map<String, String> systemMapping,
            final Map<String, PcmEntity> repositoryMapping, final String correspondentKey,
            final PcmEntityCorrespondent correspondentValue, final List<String> unmappedCorrespondents) {
        String key = correspondentKey;

        if (systemMapping.containsKey(correspondentKey)) {
            key = systemMapping.get(correspondentKey);
        }

        if (repositoryMapping.containsKey(key)) {
            /** TODO port for mapped entity. */
            /** TODO remaining code should go to a mapper stage. */
            final PcmEntity entity = repositoryMapping.get(key);
            final PcmEntityCorrespondent corr = correspondentValue;
            entity.getCorrespondents().add(corr);
        } else {
            /** TODO port unmapped. */
            unmappedCorrespondents.add(key);
        }

    }

    /**
     * Create the RAC output model.
     *
     * @param filename
     *            name of the RAC model
     */
    private void createRac(final String filename, final Map<String, PcmEntity> entityMapping) {
        try {
            final PcmMapping mapping = new PcmMapping();
            mapping.setEntities(new ArrayList<>(entityMapping.values()));
            JAXB.marshal(mapping, this.outputPath.getCanonicalPath() + File.separator + filename);
        } catch (final IOException e) {
            RacCreator.LOG.error("Error creating RAC " + e.getLocalizedMessage());
        }
    }

    /**
     * Create a list containing all mapped classes.
     *
     * @param filename
     *            filename for the list
     */
    private void createMappedFile(final String filename, final Map<String, PcmEntity> entityMapping) {
        final List<String> methodCorrespondentPairs = new ArrayList<>();
        for (final PcmEntity entity : entityMapping.values()) {
            for (final PcmEntityCorrespondent correspondent : entity.getCorrespondents()) {
                methodCorrespondentPairs.addAll(this.findMutualMethodAndCorrPairs(entity, correspondent));
            }
        }

        this.createListFile(filename, methodCorrespondentPairs, "unmapped");
    }

    /**
     * Create a list containing all unmapped classes.
     *
     * @param filename
     *            filename for the list
     */
    private void createUnmappedFile(final String filename, final List<String> unmappedCorrespondents) {
        this.createListFile(filename, unmappedCorrespondents, "unmapped");
    }

    private void createListFile(final String filename, final List<String> stringList, final String label) {
        try {
            final PrintWriter writer = new PrintWriter(this.outputPath.getCanonicalPath() + File.separator + filename,
                    "UTF-8");
            for (final String line : stringList) {
                writer.println(line);
            }
            writer.close();
        } catch (final IOException e) {
            RacCreator.LOG.error("Error creating " + label + " file " + e.getLocalizedMessage());
        }
    }

    /**
     * Create correspondents for a specific record type.
     *
     * @param monitoringData
     *            input monitoring data
     * @param correspondentMapping
     *            output mapping
     * @param recordType
     *            the record type specified by ID
     */
    private void createCorrespondents(final Map<Integer, Set<List<String>>> monitoringData,
            final Map<String, PcmEntityCorrespondent> correspondentMapping, final int recordType) {
        final Set<List<String>> data = monitoringData.get(recordType);
        for (final List<String> correspondentData : data) {

            /** name is in field 6. */
            String classSignature = correspondentData.get(6).replaceAll("\\s+", "");
            if (classSignature.contains("$")) {
                classSignature = classSignature.substring(0, classSignature.lastIndexOf('$'));
            }

            /** ignore web front end. */
            /** TODO this filtering should be done by a filter. */
            if (!classSignature.contains("cloud-web-frontend")) {
                /** TODO this is also a filter. */
                /** operation signature is in field 5. */
                final PcmCorrespondentMethod filteredMethod = this.filterMethodString(correspondentData.get(5));

                if (filteredMethod != null) {
                    final PcmEntityCorrespondent correspondent = this.getOrCreateCorrespondent(classSignature,
                            correspondentMapping);
                    /** only add method if it does not exist in the correspondent. */
                    this.addMethodToCorrespondent(filteredMethod, correspondent);
                }
            }
        }
    }

    /**
     * Returns a correspondent for the given name, if not already contained it is created. TODO this
     * has a nasty side effect.
     *
     * @param classSignature
     * @param correspondentMapping
     * @return
     */
    private PcmEntityCorrespondent getOrCreateCorrespondent(final String classSignature,
            final Map<String, PcmEntityCorrespondent> correspondentMapping) {
        /** TODO this filters whether a certain class signature already exists. */
        if (!correspondentMapping.containsKey(classSignature)) {
            final PcmEntityCorrespondent correspondent = new PcmEntityCorrespondent();
            correspondent.setFilePath("No Path");
            correspondent.setProjectName("No Project");
            try {
                correspondent.setPackageName(classSignature.substring(0, classSignature.lastIndexOf('.')));
                correspondent.setUnitName(classSignature.substring(classSignature.lastIndexOf('.') + 1));
            } catch (final Exception e) {
                e.printStackTrace();
            }
            correspondentMapping.put(classSignature, correspondent);
        }

        return correspondentMapping.get(classSignature);
    }

    /**
     * Returns an array in the order: visibilityModifier, returnType, methodName or null if the
     * given string is not a valid method.
     *
     * @param correspondentSignature
     *            signature of a correspondent, i.e., an operation
     * @return returns an operation object or null
     */
    private PcmCorrespondentMethod filterMethodString(final String correspondentSignature) {
        final PcmCorrespondentMethod method = new PcmCorrespondentMethod();
        final String[] tokenizedSignature = correspondentSignature.split(" ");

        for (int i = 0; i < tokenizedSignature.length; i++) {
            final String token = tokenizedSignature[i];
            if (token.contentEquals("private")) {
                method.setVisibilityModifier(token);
            } else if (token.contentEquals("public")) {
                method.setVisibilityModifier(token);
            } else if (token.contentEquals("static")) {
                method.setVisibilityModifier("public");
            } else if (token.contentEquals("transient")) {
                continue;
            } else if ((i >= 1) && (method.getReturnType() == null)) {
                method.setReturnType(token);
            } else if ((i >= 2) && (method.getName() == null)) {
                try {
                    String methodSig = token.substring(0, token.indexOf('('));

                    methodSig = methodSig.substring(methodSig.lastIndexOf('.') + 1);
                    if (methodSig.contains("$")) {
                        methodSig = methodSig.substring(0, methodSig.lastIndexOf('$'));
                    }

                    method.setName(methodSig);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }

        final int firstIndex = correspondentSignature.indexOf('(');
        final int lastIndex = correspondentSignature.lastIndexOf(')');
        final String parameters = correspondentSignature.substring(firstIndex + 1, lastIndex);
        method.setParameters(parameters.replaceAll("\\s+", ""));

        if ((method.getVisibilityModifier() != null) && (method.getName() != null) && (method.getReturnType() != null)
                && (method.getParameters() != null)) {
            return method;
        } else {
            return null;
        }
    }

    // Adds the method to the correspondent if the correspondent does not already
    // contain a method with the same name.
    private void addMethodToCorrespondent(final PcmCorrespondentMethod method,
            final PcmEntityCorrespondent correspondent) {
        for (final PcmCorrespondentMethod corrMethod : correspondent.getMethods()) {
            if (corrMethod.getName().contentEquals(method.getName())) {
                return; // Method already existent;
            }
        }

        correspondent.getMethods().add(method);
    }

    private Set<String> findMutualMethodAndCorrPairs(final PcmEntity entity,
            final PcmEntityCorrespondent correspondent) {
        final Set<String> foundMutualMethods = new HashSet<>();

        for (final PcmOperationSignature sig : entity.getOperationSigs()) {
            for (final PcmCorrespondentMethod met : correspondent.getMethods()) {
                if ((sig.getName().compareTo(met.getName()) == 0)
                        && (sig.getSeffName().compareTo(met.getName()) == 0)) {
                    final String correspondentPath = correspondent.getPackageName() + "." + correspondent.getUnitName();
                    final String signature = met.getVisibilityModifier() + " " + met.getReturnType() + " "
                            + correspondentPath + "." + met.getName() + "(" + met.getParameters() + ")" + ";"
                            + correspondentPath;
                    foundMutualMethods.add(signature);
                }
            }
        }

        return foundMutualMethods;
    }
}
