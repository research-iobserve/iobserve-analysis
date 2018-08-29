package org.iobserve.model.correspondence2.generator.impl;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilder;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilderConfiguration;

import org.iobserve.model.correspondence2.Correspondence2Factory;
import org.iobserve.model.correspondence2.Correspondence;
import org.iobserve.model.correspondence2.CorrespondenceModel;
import org.iobserve.model.correspondence2.HighLevelModelElement;
import org.iobserve.model.correspondence2.LowLevelModelElement;

/**
 * Implementation of {@link CorrespondenceModelBuilder}.
 * 
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 *
 */
final class CorrespondenceModelBuilderImpl implements CorrespondenceModelBuilder {
	
	/**configuration instance.*/
	private final CorrespondenceModelBuilderConfiguration configuration;
	
	/**model instance.*/
	private CorrespondenceModel model;
	
	/**
	 * Constructor to build the generator.
	 * @param theConfiguration the configuration.
	 */
	CorrespondenceModelBuilderImpl(final CorrespondenceModelBuilderConfiguration theConfiguration) {
		this.configuration = theConfiguration;
		this.clear();
	}

	@Override
	public void createCorrespondence(final HighLevelModelElement highLevelElement, final LowLevelModelElement lowLevelElement) {
		final String uniqueName = this.createUniqueName(highLevelElement, lowLevelElement);
		
		final Correspondence correspondence = Correspondence2Factory.eINSTANCE.createCorrespondence();
		correspondence.setFrom(highLevelElement);
		correspondence.setTo(lowLevelElement);
		correspondence.setDebugStr(uniqueName);
			
		if (!this.hasCorrespondence(correspondence)) {	
			correspondence.setCorrespondenceModel(this.model);
			this.model.getCorrespondences().add(correspondence);
		} else {
		}
		this.flushToOutput();
	}
	
	private boolean hasCorrespondence(final Correspondence correspondence) {
		for (final Correspondence e : this.model.getCorrespondences()) {
			if (e.getFrom().getId().equals(correspondence.getFrom().getId())
					&& e.getTo().getId().equals(correspondence.getTo().getId())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void clear() {
		this.model = Correspondence2Factory.eINSTANCE.createCorrespondenceModel();
		this.model.setHighLevelModel(this.configuration.getHighLevelModel());
		this.model.setLowLevelModel(this.configuration.getLowLevelModel());
	}

	/**
	 * Flush the current model to the output configured by the {@link CorrespondenceModelBuilderConfiguration#getOutput()}.
	 */
	private void flushToOutput() {
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = reg.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resSet = new ResourceSetImpl();
        resSet.setResourceFactoryRegistry(reg); 
        
        final Resource res = resSet.createResource(this.configuration.getOutput());
        res.getContents().add(this.model.getHighLevelModel());
        res.getContents().add(this.model.getLowLevelModel());
        res.getContents().add(this.model);
        try {
            res.save(null);
        } catch (final IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Creates a unique name composed of the name of the high-level and low-level mdoel element.
	 * @param highLevelElement element
	 * @param lowLevelElement element
	 * @return unique name as string with the format %s#%s
	 */
	private String createUniqueName(final HighLevelModelElement highLevelElement, final LowLevelModelElement lowLevelElement) {
		return String.format("%s#%s", highLevelElement.getEntityName(), lowLevelElement.getEntityName());
	}
}
