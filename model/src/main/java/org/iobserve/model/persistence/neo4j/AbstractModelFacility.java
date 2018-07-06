/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.persistence.neo4j;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 */
public abstract class AbstractModelFacility {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModelFacility.class);

    protected final GraphDatabaseService graphDatabaseService;

    protected final Map<EObject, Node> objectNodeMap;

    /**
     * Create an abstract model facility.
     *
     * @param graphDatabaseService
     *            database for the model
     * @param objectNodeMap
     *            object to node map
     */
    public AbstractModelFacility(final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap) {
        this.graphDatabaseService = graphDatabaseService;
        this.objectNodeMap = objectNodeMap;
    }

    /**
     * Create an object URI for a given object and set the corresponding node property.
     *
     * @param node
     *            the node
     * @param storeableObject
     *            the object
     */
    public void setNodeObjectUri(final Node node, final EObject storeableObject) {
        final URI uri = ((BasicEObjectImpl) storeableObject).eProxyURI();
        if (uri == null) {
            node.setProperty(ModelProviderUtil.EMF_URI, ModelProviderUtil.getUriString(storeableObject));
        } else {
            node.setProperty(ModelProviderUtil.EMF_URI, uri.toString());
        }
    }

    /**
     * Based on a certain object-URI and a list of references to nodes which possibly represent that
     * component, this method returns the node which actually represents the component or null if
     * there is none in the list. Relationships to matching nodes are removed from the list, so this
     * method can also be used to reduce a list of references to those references which link to
     * nodes whose component does not exist anymore.
     *
     * @param uri
     *            The object-URI
     * @param relationships
     *            The relationships to possibly matching nodes
     * @return The node representing the component or null if there is none
     */
    public Node findMatchingNode(final String uri, final List<Relationship> relationships) {
        if (uri != null) {
            for (final Relationship relationship : relationships) {
                final Node node = relationship.getEndNode();
                try {
                    final String nodeUri = node.getProperty(ModelProviderUtil.EMF_URI).toString();

                    if (uri.equals(nodeUri)) {
                        relationships.remove(relationship);
                        return node;
                    }
                } catch (final NotFoundException e) {
                    AbstractModelFacility.LOGGER.error(
                            "Tried to delete a relationship which has already been removed. id {} and exception {}",
                            relationship.getId(), e);
                }
            }
        }

        return null;
    }
}
