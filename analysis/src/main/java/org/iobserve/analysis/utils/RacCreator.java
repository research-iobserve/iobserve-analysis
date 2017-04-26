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
package org.iobserve.analysis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.iobserve.analysis.protocom.PcmCorrespondentMethod;
import org.iobserve.analysis.protocom.PcmEntity;
import org.iobserve.analysis.protocom.PcmEntityCorrespondent;
import org.iobserve.analysis.protocom.PcmMapping;
import org.iobserve.analysis.protocom.PcmOperationSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    private final Map<Integer, Set<List<String>>> monitoringData;
    private final Map<String, String> systemMapping;

    private final Map<String, PcmEntityCorrespondent> correspondentMapping;
    private final Map<String, PcmEntity> entityMapping;
    private final List<String> unmappedCorrespondents;

    /**
     * Main method for the RacCreator.
     *
     * @param args
     *            command line arguments
     */
    public static void main(final String[] args) {
        final RacCreator creator = new RacCreator();

        // Setting input paths
        creator.readRepository("rac_creator\\cocome-cloud.repository");

        creator.createCorrespondentMapping("rac_creator\\kieker-input.dat");

        creator.readModelMapping("rac_creator\\model_mapping.txt");

        creator.mapCorrespondentsToEntitys();

        // Setting output paths
        creator.createRac("rac_creator\\mapping.rac");

        creator.createMappedFile("rac_creator\\mapped.txt");

        creator.createUnmappedFile("rac_creator\\unmapped.txt");
    }

    public RacCreator() {
        this.monitoringData = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            this.monitoringData.put(i, new HashSet<List<String>>());
        }
        this.systemMapping = new HashMap<>();
        this.correspondentMapping = new HashMap<>();
        this.entityMapping = new HashMap<>();

        this.unmappedCorrespondents = new ArrayList<>();
    }

    public void readRepository(final String filePath) {
        try {
            final File inputFile = new File(filePath);
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(inputFile);
            final Element root = doc.getDocumentElement();

            this.parseRepositoryComponents(this.parseRepositoryInterfaces(root), root);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createCorrespondentMapping(final String filePath) {
        final Map<Integer, Set<List<String>>> localMonitoringData = this.parseMonitoringData(filePath);
        // Reading the calls with shortcut for BeforeOperationObjectInterfaceEvent, BeforeOperationObjectEvent 
        // and AfterOperationObjectEvent
        this.createCorrespondents(localMonitoringData, 4);
        this.createCorrespondents(localMonitoringData, 5);
        this.createCorrespondents(localMonitoringData, 6);
    }

    public void readModelMapping(final String filePath) {
        try {
            final FileReader reader = new FileReader(filePath);
            final BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            while (line != null) {
                final String[] mapping = line.split(" - ");
                this.systemMapping.put(mapping[0], mapping[1]);

                line = in.readLine();
            }
            in.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void mapCorrespondentsToEntitys() {
        for (final String correspondent : this.correspondentMapping.keySet()) {
            String key = correspondent;
            String entityKey;

            if (this.systemMapping.containsKey(correspondent)) {
                key = this.systemMapping.get(correspondent);
            }

            if (this.entityMapping.containsKey(key)) {
                entityKey = key;
            } else {
                this.unmappedCorrespondents.add(key);
                continue;
            }

            final PcmEntity entity = this.entityMapping.get(entityKey);
            final PcmEntityCorrespondent corr = this.correspondentMapping.get(correspondent);
            entity.getCorrespondents().add(corr);
        }

    }

    public void createRac(final String filePath) {
        final PcmMapping mapping = new PcmMapping();
        mapping.setEntities(new ArrayList<>(this.entityMapping.values()));
        JAXB.marshal(mapping, filePath);
    }

    public void createMappedFile(final String filePath) {
        Set<String> methodCorrespondentPairs = new HashSet<>();
        for (PcmEntity entity : entityMapping.values()) {
            for (PcmEntityCorrespondent correspondent : entity.getCorrespondents()) {
                methodCorrespondentPairs.addAll(findMutualMethodAndCorrPairs(entity, correspondent));
            }
        }

        try {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            for (String methodCorrespondentPair : methodCorrespondentPairs) {
                writer.println(methodCorrespondentPair);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUnmappedFile(final String filePath) {
        try {
            final PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            for (final String correspondent : this.unmappedCorrespondents) {
                writer.println(correspondent);
            }
            writer.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, Set<List<String>>> parseMonitoringData(final String filePath) {
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return localMonitoringData;
    }

    private void createCorrespondents(final Map<Integer, Set<List<String>>> monitoringData, final int i) {
        final Set<List<String>> data = monitoringData.get(i);
        for (final List<String> correspondentData : data) {

            String corrName = correspondentData.get(6).replaceAll("\\s+", "");
            if (corrName.contains("$")) {
                corrName = corrName.substring(0, corrName.lastIndexOf('$'));
            }

            if (corrName.contains("cloud-web-frontend")) {
                continue;
            }
            final PcmEntityCorrespondent correspondent = this.getCorrespondent(corrName);

            final String[] filteredMethod = this.filterMethodString(correspondentData.get(5));
            if (filteredMethod != null) {
                final PcmCorrespondentMethod method = new PcmCorrespondentMethod();
                method.setVisibilityModifier(filteredMethod[0]);
                method.setReturnType(filteredMethod[1]);
                method.setName(filteredMethod[2]);
                method.setParameters(filteredMethod[3]);

                this.addMethodToCorrespondent(method, correspondent);
            }
        }
    }

    private void parseRepositoryComponents(final Map<String, PcmEntity> interfaceMapping, final Element root) {
        final NodeList components = root.getChildNodes();
        for (int i = 0; i < components.getLength(); i++) {
            final Node component = components.item(i);
            if (component.getNodeName().compareToIgnoreCase("components__Repository") == 0) {
                final PcmEntity repComponent = new PcmEntity();
                final NamedNodeMap attributes = component.getAttributes();
                final String entityName = attributes.getNamedItem("entityName").getNodeValue().replaceAll("\\s+", "");
                repComponent.setName(entityName);
                repComponent.setId(attributes.getNamedItem("id").getNodeValue());

                repComponent.setOperationSigs(this.parseComponentOperations(interfaceMapping, component));
                this.entityMapping.put(repComponent.getName(), repComponent);
            }
        }
    }

    private List<PcmOperationSignature> parseComponentOperations(final Map<String, PcmEntity> interfaceMapping,
            final Node component) {
        final List<PcmOperationSignature> operations = new ArrayList<>();
        final NodeList childs = component.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            final Node operation = childs.item(i);
            final NamedNodeMap attributes = operation.getAttributes();
            switch (operation.getNodeName()) {
            case "signatures__OperationInterface":
                final PcmOperationSignature opSig = new PcmOperationSignature();
                opSig.setName(attributes.getNamedItem("entityName").getNodeValue());
                opSig.setSeffName(opSig.getName());
                opSig.setId(attributes.getNamedItem("id").getNodeValue());
                operations.add(opSig);
                break;
            case "providedRoles_InterfaceProvidingEntity":
                final Node item = attributes.getNamedItem("providedInterface__OperationProvidedRole");
                if (item == null) {
                    break; // removing Sinkholes
                }
                final PcmEntity entity = interfaceMapping.get(item.getNodeValue());
                operations.addAll(entity.getOperationSigs());
                break;
            default:
                break;
            }
        }

        return operations;
    }

    private Map<String, PcmEntity> parseRepositoryInterfaces(final Element root) {
        final Map<String, PcmEntity> mapping = new HashMap<>();
        final NodeList components = root.getChildNodes();
        for (int i = 0; i < components.getLength(); i++) {
            final Node component = components.item(i);
            if (component.getNodeName().compareToIgnoreCase("interfaces__Repository") == 0) {
                final PcmEntity repInterface = new PcmEntity();
                final NamedNodeMap attributes = component.getAttributes();
                final String entityName = attributes.getNamedItem("entityName").getNodeValue().replaceAll("\\s+", "");
                repInterface.setName(entityName);
                repInterface.setId(attributes.getNamedItem("id").getNodeValue());
                repInterface.setOperationSigs(this.parseInterfaceOperations(component));

                mapping.put(repInterface.getId(), repInterface);
            }
        }

        return mapping;
    }

    private List<PcmOperationSignature> parseInterfaceOperations(final Node component) {
        final List<PcmOperationSignature> operations = new ArrayList<>();
        final NodeList childs = component.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            final Node operation = childs.item(i);
            if (operation.getNodeName().compareToIgnoreCase("signatures__OperationInterface") == 0) {
                final PcmOperationSignature opSig = new PcmOperationSignature();
                final NamedNodeMap attributes = operation.getAttributes();
                opSig.setName(attributes.getNamedItem("entityName").getNodeValue().replaceAll("\\s+", ""));
                opSig.setSeffName(opSig.getName());
                opSig.setId(attributes.getNamedItem("id").getNodeValue());

                operations.add(opSig);
            }
        }

        return operations;
    }

    // Returns a correspondent for the given name, if not already contained it is created.
    private PcmEntityCorrespondent getCorrespondent(final String corrName) {
        if (!this.correspondentMapping.containsKey(corrName)) {
            final PcmEntityCorrespondent correspondent = new PcmEntityCorrespondent();
            correspondent.setFilePath("No Path");
            correspondent.setProjectName("No Project");
            try {
                correspondent.setPackageName(corrName.substring(0, corrName.lastIndexOf('.')));
                correspondent.setUnitName(corrName.substring(corrName.lastIndexOf('.') + 1));
            } catch (final Exception e) {
                e.printStackTrace();
            }
            this.correspondentMapping.put(corrName, correspondent);
        }

        return this.correspondentMapping.get(corrName);
    }

    // Returns an array in the order: visibilityModifier, returnType, methodName
    // or null if the given string is not a valid method
    private String[] filterMethodString(final String correspondentData) {
        final String[] method = new String[4];
        final String[] splitMethod = correspondentData.split(" ");

        for (int i = 0; i < splitMethod.length; i++) {
            final String split = splitMethod[i];
            if (split.contentEquals("private") || split.contentEquals("public")) {
                method[0] = split;
            } else if (split.contentEquals("static")) {
                method[0] = "public";
            } else if (split.contentEquals("transient")) {
                continue;
            } else if ((i >= 1) && (method[1] == null)) {
                method[1] = split;
            } else if ((i >= 2) && (method[2] == null)) {
                try {
                    String methodSig = split.substring(0, split.indexOf('('));

                    methodSig = methodSig.substring(methodSig.lastIndexOf('.') + 1);
                    if (methodSig.contains("$")) {
                        methodSig = methodSig.substring(0, methodSig.lastIndexOf('$'));
                    }

                    method[2] = methodSig;
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }

        final int firstIndex = correspondentData.indexOf('(');
        final int lastIndex = correspondentData.lastIndexOf(')');
        final String parameters = correspondentData.substring(firstIndex + 1, lastIndex);
        method[3] = parameters.replaceAll("\\s+", "");

        if ((method[0] != null) && (method[1] != null) && (method[2] != null) && (method[3] != null)) {
            return method;
        }

        return null;
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

    private Set<String> findMutualMethodAndCorrPairs(final PcmEntity entity, final PcmEntityCorrespondent correspondent) {
        Set<String> foundMutualMethods = new HashSet<>();

        for (PcmOperationSignature sig : entity.getOperationSigs()) {
            for (PcmCorrespondentMethod met : correspondent.getMethods()) {
                if (sig.getName().compareTo(met.getName()) == 0 && sig.getSeffName().compareTo(met.getName()) == 0) {
                    String correspondentPath = correspondent.getPackageName() + "." + correspondent.getUnitName();
                    String signature = met.getVisibilityModifier() + " " + met.getReturnType() + " " + correspondentPath + "." + met.getName() + "(" + met.getParameters() + ")" + ";" + correspondentPath;
                    foundMutualMethods.add(signature);
                }
            }
        }

        return foundMutualMethods;
    }
}
