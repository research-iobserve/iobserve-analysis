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
package org.iobserve.model.provider.neo4j;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * Provides methods to access pcm models or parts of them. Implementing classes have to define how
 * the models are serialized.
 *
 * @author Lars Bluemke
 *
 * @param <T>
 *            Type of the model's or submodel's root component
 */
public interface IModelProvider<T extends EObject> {

    /**
     * Writes the given component into the provider's model serialization.
     *
     * @param component
     *            Component to save
     */
    void createComponent(T component);

    /**
     * Reads a specified component from the provider's model serialization.
     *
     * @param clazz
     *            Data type of component to be read
     * @param id
     *            Id of component to be read
     * @return The read component
     */
    T readComponentById(Class<T> clazz, String id);

    /**
     * Reads components from the provider's model serialization by their entityName. Note that not
     * all components in the PCM models have an entityName and that an entityName doesn't need to be
     * unique. If multiple components of the specified type have the specified name, the returned
     * list contains all of them.
     *
     * @param clazz
     *            Data type of component(s) to be read
     * @param entityName
     *            EntityName of the component(s) to be read
     * @return List of the read component(s)
     */
    List<T> readComponentByName(Class<T> clazz, String entityName);

    /**
     * Reads the ids of all components of a specified data type.
     *
     * @param clazz
     *            The data type
     * @return List of ids of the specified data type
     */
    List<String> readComponentByType(Class<T> clazz);

    /**
     * Reads the pcm models root components Allocation, Repository, ResourceEnvironment, System or
     * UsageModel.
     *
     * @param clazz
     *            Data type of the root component
     * @return The read component
     */
    T readRootComponent(Class<T> clazz);

    /**
     * Updates a specified component in the the provider's model serialization.
     *
     * @param clazz
     *            Data type of component to be updated
     * @param component
     *            The new component
     */
    void updateComponent(Class<T> clazz, T component);

    /**
     * Deletes a specified component from the provider's model serialization. This method only
     * deletes a component and its containments but not the referenced data types which can result
     * in unreferenced data type nodes in the graph when they were not contained by the deleted
     * component. If data types shall not remain in the graph, use
     * {@link #deleteComponentAndDatatypes(Class, String)} instead.
     *
     * @param clazz
     *            Data type of component to be deleted
     * @param id
     *            Id of component to be deleted
     */
    void deleteComponent(Class<T> clazz, String id);

    /**
     * Deletes a specified component from the provider's model serialization. This method also
     * deletes data types which are referenced by the deleted component and not referenced by any
     * other component. If data types shall possibly remain in the graph, use
     * {@link #deleteComponent(Class, String)} instead.
     *
     * @param clazz
     *            Data type of component to be deleted
     * @param id
     *            Id of component to be deleted
     * @param forceDelete
     *            Force method to delete the specified component even if it is referenced as a data
     *            type or as a contained object
     */
    void deleteComponentAndDatatypes(Class<T> clazz, String id, boolean forceDelete);

}