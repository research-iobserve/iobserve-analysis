/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.model;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Provides facilities to save PCM models.
 * @author Robert Heinrich, 
 * @author Alessandro Giusa
 */
public final class PcmModelSaver {
	
	/**used output for the model.*/
	private final URI output;
	
	/**
	 * Create model saver with using the given output.
	 * @param theOutput output to save to
	 */
	public PcmModelSaver(final URI theOutput) { 
		this.output = theOutput;
	}
	
	/**
	 * Save the model to the output.
	 * @param model model to save
	 */
	public void save(final EObject model) {
		saveModel(model, this.output);
	}
	
	/**
	 * Get the output of the model.
	 * @return URI where model gets written
	 */
	public URI getOutput() {
		return this.output;
	}
	
	/**
	 * Saves the given object at the given uri.
	 * @param obj object to save
	 * @param uri uri where to save the object
	 */
	public static void saveModel(final EObject obj, final URI uri) {
		final Resource.Factory.Registry reg = 
				Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);

		final Resource res = resSet.createResource(uri);
		res.getContents().add(obj);
		try {
			res.save(null);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
