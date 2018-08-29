package org.iobserve.model.correspondence2.generator.impl;

import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelProvider;

import org.iobserve.model.correspondence2.Correspondence2Package;
import org.iobserve.model.correspondence2.CorrespondenceModel;

/**
 * Implementation of {@link CorrespondenceModelProvider}.
 * 
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 *
 */
public final class CorrespondenceModelProviderImpl implements CorrespondenceModelProvider {

	@Override
	public Optional<CorrespondenceModel> load(final URI uri) {
		Correspondence2Package.eINSTANCE.eClass();
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("*", new XMIResourceFactoryImpl());

        // Obtain a new resource set
        final ResourceSet resSet = new ResourceSetImpl();

        // Get the resource
        final Resource resource = resSet.getResource(uri, true);
        for (EObject next : resource.getContents()) {
        	if (next instanceof CorrespondenceModel) {
        		return Optional.of((CorrespondenceModel) next);
        	}
        }
        return Optional.empty();
	}

}
