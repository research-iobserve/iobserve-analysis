/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.persistence;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.NodeLookupException;

/**
 *
 * @author Reiner Jung
 *
 * @param <R>
 */
public interface IModelResource<R extends EObject> {

    /**
     * Deletes all nodes and relationships in the graph database.
     */
    void clearResource();

    /**
     * Store the given element and all contained elements.
     *
     * @param rootElement
     *            root element to be stored
     * @throws DBException
     *             on db errors
     */
    void storeModelPartition(final R rootElement) throws DBException;

    /**
     * Update an partition.
     *
     * @param object
     *            partition root
     * @param <T>
     *            type
     * @throws NodeLookupException
     *             on node lookup errors
     * @throws DBException
     *             on db errors
     */
    <T extends EObject> void updatePartition(final T object) throws NodeLookupException, DBException;

    /**
     * Delete an object by id and type.
     *
     * @param object
     *            the object
     * @param <T>
     *            type of the object
     * @throws DBException
     *             on db errors
     */
    <T> void deleteObject(final EObject object) throws DBException;

    /**
     * Reads components from the provider's graph by their entityName without locking it for other
     * providers. Note that not all components in the PCM models have an entityName and that an
     * entityName doesn't need to be unique. If multiple components of the specified type have the
     * specified name, the returned list contains all of them.
     *
     * @param clazz
     *            Data type of component(s) to be read
     * @param eClass
     *            EMF class
     * @param key
     *            key name
     * @param value
     *            value of the component(s) to be read
     * @param <T>
     *            type definition
     * @return List of the read component(s)
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    <T extends EObject> List<T> findObjectsByTypeAndProperty(final Class<T> clazz, final EClass eClass,
            final String key, final String value) throws DBException;

    /**
     * Collect all objects of a given type.
     *
     * @param clazz
     *            the type of the objects
     * @param eClass
     *            eClass corresponding to clazz
     *
     * @param <T>
     *            type of the objects
     *
     * @return returns a list of objects
     *
     * @throws DBException
     *             when the search for a matching reference to a relationship
     */
    <T extends EObject> List<T> collectAllObjectsByType(final Class<T> clazz, final EClass eClass) throws DBException;

    /**
     * Collects objects with refer to an object specified by class and id from the provider's graph
     * without locking it for other providers.
     *
     * @param clazz
     *            Data type of the referenced object
     * @param eClass
     *            eClass corresponding to clazz
     * @param property
     *            Id of the referenced object
     * @return List of all objects which reference the specified object
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    List<EObject> collectReferencingObjectsByTypeAndProperty(final Class<?> clazz, final EClass eClass,
            final String property) throws DBException;

    /**
     * Read the root node of a model and lock the resource.
     *
     * @param clazz
     *            type of the root class
     * @param eClass
     *            eclass corresponding to clazz
     * @return returns the object
     * @throws DBException
     *             when a relationship is not backed by a EReference
     */
    R getAndLockModelRootNode(final Class<R> clazz, final EClass eClass) throws DBException;

    /**
     * Reads the models (partition) root node and all contained classes without locking the graph
     * for other providers.
     *
     * @param clazz
     *            Data type of the root object
     * @param eClass
     *            eclass type corresponding to clazz
     * @return The read object
     * @throws DBException
     *             on db error
     */
    R getModelRootNode(final Class<R> clazz, final EClass eClass) throws DBException;

    /**
     * Check if the given type is supported by this resources factories.
     *
     * @param <T>
     *            class of the proxy object
     *
     * @param proxyObject
     *            the proxy object
     * @return returns true on success
     */
    <T extends EObject> boolean isTypeManagedByResource(final T proxyObject);

    /**
     * Resolve proxy object.
     *
     * @param proxyObject
     *            resolve proxy object
     * @param <T>
     *            type
     * @throws InvocationException
     *             when called with a null object
     * @throws DBException
     *             on database issues
     *
     * @return returns the resolved objects
     */
    <T extends EObject> T resolve(final T proxyObject) throws InvocationException, DBException;

    <T extends EObject> T findObjectByTypeAndId(Class<T> clazz, EClass eClass, String identification)
            throws DBException;

    <T extends EObject> T findAndLockObjectById(Class<T> clazz, EClass eClass, String identification)
            throws DBException;
}
