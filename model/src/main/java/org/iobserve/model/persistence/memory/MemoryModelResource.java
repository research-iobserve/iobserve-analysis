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
package org.iobserve.model.persistence.memory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.IModelResource;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.ModelGraphFactory;
import org.iobserve.model.persistence.neo4j.NodeLookupException;

/**
 * @author Reiner Jung
 *
 * @param <R>
 *            model root class
 */
public class MemoryModelResource<R extends EObject> implements IModelResource<R> {

    private final Set<EFactory> factories = new HashSet<>();
    private R model;

    public MemoryModelResource(final EPackage ePackage) {
        if (ePackage != null) {
            this.checkPackage(ePackage);
        }
    }

    private void checkPackage(final EPackage ePackage) {
        if (!this.factories.contains(ePackage.getEFactoryInstance())) {
            this.factories.add(ePackage.getEFactoryInstance());
            for (final EClassifier classifier : ePackage.getEClassifiers()) {
                if (classifier instanceof EClass) {
                    final EClass clazz = (EClass) classifier;
                    for (final EReference reference : clazz.getEAllReferences()) {
                        this.checkPackage(reference.getEReferenceType().getEPackage());
                    }
                }
            }
        }
    }

    @Override
    public void clearResource() {
        this.model = null;
    }

    @Override
    public void storeModelPartition(final R rootElement) throws DBException {
        this.model = rootElement;
    }

    @Override
    public <T extends EObject> void updatePartition(final T object) throws NodeLookupException, DBException {
        // not necessary, as object is already the real object
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void deleteObject(final EObject object) throws DBException {
        final EObject container = object.eContainer();
        final EStructuralFeature feature = object.eContainingFeature();
        if (feature.isMany()) {
            ((EList<EObject>) container.eGet(feature)).remove(object);
        } else {
            container.eSet(feature, null);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EObject> List<T> collectAllObjectsByType(final Class<T> clazz, final EClass eClass)
            throws DBException {
        final List<T> results = new ArrayList<>();
        final TreeIterator<EObject> collection = this.model.eAllContents();
        while (collection.hasNext()) {
            final EObject object = collection.next();
            if (clazz.isAssignableFrom(object.getClass())) {
                results.add((T) object);
            }
        }
        return results;
    }

    @Override
    public R getAndLockModelRootNode(final Class<R> clazz, final EClass eClass) throws DBException {
        return this.model;
    }

    @Override
    public R getModelRootNode(final Class<R> clazz, final EClass eClass) throws DBException {
        return this.model;
    }

    @Override
    public <T extends EObject> boolean isTypeManagedByResource(final T proxyObject) {
        for (final EFactory factory : this.factories) {
            try {
                final EObject instance = factory.create(proxyObject.eClass());
                if (instance != null) {
                    return true;
                }
            } catch (final IllegalArgumentException e) {

            }
        }

        return false;
    }

    @Override
    public <T extends EObject> T resolve(final T proxyObject) throws InvocationException, DBException {
        EcoreUtil.resolve(proxyObject, this.model.eResource());
        return proxyObject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EObject> List<T> findObjectsByTypeAndProperty(final Class<T> clazz, final EClass eClass,
            final String key, final String value) throws DBException {
        final List<T> results = new ArrayList<>();
        final TreeIterator<EObject> contents = this.model.eAllContents();

        final EAttribute attribute = this.findAttribute(eClass, key);

        if (attribute != null) {
            while (contents.hasNext()) {
                final EObject object = contents.next();
                if (clazz.isAssignableFrom(object.getClass())) {
                    final Object data = object.eGet(attribute);
                    if (data.toString().equals(value)) {
                        results.add((T) object);
                    }
                }
            }

        }
        return results;
    }

    private EAttribute findAttribute(final EClass eClass, final String key) {
        for (final EAttribute attribute : eClass.getEAllAttributes()) {
            if (attribute.getName().equals(key)) {
                return attribute;
            }
        }
        return null;
    }

    @Override
    public List<EObject> collectReferencingObjectsByTypeAndProperty(final Class<?> clazz, final EClass eClass,
            final String property) throws DBException {
        throw new DBException("not implemented");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends EObject> T findObjectByTypeAndId(final Class<T> clazz, final EClass eClass,
            final String identification) {
        final TreeIterator<EObject> contents = this.model.eAllContents();

        while (contents.hasNext()) {
            final EObject object = contents.next();
            if (ModelGraphFactory.getIdentification(object).equals(identification)) {
                return (T) object;
            }
        }

        return null;
    }

    @Override
    public <T extends EObject> T findAndLockObjectById(final Class<T> clazz, final EClass eClass,
            final String identification) throws DBException {
        return this.findObjectByTypeAndId(clazz, eClass, identification);
    }

}
