package org.iobserve.analysis.model.correspondence;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.correspondence.ArchitecturalModel;
import org.iobserve.analysis.model.correspondence.ArchitecturalModelElement;
import org.iobserve.analysis.model.correspondence.Correspondence;
import org.iobserve.analysis.model.correspondence.CorrespondenceModel;
import org.iobserve.analysis.model.correspondence.CorrespondenceFactory;
import org.iobserve.analysis.model.correspondence.ImplementationArtifact;
import org.iobserve.analysis.model.correspondence.ImplementationArtifactSet;
import org.palladiosimulator.pcm.core.entity.Entity;

public class CorrespondenceModelGenerator {

	/**singleton instance.*/
	public final static CorrespondenceModelGenerator INSTANCE = new CorrespondenceModelGenerator();
	
	/**todo: in and output paths nicht fest initialisieren?*/
	private URI repositoryModelPath = URI.createFileURI(".repository");
	private URI outputPath = URI.createFileURI("/correspondence.rac");
	
	private RepositoryModelProvider repositoryModelProvider;
	
	private CorrespondenceModel model;
	private ArchitecturalModel architectModel;
	private ImplementationArtifactSet implArtifacts;
	
	/**
	 * Constructor, initializes model elements.
	 */
	private CorrespondenceModelGenerator() {
		this.repositoryModelProvider = new RepositoryModelProvider(this.repositoryModelPath);
		
		this.model = CorrespondenceFactory.eINSTANCE.createCorrespondenceModel();
		this.architectModel = CorrespondenceFactory.eINSTANCE.createArchitecturalModel();
		this.implArtifacts = CorrespondenceFactory.eINSTANCE.createImplementationArtifactSet();
		
		this.model.setArchitecturalModel(this.architectModel);
		this.model.setImplementationArtifacts(this.implArtifacts);
	}
	
	/**
	 * Creates an correspondence from one ArchitectualModelElement (from) to an ImplementationArtifact (identified by 'to').
	 * @param from The actual model element.
	 * @param to The signature of the implementation artefact.
	 * 
	 * If the model element is not actual in the repository, no elements or correspondence is created.
	 */
	public void createCorrespondence(Entity from, String to) {
		if (this.repositoryModelProvider.hasEntityWithId(from.getId())) {
			final ArchitecturalModelElement fromElement = createArchitecturalModelElement(from);
			final ImplementationArtifact toArtifact = createImplementationArtifact(to);
			createCorrespondence(fromElement, toArtifact);
		}
	}
	
	private void createCorrespondence(ArchitecturalModelElement from, ImplementationArtifact to) {
		for(Correspondence correspondence : this.model.getCorrespondences()) {		
			if (correspondence.getFrom().getElement().getId().equals(from.getElement().getId())
					&& correspondence.getTo().getArtifactId().equals(to.getArtifactId())) {
				return;
			}
		}
		
		Correspondence correspondence = CorrespondenceFactory.eINSTANCE.createCorrespondence();
		correspondence.setFrom(from);
		correspondence.setTo(to);
		
		this.model.getCorrespondences().add(correspondence);
		flushToOutput();
	}
	
	private ArchitecturalModelElement createArchitecturalModelElement(Entity entity) {
		ArchitecturalModelElement newArchitectElement = getArchitecturalModelElement(entity);
		
		if (newArchitectElement == null) {
			newArchitectElement = CorrespondenceFactory.eINSTANCE.createArchitecturalModelElement();
			newArchitectElement.setElement(entity);
			this.architectModel.getElements().add(newArchitectElement);
		}
		
		return newArchitectElement;
	}
	
	private ArchitecturalModelElement getArchitecturalModelElement(Entity entity) {
		for(ArchitecturalModelElement element : this.architectModel.getElements()) {
			if (element.getElement().getId() == entity.getId()) {
				return element;
			}
		}
		
		return null;
	}
	
	private ImplementationArtifact createImplementationArtifact(String artifactId) {
		ImplementationArtifact newArtifact = getImplementationArtifact(artifactId);
		
		if (newArtifact == null) {
			newArtifact = CorrespondenceFactory.eINSTANCE.createImplementationArtifact();
			newArtifact.setArtifactId(artifactId);
			this.implArtifacts.getArtifacts().add(newArtifact);
		}
		
		
		return newArtifact;
	}
	
	private ImplementationArtifact getImplementationArtifact(String artifactId) {
		for(ImplementationArtifact artifact : this.implArtifacts.getArtifacts()) {
			if (artifact.getArtifactId() == artifactId) {
				return artifact;
			}
		}
		
		return null;
	}
	
	private void flushToOutput() {
		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = reg.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resSet = new ResourceSetImpl();
        resSet.setResourceFactoryRegistry(reg); 
        
        final Resource res = resSet.createResource(outputPath);
        res.getContents().add(this.architectModel);
        res.getContents().add(this.implArtifacts);
        res.getContents().add(this.model);
        try {
            res.save(null);
        } catch (final IOException e) {
            e.printStackTrace();
        }
	}
}