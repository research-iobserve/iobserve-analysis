/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.modelprovider;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import de.uka.ipd.sdq.identifier.Identifier;

/**
 * Provides common method for model loading/saving etc.
 *
<<<<<<< HEAD
 * @author Robert Heinrich, Alessandro Giusa, alessandrogiusa@gmail.com
=======
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
 * @version 1.0, 20.01.2015
 * @param <T>
 */
public abstract class AbstractModelProvider<T> {

	// ********************************************************************
	// * VALUES
	// ********************************************************************

	private final URI uriModelInstance;

	// ********************************************************************
	// * VARIABLES
	// ********************************************************************

	private T model;

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

<<<<<<< HEAD
=======
	// TODO exception handling has to be done
>>>>>>> 8369799fad6bbba0ee0c594bd69ce6afef0b7b41
	public AbstractModelProvider(final URI uriModelInstance) {
		this.uriModelInstance = uriModelInstance;

		// perhaps this should be called client?
		this.loadModel();
	}

	/**
	 * Get an instance of the package where this model belongs to. <br>
	 * <br>
	 * For instance:
	 *
	 * <pre>
	 * public EPackage getPackage() {
	 * 	return AllocationPackage.eINSTANCE;
	 * }
	 * </pre>
	 */
	public abstract EPackage getPackage();

	@SuppressWarnings("unchecked")
	private void loadModel() {
		this.getPackage().eClass();

		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);

		final Resource resource = resSet.getResource(this.uriModelInstance, true);

		// Alessandro Giusa
		// if contents is empty OutOfBounds is thrown,
		// but is is evtl. necessary since it is a general problem which should not appear?
		// therefore it is not handled at this point
		this.model = (T) resource.getContents().get(0);
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	/**
	 * Get the loaded model
	 *
	 * @return
	 */
	public T getModel() {
		return this.model;
	}

	/**
	 * Get a component of the model which implements {@link Identifier}
	 *
	 * @param id
	 * @param list
	 *            where to search
	 * @return
	 */
	public Identifier getIdentifiableComponent(final String id,
			final EList<? extends Identifier> list) {
		for (final Identifier next : list) {
			if (next.getId().equals(id)) {
				return next;
			}
		}
		return null;
	}
}
