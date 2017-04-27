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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.iobserve.analysis.protocom.PcmEntity;
import org.iobserve.analysis.protocom.PcmOperationSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Nicolas Boltz -- initial contribution
 * @author Reiner Jung
 *
 */
public class RepositoryFileReader {

    private final File repositoryFile;
    private final Map<String, PcmEntity> entityMapping;

    /**
     * Create a repository handler for a file.
     *
     * @param repositoryFile
     *            file to the repository
     */
    public RepositoryFileReader(final File repositoryFile) {
        this.repositoryFile = repositoryFile;
        this.entityMapping = new HashMap<>();
    }

    /**
     * Compute the mapping.
     *
     * @return returns the mapping
     * @throws ParserConfigurationException
     *             configuration error
     * @throws IOException
     *             on file IO error
     * @throws SAXException
     *             on SAX parse error
     */
    public Map<String, PcmEntity> computeMapping() throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.parse(this.repositoryFile);
        final Element root = doc.getDocumentElement();

        this.parseRepositoryComponents(this.parseRepositoryInterfaces(root), root);

        return this.entityMapping;
    }

    private void parseRepositoryComponents(final Map<String, PcmEntity> interfaceMapping, final Element root) {
        final NodeList components = root.getChildNodes();
        for (int i = 0; i < components.getLength(); i++) {
            final Node component = components.item(i);
            if (component.getNodeName().compareToIgnoreCase("components__Repository") == 0) {
                final PcmEntity repositoryComponent = new PcmEntity();
                final NamedNodeMap attributes = component.getAttributes();
                final String entityName = attributes.getNamedItem("entityName").getNodeValue().replaceAll("\\s+", "");
                repositoryComponent.setName(entityName);
                repositoryComponent.setId(attributes.getNamedItem("id").getNodeValue());

                repositoryComponent.setOperationSigs(this.parseComponentOperations(interfaceMapping, component));

                this.entityMapping.put(repositoryComponent.getName(), repositoryComponent);
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

}
