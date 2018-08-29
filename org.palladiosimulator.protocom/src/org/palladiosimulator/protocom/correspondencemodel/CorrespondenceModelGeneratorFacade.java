package org.palladiosimulator.protocom.correspondencemodel;

import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilder;
import org.iobserve.model.correspondence2.generator.CorrespondenceModelBuilderConfiguration;
import org.iobserve.model.correspondence2.generator.impl.CorrespondenceModelBuilderFactory;
import org.palladiosimulator.pcm.core.entity.Entity;

import org.iobserve.model.correspondence2.Correspondence2Factory;
import org.iobserve.model.correspondence2.HighLevelModelElement;
import org.iobserve.model.correspondence2.LowLevelModelElement;

/**
 * This singleton class acts like a facade and provides all the necessary routines to
 * create and maintain the correspondence model during the generation phase.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class CorrespondenceModelGeneratorFacade {
	
	/**singleton instance.*/
	public final static CorrespondenceModelGeneratorFacade INSTANCE = new CorrespondenceModelGeneratorFacade();
	
	/**configuration for cmg*/
	private final CorrespondenceModelBuilderConfiguration configuration;
	
	/**correspondence model generator*/
	private final CorrespondenceModelBuilder cmg;
	
	//TODO (Allesandro: CM use the org.eclipse.debug.ui.launchConfigurationTabGroups and add here an output path. Later got by the run-configuration)
	// Set Paths in start config
	private final String outputPathModel = "C:/Users/Alessandro/Desktop/OutputCorrespondenceModel/correspondence.xml";
	
	private final String repositoryModelPath = "C:/Users/Alessandro/Desktop/workspace_palladio/TestProject/default.repository";
	
	private final RepositoryModelProvider repositoryModelProvider;
	
	/**
	 * Private because of singleton. Create the {@link CorrespondenceModelBuilder}.
	 */
	private CorrespondenceModelGeneratorFacade() {
		this.configuration = new ProtoComCorresModelGenConfig(this.outputPathModel, this.repositoryModelPath);
		this.cmg = CorrespondenceModelBuilderFactory.createBuilder(this.configuration);
		this.repositoryModelProvider = new RepositoryModelProvider(this.configuration.getRepository());
	}
	
	/**
	 * Resets the {@link CorrespondenceModelBuilder}.
	 */
	public void clear() { 
		this.cmg.clear();
	}
		
	/**
	 * Create a new correspondence.
	 * 
	 * @param from high-level model element
	 * @param to low-level model element
	 */
	public void createCorrespondence(final HighLevelModelElemDescr from, final LowLevelModelElemDescr to) {
		if (this.repositoryModelProvider.hasEntityWithId(from.getEnity().getId())) {
			final HighLevelModelElement hlElem = this.getHighLevelModelElement(from);
			final LowLevelModelElement llElem = this.getLowLevelModelElement(to);
			this.cmg.createCorrespondence(hlElem, llElem);
		}
	}
	
	// ************************************************************************
	// HELPER
	// ************************************************************************
	
	/**
	 * This method either returns the model element from the model or it creates one holding
	 * the given entity.
	 * 
	 * @param entity high-level model element description.
	 * @return a high-level model element which represents the given entity.
	 */
	private HighLevelModelElement getHighLevelModelElement(final HighLevelModelElemDescr entity) {
		for (final HighLevelModelElement e : this.configuration.getHighLevelModel().getElements()) {
			if (e.getId().equals(entity.getId())) {
				return e;
			}
		}
		
		final HighLevelModelElement newElem = Correspondence2Factory.eINSTANCE.createHighLevelModelElement();
		newElem.setId(entity.getId());
		newElem.setEntityName(entity.getName());
		
		// get the parent
		if (entity.getEnity().eContainer() != null) {
			final HighLevelModelElement elemParent = this.getHighLevelModelElement(new HighLevelModelElemDescr((Entity)entity.getEnity().eContainer()));
			newElem.setParent(elemParent);
		}
		
		newElem.setModel(this.configuration.getHighLevelModel());
		this.configuration.getHighLevelModel().getElements().add(newElem);
		return newElem;
	}
	
	/**
	 * This method either returns the model element from the model or it creates one holding
	 * the given entity.
	 * 
	 * @param entity low-level model element description.
	 * @return a low-level model element which represents the given entity
	 */
	private LowLevelModelElement getLowLevelModelElement(final LowLevelModelElemDescr entity) {
		for (final LowLevelModelElement e : this.configuration.getLowLevelModel().getElements()) {
			if (e.getId().equals(entity.getId())) {
				return e;
			}
		}
		
		
		final LowLevelModelElement newElem = Correspondence2Factory.eINSTANCE.createLowLevelModelElement();
		newElem.setId(entity.getId());
		newElem.setEntityName(entity.getName());
		newElem.setModel(this.configuration.getLowLevelModel());
		
		// get the parent if this is not an entity without parent
		if (entity.getParentId() != null) {
			LowLevelModelElement parent = null;
			for (final LowLevelModelElement e : this.configuration.getLowLevelModel().getElements()) {
				if (e.getId().equals(entity.getParentId())) {
					parent = e;
					break;
				}
			}
			
			if (parent != null) {
				newElem.setParent(parent);
			} else {
				// create the parent
				final String parentId = entity.getParentId();
				final String parentName = parentId.substring(0, parentId.lastIndexOf("."));
				final LowLevelModelElement newParent = Correspondence2Factory.eINSTANCE.createLowLevelModelElement();
				newParent.setId(parentId);
				newParent.setEntityName(parentName);
				newParent.setModel(this.configuration.getLowLevelModel());
				this.configuration.getLowLevelModel().getElements().add(newParent);
				newElem.setParent(newParent);
			}
		}
		
		this.configuration.getLowLevelModel().getElements().add(newElem);
		return newElem;
	}
}
