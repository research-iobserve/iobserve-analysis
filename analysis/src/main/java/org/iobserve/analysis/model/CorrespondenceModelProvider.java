package org.iobserve.analysis.model;

import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.analysis.model.correspondence.ArchitecturalModelElement;
import org.iobserve.analysis.model.correspondence.Correspondence;
import org.iobserve.analysis.model.correspondence.CorrespondenceModel;
import org.iobserve.analysis.model.correspondence.CorrespondencePackage;

public class CorrespondenceModelProvider extends AbstractModelProvider<CorrespondenceModel>  {

	public CorrespondenceModelProvider(URI theUriModelInstance) {
		super(theUriModelInstance);
	}

	@Override
	protected EPackage getPackage() {
		return CorrespondencePackage.eINSTANCE;
	}

	@Override
	public void resetModel() {
		this.getModel().getCorrespondences().clear();
		this.getModel().getArchitecturalModel().getElements().clear();
		this.getModel().getImplementationArtifacts().getArtifacts().clear();
	}
	
	public Optional<ArchitecturalModelElement> getCorrespondent(String classSig) {
		for(Correspondence correspondence : this.getModel().getCorrespondences()) {
			if(correspondence.getTo().getArtifactId().equals(classSig)) {
				return Optional.of(correspondence.getFrom());
			}
		}
		
		return Optional.empty();
	}
	
	public Optional<ArchitecturalModelElement> getCorrespondent(final String classSig, final String operationSig) {
		String implementationArtifactId = classSig.trim().replaceAll(" ", "") + operationSig.trim().replaceAll(" ", "");
		
		for(Correspondence correspondence : this.getModel().getCorrespondences()) {
			if(correspondence.getTo().getArtifactId().equals(implementationArtifactId)) {
				return Optional.of(correspondence.getFrom());
			}
		}
		
		return Optional.empty();
	}
	
	public boolean containsCorrespondence(final String classSig, final String operationSig) {
		return this.getCorrespondent(classSig, operationSig).isPresent();
	}

}
